package test.domain;

import com.google.inject.Injector;
import org.jexpress.demo.app.Main;
import org.summerboot.jexpress.boot.SummerApplication;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author DuXiao
 */
public class DomainTest {

    public DomainTest() {
    }

    private static SummerApplication app;
    private static Injector injector;

    @BeforeClass
    public static void setUpClass() throws Exception {
        app = SummerApplication.unittest(Main.class, null, "-cfgdir run/standalone_dev/configuration");
        injector = app.getGuiceInjector();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        app.shutdown();
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    @Test
    public void testInit() {
        System.out.println("aaa");
    }
}
