package org.jexpress.mockservice.app;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.netty.handler.codec.http.HttpResponseStatus;
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
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.security.JwtUtil;
import org.summerboot.jexpress.security.auth.AuthConfig;

/**
 *
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

    public static Properties loadProperties(String fileName, boolean createIfNotExist) throws IOException {
        File fileEntry = new File(fileName).getAbsoluteFile();
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

    public static String loadFileContent(String fileName, boolean createIfNotExist) throws IOException {
        File fileEntry = new File(fileName).getAbsoluteFile();
        if (!fileEntry.exists()) {
            if (createIfNotExist) {
                fileEntry.getParentFile().mkdirs();
                fileEntry.createNewFile();
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
}
