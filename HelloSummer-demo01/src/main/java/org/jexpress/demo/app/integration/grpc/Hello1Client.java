package org.jexpress.demo.app.integration.grpc;


import org.summerboot.jexpress.nio.server.SessionContext;

public interface Hello1Client {
    String bizFunction(String firstName, String lastName, SessionContext context);
}
