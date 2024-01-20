package org.jexpress.demo.app;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.summerboot.jexpress.boot.annotation.Controller;

@Controller
@Path("/hellosummer1")
public class SimpleWebController {

    private static final String USER_INPUT_VALIDATION_REGEX = "[a-zA-Z\\#\\-' ]{1,10}";

    // 200 JSON: curl -k https://localhost:8311/hellosummer1/hello/John-Doe -H "Accept":"application/json
    // 200 XML: curl -k https://localhost:8311/hellosummer1/hello/John-Doe -H "Accept":"application/xml"
    // 400: curl -k https://localhost:8311/hellosummer1/hello/John.Doe -H "Accept":"application/json"
    // 400: curl -k https://localhost:8311/hellosummer1/hello/John.Doe -H "Accept":"application/xml"
    @GET
    @Path("/hello/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //@RolesAllowed({"Employee"})
    public MyResponse hello(@PathParam("name") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String name) {
        return new MyResponse(SimpleWebController.class.getName(), "GET Hello " + name);
    }

    @POST
    @Path("/hello/post")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MyResponse helloPost(JsonNode name) {
        return new MyResponse(SimpleWebController.class.getName(), "POST Hello " + name);
    }
}
