package org.jexpress.demo.app;

import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Version;

@Version(value = {"Hello Summer v2.0.0", "jex32.3.6"}, logFileName="hellosummerv2")
public class Main {

    public static void main(String... args) {
        SummerApplication.run();
    }
}
