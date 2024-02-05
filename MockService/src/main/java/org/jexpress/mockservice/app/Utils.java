package org.jexpress.mockservice.app;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.security.JwtUtil;
import org.summerboot.jexpress.security.auth.AuthConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.commons.lang3.StringUtils;
import org.graalvm.polyglot.Context;
import org.summerboot.jexpress.boot.BootConstant;
import static org.summerboot.jexpress.util.FormatterUtil.EMPTY_STR_ARRAY;

/**
 * @author 魏泽北
 */
public class Utils {

    public static final String KEY_RESPONSE_STATUS_CODE = "Response_Status_Code";
    public static final String KEY_RESPONSE_DELAY_SECOND = "Response_Delay_Second";

    public static final String RSPONSE_HEADER_FILE_CONTENT = """
            ####################
            # Response Ctrl    #
            ####################
            Response_Status_Code=200  
            Response_Delay_Second=0
                                                                         
            ####################
            # Response Headers #
            ####################
            #header1=value1
            """;

    public static HttpResponseStatus setResponseHeaders(Properties responseHeaders, ServiceContext context) {
        HttpResponseStatus status = HttpResponseStatus.OK;
        if (responseHeaders == null) {
            return status;
        }
        String responseStatusCode = responseHeaders.getProperty(KEY_RESPONSE_STATUS_CODE);
        if (responseStatusCode != null) {
            int code = Integer.parseInt(responseStatusCode);
            status = HttpResponseStatus.valueOf(code);
            responseHeaders.remove(KEY_RESPONSE_STATUS_CODE);
        }
        int delay = 0;
        String responseDelaySecond = responseHeaders.getProperty(KEY_RESPONSE_DELAY_SECOND);
        if (responseDelaySecond != null) {
            delay = Integer.parseInt(responseDelaySecond);
            responseHeaders.remove(KEY_RESPONSE_DELAY_SECOND);
        }
        for (Object key : responseHeaders.keySet()) {
            Object value = responseHeaders.get(key);
            context.responseHeader(key.toString(), value);
        }
        if (delay > 0) {
            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        return status;
    }

    public static String escape4Filename(String s) {
        return s.replaceAll("[?]", "_");
    }

    public static Properties loadProperties(String fileName, boolean createIfNotExist) throws IOException {
        File fileEntry = new File(escape4Filename(fileName)).getAbsoluteFile();
        if (!fileEntry.exists()) {
            if (createIfNotExist) {
                fileEntry.getParentFile().mkdirs();
                //fileEntry.createNewFile();
                try (FileWriter writer = new FileWriter(fileEntry);) {
                    writer.write(RSPONSE_HEADER_FILE_CONTENT);
                }
            }
            return null;
        }
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(fileEntry); InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);) {
            props.load(isr);
        }
        return props;
    }

    public static String loadFileContent(String fileName, boolean createIfNotExist, String defaultContent) throws IOException {
        File fileEntry = new File(escape4Filename(fileName)).getAbsoluteFile();
        if (!fileEntry.exists()) {
            if (createIfNotExist) {
                fileEntry.getParentFile().mkdirs();
                if (StringUtils.isBlank(defaultContent)) {
                    fileEntry.createNewFile();
                } else {
                    try (FileWriter writer = new FileWriter(fileEntry);) {
                        writer.write(defaultContent);
                    }
                }
            }
            return null;
        }
        return Files.readString(Paths.get(fileEntry.getAbsolutePath()));
    }

    public static String generateJWT(String id, String issuer, String subject, String audience, int ttlMinutes) {
//        RoleMapping rm = AuthConfig.cfg.getRole(roleName);
//        if (rm == null) {
//            throw new IllegalArgumentException("Role (" + roleName + ") not defined in " + AuthConfig.cfg.getCfgFile());
//        }
//        Set<String> g = rm.getGroups();
//        String audience = FormatterUtil.toCSV(g);

        JwtBuilder jb = Jwts.builder()
                .setId(id)
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience);
        return JwtUtil.createJWT(AuthConfig.cfg.getJwtSigningKey(), jb, Duration.ofMinutes(ttlMinutes));
    }

    private static final String JS_CODE1 = """                         
                        var app = {                        
                         ruleEngine: function(requestHeader, queryParam, requestBody) {
                        """;
    private static final String JS_CODE2 = """                         
                         }
                        }
                        """;
    public static final String JS_FILE_CONTENT = """
            // @param requestHeader as Map<String, String>
            // @param queryParam as Map<String, String>
            // @param requestBody as String
            // @return a postfix string, to append at the end of the response file name, which content will send back as response
            // example:
            // var isJson = requestHeader["Content-Type"].includes('json');
            // var json = JSON.parse(requestBody);
            // var value1 = queryParam.key1;
            // return isJson ? 'json' : 'xml';
            """;

    public static String javascriptRuleEngine(String jsCode, List<Map.Entry<String, String>> listOfEntry, Map<String, String> queryParam, String requestBody, final ServiceContext context) throws ScriptException, NoSuchMethodException {
        if (StringUtils.isBlank(jsCode)) {
            return null;
        }
        String[] jsLines = StringUtils.isBlank(jsCode) ? EMPTY_STR_ARRAY : jsCode.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        for (String jsLine : jsLines) {
            if (StringUtils.isBlank(jsLine)) {
                continue;
            }
            String trimed = jsLine.trim();
            if (trimed.startsWith("//") || trimed.startsWith("#")) {
                continue;
            }
            sb.append(jsLine).append(BootConstant.BR);
        }

        Map<String, String> requestHeader = new HashMap();
        for (Map.Entry<String, String> entry : listOfEntry) {
            requestHeader.put(entry.getKey(), entry.getValue());
        }
        ScriptEngine graalEngine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                        .allowAllAccess(true)
        );
        jsCode = JS_CODE1 + sb.toString() + JS_CODE2;
        if (context != null) {
            context.memo("jsCode", jsCode);
        }
        graalEngine.eval(jsCode);
        Invocable invocable = (Invocable) graalEngine;
        Object thiz = graalEngine.get("app");

        Object result = invocable.invokeMethod(thiz, "ruleEngine", requestHeader, queryParam, requestBody);
        return result == null ? null : result.toString();
    }
}
