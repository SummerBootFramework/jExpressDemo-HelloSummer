package org.jexpress.demo.grpc.client;

import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;

//@ImportResource("cfg_grpcclient2.properties")
public class GrpcClientConfig2 extends GRPCClientConfig {

    public static void main(String... args) {
        String t = generateTemplate(GrpcClientConfig2.class);
        System.out.println(t);
    }

    public static final GrpcClientConfig2 cfg = new GrpcClientConfig2();

    private GrpcClientConfig2() {
    }
}
