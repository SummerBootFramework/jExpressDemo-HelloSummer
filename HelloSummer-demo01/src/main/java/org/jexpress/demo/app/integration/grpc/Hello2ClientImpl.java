package org.jexpress.demo.app.integration.grpc;

import com.google.inject.Singleton;
import org.jexpress.demo.grpc.Hello2Service_BizClient;
import org.summerboot.jexpress.boot.annotation.Inspector;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;

@Singleton
@Service
@Inspector
public class Hello2ClientImpl extends Hello2Service_BizClient implements Hello2Client {


    @Override
    protected GRPCClientConfig getGRPCClientConfig() {
        return Hello2ClientConfig.cfg;
    }
}
