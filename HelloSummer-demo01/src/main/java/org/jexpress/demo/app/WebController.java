package org.jexpress.demo.app;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.ws.rs.BootController;

@Controller
@Path("/hellosummer")
public class WebController extends BootController {

    // curl http://localhost:8311/hellosummer/hello/JohnDoe
    @GET
    @Path("/hello/{name}")
    public String hello(@PathParam("name") String name) {
        return "Hello " + name;
    }
}
