package org.jexpress.demo.app.instrumentation;


import org.summerboot.jexpress.boot.annotation.Inspector;
import org.summerboot.jexpress.boot.instrumentation.HealthInspector;
import org.summerboot.jexpress.nio.server.domain.Err;

import java.util.List;


@Inspector
public class HealthChecker implements HealthInspector<Object> {

    public static int a = 0;

    /**
     * @param param
     * @return
     */
    @Override
    public List<Err> ping(Object... param) {
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        List<Err> ret = null;
        switch (a) {
            case 1 -> {
                ret = List.of(new Err(1, "test 1", null, null));
            }
            case 2 -> {
                ret = List.of(new Err(2, "test 2a", null, null));
            }
        }
        return ret;
    }
}
