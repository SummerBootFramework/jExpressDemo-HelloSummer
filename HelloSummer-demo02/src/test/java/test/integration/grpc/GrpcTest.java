package test.integration.grpc;

import com.google.inject.Injector;
import com.google.inject.Module;
import io.grpc.NameResolverProvider;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.jexpress.demo.app.Main;
import org.jexpress.demo.grpc.client.HelloClientConfig;
import org.jexpress.demo.grpc.client.Hello1Client;
import org.jexpress.demo.grpc.client.Hello2Client;
import org.summerboot.jexpress.boot.SummerApplication;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrpcTest {

    public GrpcTest() {
    }

    private static SummerApplication app;
    private static Injector injector;

    @BeforeClass
    public static void setUpClass() throws Exception {
        //app = SummerApplication.unittest(Main.class, null, "-cfgdir run/standalone_dev/configuration");
        //injector = app.getGuiceInjector();
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
        HelloClientConfig clientcfg = HelloClientConfig.cfg;
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
        Hello1Client c1 = new Hello1Client(nameResolverProvider, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
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
        gRPCTest("-cfgdir run/standalone_dev/configuration_two-way_8424", "Hello", "Hello");
        gRPCTest("-cfgdir run/standalone_dev/configuration_two-way_8424 -use hawaii_1 hawaii_2", "Aloha", "Aloha");
        gRPCTest("-cfgdir run/standalone_dev/configuration_two-way_8424", "Hello", "Hello");
        gRPCTest("-cfgdir run/standalone_dev/configuration_two-way_8424 -use hawaii_2", "Hello", "Aloha");
        gRPCTest("-cfgdir run/standalone_dev/configuration_two-way_8424 -use hawaii_1", "Aloha", "Hello");
    }

    private void gRPCTest(String serverArgs, String impleKey2Verify1, String impleKey2Verify2) throws IOException {
        Module userOverrideModule = null;
        app = SummerApplication.run(Main.class, userOverrideModule, serverArgs);
        System.out.println("\n\t server started: " + serverArgs);
        HelloClientConfig clientcfg = HelloClientConfig.cfg;
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
        Hello1Client c1 = new Hello1Client(nameResolverProvider, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
        Hello2Client c2 = new Hello2Client(nameResolverProvider, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
        String firstName = "John";
        String lastName = "Doe";
        c1.connect();
        c2.connect();
        try {
            String g1 = c1.hello(firstName, lastName);
            System.out.println("g1=" + g1);
            assertEquals(g1, impleKey2Verify1 + "1 John Doe");
            String g2 = c2.hello(firstName, lastName);
            System.out.println("g2=" + g2);
            assertEquals(g2, impleKey2Verify2 + "2 John Doe");
        } finally {
            c1.disconnect();
            c2.disconnect();
            app.shutdown();
        }
    }
}
