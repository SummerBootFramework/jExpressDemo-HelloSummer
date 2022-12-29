package org.jexpress.demo.restful.jaxrs;

import com.google.inject.Singleton;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jexpress.demo.dto.Pong;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.boot.annotation.Ping;
import org.summerboot.jexpress.nio.server.ws.rs.BootController;

@Controller
@Singleton
@Path("/hellosummer")
@OpenAPIDefinition(//OAS v3
        info = @Info(
                title = "Hello Summer! Demo02",
                version = "Demo 02",
                description = "This is demo",
                contact = @Contact(
                        name = "jExpress.org",
                        email = "info@jexpress.org"
                )
        ),
        servers = {
            @Server(url = "https://localhost:8311", description = "Local Development server")
        }
)
public class WebController extends BootController {

    // curl -k https://localhost:8311/hellosummer/ping
    @GET
    @Path("/ping")
    @Ping //this annotation will override BootController.ping()
    public void myPing() {
    }

    /*
     * curl -k https://localhost:8311/hellosummer/hello/234 -H "Accept":"application/xml"
     * curl -k https://localhost:8311/hellosummer/hello/234 -H "Accept":"application/json"
     */
    @GET
    @Path("/hello/{name}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Pong hello(@PathParam("name") long value) {
        return new Pong("Hello Summer", value);
    }

    @GET
    @Path("/helloAdmin/{name}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed({"demoadmin"})
    public Pong ping2(@PathParam("name") long value) {
        return new Pong("Hello Summer Admin", value);
    }

    @GET
    @Path("/helloUsers/{name}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @PermitAll
    public Pong ping3(@PathParam("name") long value) {
        return new Pong("Hello Summer Users", value);
    }
}
