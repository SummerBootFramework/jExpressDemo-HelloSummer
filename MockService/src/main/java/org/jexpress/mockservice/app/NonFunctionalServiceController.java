package org.jexpress.mockservice.app;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.nio.server.ws.rs.BootController;

/**
 *
 * @author 魏泽北
 */
@Singleton
@Controller
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("/jexpress/mockservice")
public class NonFunctionalServiceController extends BootController {

    private static final String USER_INPUT_VALIDATION_REGEX = "[a-zA-Z\\.\\-' ]{1,10}";

    @POST
    @Path("/jwt/{roleName}/{id}/{issuer}/{subject}/{ttlMinutes}")
    public String generateJWT(@PathParam("roleName") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String roleName,
            @PathParam("id") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String id,
            @PathParam("issuer") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String issuer,
            @PathParam("subject") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String subject,
            @PathParam("ttlMinutes") int ttlMinutes,
            @Parameter(hidden = true) final ServiceContext context) {
        context.status(HttpResponseStatus.CREATED);
        return Utils.generateJWT(roleName, id, issuer, subject, ttlMinutes);
    }

}
