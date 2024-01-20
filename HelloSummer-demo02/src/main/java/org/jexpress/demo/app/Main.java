package org.jexpress.demo.app;

import java.io.IOException;
import java.net.URISyntaxException;
import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Version;

@Version(value = {"Hello Summer v2.0.0", "jex2.3.9"}, logFileName = "hellosummerv2")
public class Main {

    public static void main(String... args) throws URISyntaxException, IOException {
        SummerApplication.run();
//        SummerApplication app = SummerApplication.run();
//        app.shutdown();
//        app.start();
    }
}
