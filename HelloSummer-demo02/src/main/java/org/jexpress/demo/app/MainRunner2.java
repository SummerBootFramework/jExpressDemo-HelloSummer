package org.jexpress.demo.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.summerboot.jexpress.boot.SummerRunner;
import org.summerboot.jexpress.boot.annotation.Order;

@Order(4)
public class MainRunner2 implements SummerRunner {

    private static final Logger log = LogManager.getLogger(MainRunner2.class);

    @Override
    public void run(RunnerContext context) throws Exception {

        log.warn("beforeStart=" + context.getConfigDir());
    }
}
