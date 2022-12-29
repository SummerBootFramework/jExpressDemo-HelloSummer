package org.jexpress.demo.app;

import com.google.inject.Inject;
import java.io.File;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.summerboot.jexpress.boot.SummerRunner;
import org.summerboot.jexpress.boot.annotation.Order;
import org.summerboot.jexpress.integration.smtp.PostOffice;
import org.summerboot.jexpress.boot.SummerInitializer;

@Order(1)
public class MainRunner implements SummerRunner, SummerInitializer {

    private static Logger log;// = LogManager.getLogger(MainRunner.class);

    @Inject
    PostOffice po;

    @Override
    public void initCLI(Options options) {
    }

    @Override
    public void initApp(File configDir) {
    }

    @Override
    public void run(RunnerContext context) throws Exception {
        log = LogManager.getLogger(MainRunner.class);
        log.warn("beforeStart=" + context.getConfigDir());
    }
}
