package org.jexpress.mockservice.app;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.security.JwtUtil;
import org.summerboot.jexpress.security.auth.AuthConfig;
import org.summerboot.jexpress.security.auth.RoleMapping;
import org.summerboot.jexpress.util.FormatterUtil;

/**
 *
 * @author 魏泽北
 */
public class Utils {

    public static final String KEY_RESPONSE_STATUS_CODE = "Response_Status_Code";

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
        for (Object key : responseHeaders.keySet()) {
            Object value = responseHeaders.get(key);
            context.responseHeader(key.toString(), value);
        }
        return status;
    }

    public static Properties loadProperties(String fileName) throws IOException {
        File fileEntry = new File(fileName).getAbsoluteFile();
        if (!fileEntry.exists()) {
            return null;
        }
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(fileEntry); InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);) {
            props.load(isr);
        }
        return props;
    }

    public static String loadFileContent(String fileName) throws IOException {
        File fileEntry = new File(fileName).getAbsoluteFile();
        return Files.readString(Paths.get(fileEntry.getAbsolutePath()));
    }

    public static String generateJWT(String roleName, String id, String issuer, String subject, int ttlMinutes) {
        RoleMapping rm = AuthConfig.cfg.getRole(roleName);
        if (rm == null) {
            throw new IllegalArgumentException("Role (" + roleName + ") not defined in " + AuthConfig.cfg.getCfgFile());
        }
        Set<String> g = rm.getGroups();
        String groupNames = FormatterUtil.toCSV(g);

        JwtBuilder jb = Jwts.builder()
                .setId(id)
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(groupNames);
        return JwtUtil.createJWT(AuthConfig.cfg.getJwtSigningKey(), jb, Duration.ofMinutes(ttlMinutes));
    }
}
