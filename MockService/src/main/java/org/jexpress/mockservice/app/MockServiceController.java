package org.jexpress.mockservice.app;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;

/**
 *
 * @author 魏泽北
 */
@Singleton
@Controller
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MockServiceController {

    // curl -k https://localhost:8211/service1/action1 -H "Accept: application/json" -H "Authorization: Bearer abcdefg"
    // curl -k https://localhost:8211/service1/action1 -H "Accept: application/json" -H "Authorization: Bearer abcdefg" -X POST -d '{json}'
    @GET
    @POST
    @PUT
    @PATCH
    @DELETE
    @Path("")
    public String returnMockJsonContent(String body, @Parameter(hidden = true) final ServiceContext context) throws IOException {
        String filePath = "mock_response" + context.uri() + "_" + context.method();
        Properties responseHeaders = loadProperties(filePath + ".properties");
        HttpResponseStatus status = setResponseHeaders(responseHeaders, context);
        context.status(status);
        return loadFileContent(filePath + ".txt");
    }

    protected Properties loadProperties(String fileName) throws IOException {
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

    protected String loadFileContent(String fileName) throws IOException {
        File fileEntry = new File(fileName).getAbsoluteFile();
        return Files.readString(Paths.get(fileEntry.getAbsolutePath()));
    }

    protected final String KEY_RESPONSE_STATUS_CODE = "Response_Status_Code";

    protected HttpResponseStatus setResponseHeaders(Properties responseHeaders, ServiceContext context) {
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
}
