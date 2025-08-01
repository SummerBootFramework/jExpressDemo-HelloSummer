package org.jexpress.demo.restful.service;

import com.google.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.SessionContext;
import org.summerboot.jexpress.nio.server.ws.rs.WebResourceController;

import java.io.File;

@Singleton
@Controller(AlternativeName = "WebBased")
// to enable it, start application with -use WebBased or -use RoleBased WebBased to enable both role and web based controllers. 404 error will be responsed as html when extends WebResourceController
public class MyControllerWebBased extends WebResourceController {

    private static final String WELCOME_PAGE1 = "pages" + File.separator + "page1.html";
    private static final String WELCOME_PAGE2 = "pages" + File.separator + "page2.html";

    @GET
    @Path("/")
    public void welcomePage(final SessionContext context) {
        context.response(WELCOME_PAGE1, false);
    }

    @GET
    @Path("/page2")
    public void welcomePage2(final SessionContext context) {
        context.response(WELCOME_PAGE2, false);
    }

    @GET
    @Path("/redirect")
    public void redirect(final SessionContext context) {
        context.redirect("/page2");
    }

    @GET
    @Path("/download/{downloadMode}")
    public void download(@NotNull @PathParam("downloadMode") boolean isDownloadMode, final SessionContext context) {
        context.response(WELCOME_PAGE1, isDownloadMode);
    }

    @GET
    @Path("/download")
    public void download2(final SessionContext context) {
        context.response(new File("../tree.txt"), false);
    }

}
