package org.jexpress.demo.restful;

import com.google.inject.Singleton;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.nio.server.ws.rs.BootController;


@Singleton
@Controller(implTag = "RoleBased")// to enable it, start application with -use RoleBased or -use RoleBased WebBased to enable both role and web based controllers
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
public class MyControllerRoleBased extends BootController {

    // curl -k https://localhost:8311/hellosummer/ping
    //@Ping //this annotation will override BootController.ping()
    @GET
    @Path("/ping")
    public void myPing() {
    }

    /*
     * curl -k https://localhost:8311/hellosummer/hello/234 -H "Accept":"application/xml"
     * curl -k https://localhost:8311/hellosummer/hello/234 -H "Accept":"application/json"
     */
    @GET
    @Path("/hello/anonymous")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Pong anonymous(ServiceContext context) {
        return new Pong("Hello stranger: " + context.caller(), context.hit());
    }

    @GET
    @Path("/helloAdmin/user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @PermitAll
    public Pong loginedUserOnly(ServiceContext context) {
        return new Pong("Hello user: " + context.caller(), context.hit());
    }

    @GET
    @Path("/helloAdmin/admin")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed({"AppAdmin"})
    public Pong adminOnly(ServiceContext context) {
        return new Pong("Hello admin: " + context.caller(), context.hit());
    }

    @GET
    @Path("/helloAdmin/employee")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed({"Employee"})
    public Pong employeeOnly(ServiceContext context) {
        return new Pong("Hello employee: " + context.caller(), context.hit());
    }

    public static class Pong {

        private final String name;
        private final long value;
        private final OffsetDateTime receivedTime;

        public Pong(String name, long value) {
            this.name = name;
            this.value = value;
            this.receivedTime = OffsetDateTime.now();
        }

        public String getName() {
            return name;
        }

        public long getValue() {
            return value;
        }

        public OffsetDateTime getReceivedTime() {
            return receivedTime;
        }

    }
}
