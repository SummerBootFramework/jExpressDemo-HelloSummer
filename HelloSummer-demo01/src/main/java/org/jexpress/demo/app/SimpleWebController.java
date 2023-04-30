package org.jexpress.demo.app;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.summerboot.jexpress.boot.annotation.Controller;

@Controller
@Path("/hellosummer")
public class SimpleWebController {

    // curl http://localhost:8311/hellosummer/hello/JohnDoe
    @GET
    @Path("/hello/{name}")
    @RolesAllowed({"Employee"})
    public String hello(@PathParam("name") String name) {
        return "Hello " + name;
    }

    @POST
    @Path("/hello/post")
    public String helloPost(JsonNode name) {
        return "Hello " + name;
    }
}
