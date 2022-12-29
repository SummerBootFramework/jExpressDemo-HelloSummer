/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package test.grpc;

import com.google.inject.Module;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.jexpress.demo.app.Main;
import org.jexpress.demo.grpc.HelloClientConfig;
import org.jexpress.demo.grpc.client.Hello1Client;
import org.jexpress.demo.grpc.client.Hello2Client;
import org.summerboot.jexpress.boot.SummerApplication;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author DuXiao
 */
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

    @Test
    public void testConnect() throws URISyntaxException, IOException {
        Module userOverrideModule = null;

        gRPCTest(userOverrideModule, "-cfgdir run/env_qa/configuration_nossl");
        gRPCTest(userOverrideModule, "-cfgdir run/env_qa/configuration_ssl_trustall");
    }

    private void gRPCTest(Module userOverrideModule, String args) throws IOException {
        SummerApplication app = SummerApplication.run(Main.class, userOverrideModule, args);
        HelloClientConfig clientcfg = HelloClientConfig.cfg;
        URI uri = clientcfg.getUri();
        KeyManagerFactory keyManagerFactory = clientcfg.getKmf();
        TrustManagerFactory trustManagerFactory = clientcfg.getTmf();
        String overrideAuthority = clientcfg.getOverrideAuthority();
        Iterable<String> ciphers = clientcfg.getCiphers();
        String[] tlsVersionProtocols = clientcfg.getSslProtocols();
        Hello1Client c1 = new Hello1Client(uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
        Hello2Client c2 = new Hello2Client(uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
        String firstName = "John";
        String lastName = "Doe";
        c1.connect();
        c2.connect();
        try {
            String g1 = c1.hello(firstName, lastName);
            System.out.println("g1=" + g1);
            assertEquals(g1, "Hello1 John Doe");
            String g2 = c2.hello(firstName, lastName);
            System.out.println("g2=" + g2);
            assertEquals(g2, "Hello2 John Doe");
        } finally {
            c1.disconnect();
            c2.disconnect();
            app.stop();
        }
    }
}
