package org.jexpress.demo.app.integration.grpc;

import org.summerboot.jexpress.boot.config.annotation.ImportResource;
import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;


@ImportResource(value = "cfg_grpcclient1a.properties")
public class Hello1ClientConfig extends GRPCClientConfig {

    public static void main(String... args) {
        String t = generateTemplate(Hello1ClientConfig.class);
        System.out.println(t);
    }

    public static final Hello1ClientConfig cfg = new Hello1ClientConfig();

    private Hello1ClientConfig() {
    }
}
