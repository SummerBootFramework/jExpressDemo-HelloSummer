package org.jexpress.mockservice.app;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.boot.annotation.Log;
import org.summerboot.jexpress.nio.server.SessionContext;
import org.summerboot.jexpress.nio.server.domain.ServiceError;
import org.summerboot.jexpress.nio.server.ws.rs.BootController;
import org.summerboot.jexpress.security.auth.Caller;

/**
 * @author 魏泽北
 */
@Singleton
@Controller
//@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("/mockservice")
public class NonFunctionalServiceController extends BootController {

    private static final String USER_INPUT_VALIDATION_REGEX = "[a-zA-Z\\.\\-' ]{1,10}";

    private static final String X_AUTH_TOKEN = "X-AuthToken";

    // curl -v -k https://localhost:8211/mockservice/jwt/10 -X POST -H "application/x-www-form-urlencoded; charset=UTF-8" -X POST -d "id=myid&issuer=myissuer&subject=mysubject&audience=myaudience"
    @POST
    @Path("/jwt/{ttlMinutes}")
    @Log(requestBody = false, responseHeader = false)
    @Operation(
            tags = {"Mock Service"},
            summary = "Generate mock JWT",
            description = "Generate mock JWT with user inputs",
            responses = {
                    @ApiResponse(responseCode = "201", description = "success and return JWT token in header " + X_AUTH_TOKEN,
                            headers = {
                                    @Header(name = X_AUTH_TOKEN, schema = @Schema(type = "string"), description = "Generated JWT")
                            },
                            content = @Content(schema = @Schema(implementation = Caller.class))
                    ),
                    @ApiResponse(responseCode = "4XX", description = "A fault has taken place on client side. Client should not retransmit the same request again, but fix the error first.",
                            content = @Content(schema = @Schema(implementation = ServiceError.class))
                    ),
                    @ApiResponse(responseCode = "5XX", description = "Something happened on the server side. The client can continue and try again with the request without modification.",
                            content = @Content(schema = @Schema(implementation = ServiceError.class))
                    )
            }
    )
    public void generateJWT(@PathParam("ttlMinutes") int ttlMinutes,
                            @FormParam("id") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String id,
                            @FormParam("issuer") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String issuer,
                            @FormParam("subject") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String subject,
                            @FormParam("audience") @Pattern(regexp = USER_INPUT_VALIDATION_REGEX) String audience,
                            @Parameter(hidden = true) final SessionContext context) {
        String jwt = Utils.generateJWT(id, issuer, subject, audience, ttlMinutes);
        context.responseHeader(X_AUTH_TOKEN, jwt).status(HttpResponseStatus.CREATED);
    }

}
