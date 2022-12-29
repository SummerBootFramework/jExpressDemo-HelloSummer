package org.jexpress.demo.restful.auth;

import com.google.inject.Singleton;
import java.io.IOException;
import java.util.List;
import javax.naming.NamingException;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.security.auth.Authenticator;
import org.summerboot.jexpress.security.auth.AuthenticatorListener;
import org.summerboot.jexpress.security.auth.BootAuthenticator;
import org.summerboot.jexpress.security.auth.Caller;
import org.summerboot.jexpress.security.auth.User;

@Singleton
@Service(binding = Authenticator.class)
public class AuthenticatorImpl extends BootAuthenticator {

    @Override
    protected Caller login(String uid, String password, AuthenticatorListener listener) throws IOException, NamingException {
        long tenantId = 1;
        String tenantName = "123 Inc.";
        long userId = 12;
        User user = new User(tenantId, tenantName, userId, uid);
        user.addGroup("AdminGroup");
        user.addGroup("UserGroup");
        return user;
    }

    @Override
    public List ping(Object... param) {
        return null;
    }

}
