package org.jexpress.demo.app;

import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Version;

@Version(value = "Hello Summer v2.0.0", logFileName = "hellosummerv2")
public class Main {

    public static void main(String... args) {
        SummerApplication.run();
//        SummerApplication app = SummerApplication.run();
//        app.shutdown();
//        app.start();
    }
}
