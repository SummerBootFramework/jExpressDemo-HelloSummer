package org.jexpress.demo.app.integration.grpc;

import com.google.inject.Singleton;
import org.jexpress.demo.grpc.Hello1Service_BizClient;
import org.summerboot.jexpress.boot.annotation.Inspector;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;

@Singleton
@Service
@Inspector
public class Hello1ClientImpl extends Hello1Service_BizClient implements Hello1Client {


    @Override
    protected GRPCClientConfig getGRPCClientConfig() {
        return Hello1ClientConfig.cfg;
    }
}
