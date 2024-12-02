package org.jexpress.demo.restful.service;

import com.google.inject.Singleton;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jexpress.demo.app.instrumentation.HealthChecker;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.boot.annotation.Deamon;
import org.summerboot.jexpress.boot.instrumentation.HealthMonitor;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.nio.server.ws.rs.BootController;

import java.io.IOException;
import java.time.OffsetDateTime;

@Singleton
@Controller(implTag = "RoleBased")
// to enable it, start application with -use RoleBased or -use RoleBased WebBased to enable both role and web based controllers
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

    // curl -v -k https://localhost:8311/hellosummer/ping
    //@Ping //this annotation will override BootController.ping()
    @GET
    @Path("/ping")
    //@Ping
    public void myPing() {
    }

    /*
     * curl -v -k https://localhost:8311/hellosummer/hello/234 -H "Accept":"application/xml"
     * curl -v -k https://localhost:8311/hellosummer/hello/234 -H "Accept":"application/json"
     */
    @GET
    @Path("/hello/anonymous/{number}")
    @Deamon
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Pong anonymous(@PathParam("number") int number, ServiceContext context) throws IOException {
        HealthChecker.a = number;
        switch (number) {
            case 1 -> {
                throw new IOException("test 1");
            }
            case 2 -> {
                HealthMonitor.inspect();
            }
        }
        return new Pong("Hello stranger: " + context.caller(), context.txId());
    }

    @GET
    @Path("/helloAdmin/user")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @PermitAll
    public Pong loginedUserOnly(ServiceContext context) {
        return new Pong("Hello user: " + context.caller(), context.txId());
    }

    @GET
    @Path("/helloAdmin/admin")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed({"AppAdmin"})
    public Pong adminOnly(ServiceContext context) {
        return new Pong("Hello admin: " + context.caller(), context.txId());
    }

    @GET
    @Path("/helloAdmin/employee")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed({"Employee"})
    public Pong employeeOnly(ServiceContext context) {
        return new Pong("Hello employee: " + context.caller(), context.txId());
    }

    @GET
    @Path("/helloAdmin/adminoremployee")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed({"AppAdmin", "Employee"})
    public Pong adminorEmployeeOnly(ServiceContext context) {
        return new Pong("Hello employee: " + context.caller(), context.txId());
    }

    public static class Pong {

        private final String name;
        private final String value;
        private final OffsetDateTime receivedTime;

        public Pong(String name, String value) {
            this.name = name;
            this.value = value;
            this.receivedTime = OffsetDateTime.now();
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public OffsetDateTime getReceivedTime() {
            return receivedTime;
        }

    }
}
