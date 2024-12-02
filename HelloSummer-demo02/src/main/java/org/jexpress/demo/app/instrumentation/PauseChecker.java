package org.jexpress.demo.app.instrumentation;


import org.summerboot.jexpress.boot.annotation.Inspector;
import org.summerboot.jexpress.boot.instrumentation.HealthInspector;
import org.summerboot.jexpress.nio.server.domain.Err;

import java.util.List;


@Inspector
public class PauseChecker implements HealthInspector<Object> {

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
        switch (HealthChecker.a) {
            case 2 -> {
                ret = List.of(new Err(1, "test 2b", null, null));
            }
            case 3 -> {
                ret = List.of(new Err(2, "test 3", null, null));
            }
        }
        return ret;
    }

    @Override
    public InspectionType inspectionType() {
        return InspectionType.PauseCheck;
    }
}
