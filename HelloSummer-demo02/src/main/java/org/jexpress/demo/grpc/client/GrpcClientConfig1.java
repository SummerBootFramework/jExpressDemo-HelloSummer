package org.jexpress.demo.grpc.client;

import org.summerboot.jexpress.boot.config.annotation.ImportResource;
import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;


@ImportResource("cfg_grpcclient1.properties")
public class GrpcClientConfig1 extends GRPCClientConfig {

    public static void main(String... args) {
        String t = generateTemplate(GrpcClientConfig1.class);
        System.out.println(t);
    }

    public static final GrpcClientConfig1 cfg = new GrpcClientConfig1();

    private GrpcClientConfig1() {
    }
}
