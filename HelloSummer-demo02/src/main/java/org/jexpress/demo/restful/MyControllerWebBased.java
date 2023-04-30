package org.jexpress.demo.restful;

import com.google.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.io.File;
import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.NioConfig;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.nio.server.ws.rs.WebResourceController;

@Singleton
@Controller(implTag = "WebBased")// to enable it, start application with -use WebBased or -use RoleBased WebBased to enable both role and web based controllers. 404 error will be responsed as html when extends WebResourceController
public class MyControllerWebBased extends WebResourceController {

    private static final File WELCOME_PAGE1 = new File(NioConfig.cfg.getDocrootDir() + File.separator + "page1.html").getAbsoluteFile();
    private static final File WELCOME_PAGE2 = new File(NioConfig.cfg.getDocrootDir() + File.separator + "page2.html").getAbsoluteFile();

    @GET
    @Path("/")
    public void welcomePage(final ServiceContext context) {
        context.file(WELCOME_PAGE1, false);
    }

    @GET
    @Path("/page2")
    public void welcomePage2(final ServiceContext context) {
        context.file(WELCOME_PAGE2, false);
    }

    @GET
    @Path("/redirect")
    public void redirect(final ServiceContext context) {
        context.redirect("/page2");
    }

    @GET
    @Path("/download/{downloadMode}")
    public void download(@NotNull @PathParam("downloadMode") boolean isDownloadMode, final ServiceContext context) {
        context.file(WELCOME_PAGE1, isDownloadMode);
    }

}
