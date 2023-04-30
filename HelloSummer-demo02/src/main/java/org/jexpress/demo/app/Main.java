package org.jexpress.demo.app;

import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Version;

@Version({"Hello Summer v1.0.0", SummerApplication.VERSION})
public class Main {

    public static void main(String... args) {
        SummerApplication.run();
    }
}
