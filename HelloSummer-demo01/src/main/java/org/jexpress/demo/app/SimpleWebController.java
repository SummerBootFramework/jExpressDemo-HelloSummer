package org.jexpress.demo.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.MatrixParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.jexpress.demo.app.integration.grpc.Hello1Client;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.boot.annotation.Deamon;
import org.summerboot.jexpress.nio.server.SessionContext;
import org.summerboot.jexpress.nio.server.ws.rs.PingController;

@Controller
@Path("/hellosummer1")
public class SimpleWebController extends PingController {

    @Inject
    private Hello1Client grpcClient1;

    private static final String USER_INPUT_VALIDATION_REGEX = "[a-zA-Z\\#\\-' ]{1,10}";

    // 200 JSON: curl -v -k https://localhost:8311/hellosummer1/hello/John-Doe -H "Accept: application/json" -H "Authorization: Bearer abcdefg"
    // 200 XML: curl -v -k https://localhost:8311/hellosummer1/hello/John-Doe -H "Accept: application/xml"
    // 400: curl -v -k https://localhost:8311/hellosummer1/hello/John.Doe -H "Accept: application/json"
    // 400: curl -v -k https://localhost:8311/hellosummer1/hello/John.Doe -H "Accept: application/xml"
    @GET
    @Path("/hello/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //@Log(requestHeader = false, requestBody = false, responseHeader = false, responseBody = true, hideJsonStringFields = "value")
    //@RolesAllowed({"Employee"})
    public MyResponse hello(@PathParam("name") /*@Pattern(regexp = USER_INPUT_VALIDATION_REGEX)*/ String name,
                            @QueryParam("key !@#$%^&*()_+-=[]\\{}|;':\",./<>? 中文 ") String value,
                            @MatrixParam("color") String color,
                            @MatrixParam("color2") String color2) {
        return new MyResponse(SimpleWebController.class.getName(), "\n\t name=" + name + "\n\t value=" + value + "\n\t color=" + color + "\n\t color2=" + color2);
    }

    @POST
    @Path("/hello/post")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MyResponse helloPost(JsonNode name) {
        return new MyResponse(SimpleWebController.class.getName(), "POST Hello " + name);
    }

    @GET
    @Path("hello/grpc/{input}")
    @Deamon//(requiredHealthChecks = "ins1")
    public MyResponse grpc(@PathParam("input") String input, SessionContext context) {
        String greeting = grpcClient1.bizFunction(input, "", context);
        if (greeting == null) {
            return null;
        }
        return new MyResponse("grpc", greeting);
    }
}
