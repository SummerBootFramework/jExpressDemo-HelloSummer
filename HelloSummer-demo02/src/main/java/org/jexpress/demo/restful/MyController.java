package org.jexpress.demo.restful;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.jexpress.demo.app.MyConfig;
import org.jexpress.demo.restful.vo.AppPOI;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.boot.annotation.Log;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;

@Singleton
@Controller
@Path("/hellosummer2")
public class MyController {

    @GET
    @Path("/hello/{name}")
    @Produces({MediaType.TEXT_PLAIN})
    public String hello(@NotNull @PathParam("name") String myName) {// both Nonnull or NotNull works    
        return "Hello " + myName;
    }

    @POST
    @Path("/account1/{name}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})// require request header Content-Type: application/json or Content-Type: application/xml
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})// require request header Accept: application/json or Accept: application/xml
    public ResponseDto hello_no_validation_unprotected_logging(@PathParam("name") String myName, RequestDto request) {
        return new ResponseDto("secret: " + MyConfig.cfg.getLicenseKey(), "shared");
    }

    /**
     * Three features:
     * <p>
     * 1. auto validate JSON request by @Valid and @NotNull annotation
     * <p>
     * 2. protected user credit card and privacy information from being logged
     * by @Log annotation
     * <p>
     * 3. mark performance POI (point of interest) by using
     * ServiceContext.poi(key), see section#8.3
     *
     * @param myName
     * @param request
     * @param context
     * @return
     */
    @POST
    @Path("/account2/{name}")
    //@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})// require request header Content-Type: application/json or Content-Type: application/xml
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})// require request header Accept: application/json or Accept: application/xml
    @Log(hideJsonStringFields = {"creditCardNumber", "clientPrivacy"}, hideJsonArrayFields = "secretList")
    public ResponseDto hello_auto_validation_protected_logging_markWithPOI(@NotNull @PathParam("name") String myName, @NotNull @Valid RequestDto request, final ServiceContext context) {
        context.poi(AppPOI.DB_BEGIN);// about POI, see section8.3
        // DB access and it takes time ...
        context.poi(AppPOI.DB_END);

        context.poi(AppPOI.GRPC_BEGIN);// about POI, see section8.3
        // gRPC access and it takes time ...
        context.poi(AppPOI.GRPC_END);

        context.status(HttpResponseStatus.CREATED);// override, default is 200 OK
        return new ResponseDto("secret: " + MyConfig.cfg.getLicenseKey(), "shared");
    }

    public static class RequestDto {

        @NotNull
        private String creditCardNumber;

        @Valid
        @NotEmpty
        private List<String> shoppingList;

        public String getCreditCardNumber() {
            return creditCardNumber;
        }

        public void setCreditCardNumber(String creditCardNumber) {
            this.creditCardNumber = creditCardNumber;
        }

        public List<String> getShoppingList() {
            return shoppingList;
        }

        public void setShoppingList(List<String> shoppingList) {
            this.shoppingList = shoppingList;
        }

    }

    public static class ResponseDto {

        private final String clientPrivacy;
        private final String clientNonPrivacy;
        private final List<String> secretList = List.of("aa", "bb");

        public ResponseDto(String clientPrivacy, String clientNonPrivacy) {
            this.clientPrivacy = clientPrivacy;
            this.clientNonPrivacy = clientNonPrivacy;
        }

        public String getClientPrivacy() {
            return clientPrivacy;
        }

        public String getClientNonPrivacy() {
            return clientNonPrivacy;
        }

        public List<String> getSecretList() {
            return secretList;
        }

    }
}
