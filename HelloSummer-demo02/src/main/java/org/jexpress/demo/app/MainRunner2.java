package org.jexpress.demo.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.summerboot.jexpress.boot.SummerRunner;
import org.summerboot.jexpress.boot.annotation.Order;

@Order(2)// incase you have more than one SummerRunner implementations and would like to sepcify the order to be called
public class MainRunner2 implements SummerRunner {

    private static final Logger log = LogManager.getLogger(MainRunner2.class);

    @Override
    public void run(RunnerContext context) throws Exception {
        log.debug("beforeStart=" + context.getConfigDir());
    }
}
