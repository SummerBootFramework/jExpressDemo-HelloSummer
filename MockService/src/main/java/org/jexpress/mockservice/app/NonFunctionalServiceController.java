package org.jexpress.mockservice.app;

import com.google.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.summerboot.jexpress.boot.annotation.Controller;
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
}
