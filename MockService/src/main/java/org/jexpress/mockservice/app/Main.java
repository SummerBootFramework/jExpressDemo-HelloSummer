package org.jexpress.mockservice.app;

import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Version;

/**
 * @author 魏泽北
 */
@Version(value = "MockService1.0.5")
public class Main {

    public static void main(String[] args) {
        SummerApplication.run();
        
        System.out.println("Mock JWT: " + Utils.generateJWT("mock.uid", "mock.role", 0));
    }
}
