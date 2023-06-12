package org.jexpress.demo.grpc;

import io.grpc.ServerInterceptor;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.nio.grpc.BootServerInterceptor;
import org.summerboot.jexpress.security.auth.Caller;
import org.summerboot.jexpress.security.auth.User;


@Service(binding = ServerInterceptor.class)
public class AuthorizationServerInterceptor extends BootServerInterceptor {

    @Override
    protected Caller buildCaller(String jwt) {
        User user = new User(888L, "mockuser");
        return user;
    }

}
