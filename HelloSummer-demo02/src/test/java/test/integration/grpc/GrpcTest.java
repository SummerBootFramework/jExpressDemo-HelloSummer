package test.integration.grpc;

import com.google.inject.Injector;
import com.google.inject.Module;
import io.grpc.BindableService;
import io.grpc.NameResolverProvider;
import io.grpc.NameResolverRegistry;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jexpress.demo.app.Main;
import org.jexpress.demo.grpc.client.Hello1ClientConfig;
import org.jexpress.demo.grpc.client.Hello1ClientImpl;
import org.jexpress.demo.grpc.client.Hello2ClientImpl;
import org.jexpress.demo.grpc.impl.Hello1ServiceImpl_A;
import org.jexpress.demo.grpc.impl.Hello2ServiceImpl_A;
import org.summerboot.jexpress.boot.BootConstant;
import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.config.BootConfig;
import org.summerboot.jexpress.nio.grpc.BootLoadBalancerProvider;
import org.summerboot.jexpress.nio.grpc.GRPCClientConfig;
import org.summerboot.jexpress.nio.grpc.GRPCServer;
import org.summerboot.jexpress.security.EncryptorUtil;
import org.summerboot.jexpress.security.SSLUtil;
import org.summerboot.jexpress.security.auth.LDAPAuthenticator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GrpcTest {

    public GrpcTest() {
    }

    private static SummerApplication app;
    private static Injector injector;

    @BeforeClass
    public static void setUpClass() throws Exception {
        EncryptorUtil.setMasterPassword("changeit");
        //app = SummerApplication.unittest(Main.class, null, "-cfgdir run/standalone_dev/configuration");
        //injector = app.getGuiceInjector();
        //AuthConfig.cfg.load(new File("run/standalone_dev/configuration/cfg_auth.properties"), true);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /*
        \run>java -jar hellosummer-2.0.jar -cfgdir standalone_dev/configuration_two-way_8424
        \run>java -jar hellosummer-2.0.jar -cfgdir standalone_dev/configuration_two-way_8425 -use hawaii_1 hawaii_2
     */
    //@Test
    public void testClientSideLoadBalancer() throws IOException {
        String firstName = "John";
        String lastName = "Doe";
        String impleKey2Verify1 = "Hello";

//        NameResolverProvider nameResolverProvider = new BootLoadBalancerProvider("grpcs",
//                new InetSocketAddress("192.168.1.11", 8882),
//                new InetSocketAddress("127.0.0.1", 8881)
//        );
//        NameResolverRegistry.getDefaultRegistry().register(nameResolverProvider);
        Hello1ClientConfig clientcfg = Hello1ClientConfig.cfg;
        clientcfg.load(new File("run/standalone_dev/configuration_two-way_8424/cfg_grpcclient.properties"), true);
        NameResolverProvider nameResolverProvider = clientcfg.getNameResolverProvider();
        URI uri = clientcfg.getUri();
        KeyManagerFactory keyManagerFactory = clientcfg.getKmf();
        TrustManagerFactory trustManagerFactory = clientcfg.getTmf();
        String overrideAuthority = clientcfg.getOverrideAuthority();
        Iterable<String> ciphers = clientcfg.getCiphers();
        String[] tlsVersionProtocols = clientcfg.getSslProtocols();
        System.out.println("\n\t client: " + clientcfg);
        System.out.println("\n\t client.nameResolverProvider: " + nameResolverProvider);
        System.out.println("\n\t client.uri: " + uri);
        System.out.println("\n\t client.servers: " + clientcfg.getLoadBalancingServers());
        System.out.println("\n\n");
        NettyChannelBuilder cb1 = Hello1ClientConfig.initNettyChannelBuilder(nameResolverProvider, GRPCClientConfig.LoadBalancingPolicy.ROUND_ROBIN, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);

        Hello1ClientImpl c1 = new Hello1ClientImpl().withNettyChannelBuilder(cb1);
        try {
            do {
                try {
                    c1.connect();
                    String response = c1.hello(firstName, lastName);
                    System.out.println("response=" + response);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                } finally {
                    c1.disconnect();
                }
                //assertEquals(response, impleKey2Verify1 + "1 John Doe");
            } while (true);
        } finally {
            //app1.stop();
            //app2.stop();
        }

//        impleKey2Verify1 = "Aloha";
//        try {
//            String g1 = c1.hello(firstName, lastName);
//            System.out.println("g1=" + g1);
//            assertEquals(g1, impleKey2Verify1 + "1 John Doe");
//        } finally {
//            c1.disconnect();
//        }
//        app1.stop();
//        app2.stop();
    }

    @Test
    public void testConnect() throws URISyntaxException, IOException {
        String configDir = "run/standalone_dev/configuration";
        gRPCTest("-cfgdir " + configDir, "Hello", "Hello");
        gRPCTest("-cfgdir " + configDir + " -use hawaii_1 hawaii_2", "Aloha", "Aloha");
        gRPCTest("-cfgdir " + configDir, "Hello", "Hello");
        gRPCTest("-cfgdir " + configDir + " -use hawaii_2", "Hello", "Aloha");
        gRPCTest("-cfgdir " + configDir + " -use hawaii_1", "Aloha", "Hello");
    }

    private void gRPCTest(String serverArgs, String impleKey2Verify1, String impleKey2Verify2) throws IOException {
        // 1. start server
        Module userOverrideModule = null;
        app = SummerApplication.run(Main.class, userOverrideModule, serverArgs);
        System.out.println("\n\t server started: " + serverArgs);

        // 2. config client
        Hello1ClientConfig clientcfg = Hello1ClientConfig.cfg;
        NameResolverProvider nameResolverProvider = clientcfg.getNameResolverProvider();
        URI uri = clientcfg.getUri();
        System.out.println("\n\t client: " + clientcfg);
        System.out.println("\n\t client.nameResolverProvider: " + nameResolverProvider);
        System.out.println("\n\t client.uri: " + uri);
        System.out.println("\n\t client.servers: " + clientcfg.getLoadBalancingServers());
        System.out.println("\n\n");
        KeyManagerFactory keyManagerFactory = clientcfg.getKmf();
        TrustManagerFactory trustManagerFactory = clientcfg.getTmf();
        String overrideAuthority = clientcfg.getOverrideAuthority();
        Iterable<String> ciphers = clientcfg.getCiphers();
        String[] tlsVersionProtocols = clientcfg.getSslProtocols();
        NettyChannelBuilder cb1 = Hello1ClientConfig.initNettyChannelBuilder(nameResolverProvider, GRPCClientConfig.LoadBalancingPolicy.ROUND_ROBIN, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);

        // 3. create client
        Hello1ClientImpl c1 = new Hello1ClientImpl().withNettyChannelBuilder(cb1);
        Hello2ClientImpl c2 = new Hello2ClientImpl().withNettyChannelBuilder(cb1);
        String firstName = "John";
        String lastName = "Doe";
        c1.connect();
        c2.connect();

        // 4. call service
        try {
            String g1 = c1.hello(firstName, lastName);
            System.out.println("g1=" + g1);
            assertEquals(g1, BootConstant.APP_ID + " " + impleKey2Verify1 + "1 John Doe");
            String g2 = c2.hello(firstName, lastName);
            System.out.println("g2=" + g2);
            assertEquals(g2, BootConstant.APP_ID + " " + impleKey2Verify2 + "2 John Doe");
        } finally {
            c1.disconnect();
            c2.disconnect();
            app.shutdown();
        }
    }

    @Test
    public void test2WayAuth() throws GeneralSecurityException, IOException {
        test2WayAuth_step1_config(2048);
        test2WayAuth_step1_config(4096);
    }

    private void test2WayAuth_step1_config(int length) throws GeneralSecurityException, IOException {
        String configDir = "src/test/resources/config/"; // "run/standalone_dev/configuration/";
        String keyStore = new File(configDir + "keystore.p12").getAbsolutePath();
        String serverTrustStore = new File(configDir + "truststore_grpc_server.p12").getAbsolutePath();
        String clientTrustStore = new File(configDir + "truststore_grpc_client.p12").getAbsolutePath();
        String emptyTrustStore = new File(configDir + "truststore_empty.p12").getAbsolutePath();
        String serverKeyAlias = "server2_" + length + ".jexpress.org";
        String clientKeyAlias = "server3_" + length + ".jexpress.org";
        String overrideAuthority = "server2." + length + ".jexpress.org";


        String keyStorePassword = "changeit";
        String keyPassword = "changeit";
        String trustStorePassword = "changeit";
        // server
        KeyManagerFactory kmfServer = SSLUtil.buildKeyManagerFactory(keyStore, keyStorePassword.toCharArray(), serverKeyAlias, keyPassword.toCharArray());
        TrustManagerFactory tmfServer = SSLUtil.buildTrustManagerFactory(serverTrustStore, trustStorePassword.toCharArray());
        // client
        KeyManagerFactory kmfClient = SSLUtil.buildKeyManagerFactory(keyStore, keyStorePassword.toCharArray(), clientKeyAlias, keyPassword.toCharArray());
        TrustManagerFactory tmfClient = SSLUtil.buildTrustManagerFactory(clientTrustStore, trustStorePassword.toCharArray());
        // test: 2-way TLS
        test2WayAuth_step2_runServer(kmfServer, tmfServer, kmfClient, tmfClient, overrideAuthority);
        // test: client verify server certificate, server trust all client certificate
        test2WayAuth_step2_runServer(kmfServer, null, kmfClient, tmfClient, overrideAuthority);
        // test: server verify server certificate, client trust all client certificate
        test2WayAuth_step2_runServer(kmfServer, tmfServer, kmfClient, null, overrideAuthority);
        // test: server and client trust all certificates
        test2WayAuth_step2_runServer(kmfServer, null, kmfClient, null, overrideAuthority);


        // empty trust store
        TrustManagerFactory tmfEmpty = SSLUtil.buildTrustManagerFactory(emptyTrustStore, trustStorePassword.toCharArray());
        // test: server does NOT trust client certificate, client trust server certificate
        try {
            test2WayAuth_step2_runServer(kmfServer, tmfEmpty, kmfClient, tmfClient, overrideAuthority);
        } catch (StatusRuntimeException ex) {
            System.out.println("server does NOT trust client certificate - Expected exception: " + ex);
            assertEquals(ex.getMessage(), "UNAVAILABLE: ssl exception");
        }
        // test: client does NOT trust server certificate, server trust server certificate
        try {
            test2WayAuth_step2_runServer(kmfServer, tmfServer, kmfClient, tmfEmpty, overrideAuthority);
        } catch (StatusRuntimeException ex) {
            System.out.println("client does NOT trust server certificate - Expected exception: " + ex);
            String receivedMessage = ex.getMessage();
            assertTrue(receivedMessage.startsWith("UNAVAILABLE: io exception"));
            assertEquals(receivedMessage, "UNAVAILABLE: io exception\nChannel Pipeline: [SslHandler#0, ProtocolNegotiators$ClientTlsHandler#0, WriteBufferingAndExceptionHandler#0, DefaultChannelPipeline$TailContext#0]");
        }
    }

    private void test2WayAuth_step2_runServer(KeyManagerFactory kmfServer, TrustManagerFactory tmfServer, KeyManagerFactory kmfClient, TrustManagerFactory tmfClient, String overrideAuthority) throws IOException {
        // 1. config server
        String host = "localhost";
        int port = 8425;
        ThreadPoolExecutor tpe = BootConfig.buildThreadPoolExecutor("");
        ServerInterceptor serverInterceptor = new LDAPAuthenticator();
        GRPCServer gRPCServer = new GRPCServer(host, port, kmfServer, tmfServer, tpe, true, false, null, serverInterceptor);
        BindableService impl1 = new Hello1ServiceImpl_A();
        BindableService impl2 = new Hello2ServiceImpl_A();

        ServerBuilder serverBuilder = gRPCServer.getServerBuilder();
        serverBuilder.addService(impl1);
        serverBuilder.addService(impl2);

        StringBuilder startingMemo = new StringBuilder();
        gRPCServer.start(startingMemo);

        try {
            // 2. config client
            String loadBalancingTargetScheme = "grpc";
            int priority = 0;
            List<InetSocketAddress> loadBalancingServers = List.of(new InetSocketAddress(host, port));
            NameResolverProvider nameResolverProvider = new BootLoadBalancerProvider(loadBalancingTargetScheme, ++priority, loadBalancingServers);
            // register
            NameResolverRegistry nameResolverRegistry = NameResolverRegistry.getDefaultRegistry();
            nameResolverRegistry.register(nameResolverProvider);
            // init
            String policy = GRPCClientConfig.LoadBalancingPolicy.PICK_FIRST.getValue();
            String target = nameResolverProvider.getDefaultScheme() + ":///";
            NettyChannelBuilder channelBuilder = NettyChannelBuilder.forTarget(target).defaultLoadBalancingPolicy(policy);
            // set SSL
            final SslContextBuilder sslBuilder = GrpcSslContexts.forClient();
            sslBuilder.keyManager(kmfClient);
            //
            if (tmfClient == null) {//ignore Server Certificate
                sslBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            } else {
                sslBuilder.trustManager(tmfClient);
                if (overrideAuthority != null) {
                    channelBuilder.overrideAuthority(overrideAuthority);
                }
            }
            GrpcSslContexts.configure(sslBuilder, SslProvider.OPENSSL);
            String[] tlsVersionProtocols = {"TLSv1.3"};//{"TLSv1.2", "TLSv1.3"};
            if (tlsVersionProtocols != null) {
                sslBuilder.protocols(tlsVersionProtocols);
            }
            SslContext sslContext = sslBuilder.build();
            channelBuilder.sslContext(sslContext).useTransportSecurity();

            test2WayAuth_step3_runClient(channelBuilder);
        } finally {
            gRPCServer.shutdown();
        }

    }

    private void test2WayAuth_step3_runClient(NettyChannelBuilder channelBuilder) {
        // connect to server
        Hello1ClientImpl client1 = new Hello1ClientImpl().withNettyChannelBuilder(channelBuilder).connect();
        Hello2ClientImpl client2 = new Hello2ClientImpl().withNettyChannelBuilder(channelBuilder).connect();
        try {
            String g1 = client1.hello("firstName", "lastName");
            System.out.println("g1=" + g1);
            assertEquals(g1, BootConstant.APP_ID + " Hello1 " + "firstName lastName");
            String g2 = client2.hello("firstName", "lastName");
            System.out.println("g2=" + g2);
            assertEquals(g2, BootConstant.APP_ID + " Hello2 " + "firstName lastName");
        } finally {
            client1.disconnect();
            client2.disconnect();
        }
    }
}
