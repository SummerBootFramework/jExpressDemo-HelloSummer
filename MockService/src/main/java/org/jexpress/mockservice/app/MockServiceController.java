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

import java.io.IOException;
import java.util.Properties;

import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;

/**
 * @author 魏泽北
 */
@Singleton
@Controller
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MockServiceController {

    // curl -v -k https://localhost:8211/service1/action1 -H "Accept: application/json" -H "Authorization: Bearer abcdefg"
    // curl -v -k https://localhost:8211/service1/action1 -H "Accept: application/json" -H "Authorization: Bearer abcdefg" -X POST -d '{"name":"John Done","title":"Boss"}'
    @GET
    @POST
    @PUT
    @PATCH
    @DELETE
    @Path("")
    public String mockService(String body, @Parameter(hidden = true) final ServiceContext context) throws IOException {
        String filePath = "mock_response" + context.uri() + "_" + context.method();
        Properties responseHeaders = Utils.loadProperties(filePath + ".properties", true);
        HttpResponseStatus status = Utils.setResponseHeaders(responseHeaders, context);
        context.status(status);
        return Utils.loadFileContent(filePath + ".txt", true);
    }

}
