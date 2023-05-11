package test.grpc;

import com.google.inject.Module;
import io.grpc.NameResolverProvider;
import io.grpc.NameResolverRegistry;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.jexpress.demo.app.Main;
import org.jexpress.demo.grpc.client.HelloClientConfig;
import org.jexpress.demo.grpc.client.Hello1Client;
import org.jexpress.demo.grpc.client.Hello2Client;
import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.nio.grpc.BootLoadBalancerProvider;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrpcTest {

    public GrpcTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @BeforeClass
    public static void setUpClass() throws Exception {
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

    //@Test
    public void testClientSideLoadBalancer() throws IOException {
        /*
        \run>java -jar hellosummer-2.0.jar -cfgdir standalone_qa/configuration_ssl_trustall_8881
        \run>java -jar hellosummer-2.0.jar -cfgdir standalone_qa/configuration_ssl_trustall_8882 -use hawaii_1 hawaii_2
        
        */
        String firstName = "John";
        String lastName = "Doe";
        String impleKey2Verify1 = "Hello";

//        NameResolverProvider nameResolverProvider = new BootLoadBalancerProvider("grpcs",
//                new InetSocketAddress("192.168.1.11", 8882),
//                new InetSocketAddress("127.0.0.1", 8881)
//        );
//        NameResolverRegistry.getDefaultRegistry().register(nameResolverProvider);

        HelloClientConfig clientcfg = HelloClientConfig.cfg;
        clientcfg.load(new File("run/standalone_qa/configuration_ssl_trustall_8881/cfg_grpcclient.properties"), true);
        NameResolverProvider nameResolverProvider = clientcfg.getNameResolverProvider();
        URI uri = clientcfg.getUri();
        KeyManagerFactory keyManagerFactory = clientcfg.getKmf();
        TrustManagerFactory trustManagerFactory = clientcfg.getTmf();
        String overrideAuthority = clientcfg.getOverrideAuthority();
        Iterable<String> ciphers = clientcfg.getCiphers();
        String[] tlsVersionProtocols = clientcfg.getSslProtocols();
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
        Module userOverrideModule = null;

        gRPCTest(userOverrideModule, "-cfgdir run/standalone_qa/configuration_nossl", "Hello", "Hello");
        gRPCTest(userOverrideModule, "-cfgdir run/standalone_qa/configuration_nossl -use hawaii_1 hawaii_2", "Aloha", "Aloha");
        gRPCTest(userOverrideModule, "-cfgdir run/standalone_qa/configuration_ssl_trustall_8881", "Hello", "Hello");
        gRPCTest(userOverrideModule, "-cfgdir run/standalone_qa/configuration_ssl_trustall_8881 -use hawaii_2", "Hello", "Aloha");
        gRPCTest(userOverrideModule, "-cfgdir run/standalone_qa/configuration_ssl_trustall_8881 -use hawaii_1", "Aloha", "Hello");
    }

    private void gRPCTest(Module userOverrideModule, String args, String impleKey2Verify1, String impleKey2Verify2) throws IOException {
        SummerApplication app = SummerApplication.run(Main.class, userOverrideModule, args);
        HelloClientConfig clientcfg = HelloClientConfig.cfg;
        NameResolverProvider nameResolverProvider = clientcfg.getNameResolverProvider();
        URI uri = clientcfg.getUri();
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
            app.stop();
        }
    }
}
