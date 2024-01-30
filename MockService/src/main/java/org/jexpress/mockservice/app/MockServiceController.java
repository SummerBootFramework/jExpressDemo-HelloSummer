package org.jexpress.mockservice.app;

import com.google.inject.Singleton;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @POST
    @GET
    @Path("")
    public String returnMockJsonContent(String body, @Parameter(hidden = true) final ServiceContext context) throws IOException {
        return loadFile("mock_response" + context.uri() + "_" + context.method() + ".json");
    }

    private String loadFile(String fileName) throws IOException {
        File fileEntry = new File(fileName).getAbsoluteFile();
        return Files.readString(Paths.get(fileEntry.getAbsolutePath()));
    }
}
