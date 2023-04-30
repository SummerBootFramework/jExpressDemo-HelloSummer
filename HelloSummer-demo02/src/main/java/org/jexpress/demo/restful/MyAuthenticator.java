package org.jexpress.demo.restful;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpHeaders;
import javax.naming.NamingException;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.nio.server.RequestProcessor;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.security.auth.Authenticator;
import org.summerboot.jexpress.security.auth.AuthenticatorListener;
import org.summerboot.jexpress.security.auth.BootAuthenticator;
import org.summerboot.jexpress.security.auth.Caller;
import org.summerboot.jexpress.security.auth.User;

@Singleton
@Service(binding = Authenticator.class)
public class MyAuthenticator extends BootAuthenticator<Long> {

    @Override
    protected Caller authenticate(String usename, String password, Long metaData, AuthenticatorListener listener, ServiceContext context) throws NamingException {
        // verify username and password against LDAP
        if ("wrongpwd".equals(password)) {
            return null;
        }
        // build a caller to return
        long tenantId = 1;
        String tenantName = "jExpress Org";
        long userId = 456;
        User user = new User(tenantId, tenantName, userId, usename);
        user.addGroup("AdminGroup");
        user.addGroup("EmployeeGroup");
        return user;
    }

    @Override
    public boolean customizedAuthorizationCheck(RequestProcessor processor, HttpHeaders httpRequestHeaders, String httpRequestPath, ServiceContext context) throws Exception {
        return true;
    }

}
