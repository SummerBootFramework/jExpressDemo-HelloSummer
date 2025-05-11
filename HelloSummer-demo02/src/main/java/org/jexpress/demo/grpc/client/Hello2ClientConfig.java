package org.jexpress.demo.grpc.client;

import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;

//@ImportResource("cfg_grpcclient2.properties")
public class Hello2ClientConfig extends GRPCClientConfig {

    public static void main(String... args) {
        String t = generateTemplate(Hello2ClientConfig.class);
        System.out.println(t);
    }

    public static final Hello2ClientConfig cfg = new Hello2ClientConfig();

    private Hello2ClientConfig() {
    }
}
