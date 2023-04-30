package org.jexpress.demo.grpc.client;

import org.summerboot.jexpress.boot.config.annotation.ImportResource;
import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;

@ImportResource("cfg_grpcclient.properties")
public class HelloClientConfig extends GRPCClientConfig {

    public static void main(String... args) {
        String t = generateTemplate(HelloClientConfig.class);
        System.out.println(t);
    }

    public static final HelloClientConfig cfg = new HelloClientConfig();

    private HelloClientConfig() {
    }
}
