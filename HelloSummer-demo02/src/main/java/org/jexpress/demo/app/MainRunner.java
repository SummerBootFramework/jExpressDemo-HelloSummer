package org.jexpress.demo.app;

import com.google.inject.Key;
import com.google.inject.name.Names;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.summerboot.jexpress.boot.SummerInitializer;
import org.summerboot.jexpress.boot.SummerRunner;
import org.summerboot.jexpress.boot.annotation.Order;

import java.io.File;

@Order(1)
public class MainRunner implements SummerInitializer, SummerRunner {

    private static final String CLI_CMD = "mycli";

    private static final Logger log = LogManager.getLogger(MainRunner.class);
    private static final java.util.logging.Logger jul = java.util.logging.Logger.getLogger(MainRunner.class.getName());

    @Override
    public void initCLI(Options options) {
        Option arg = Option.builder(CLI_CMD)
                .desc("this is my cli")
                .build();
        options.addOption(arg);
        Throwable ex = null;//new RuntimeException("test");
        jul.log(java.util.logging.Level.INFO, "JUL log string={0} int={1}", new Object[]{"abc", 123});
        log.info("Log4J2 log stirng={} int={}", "abc", 123, ex);
    }

    @Override
    public void initApp(File configDir) {
        log.info(configDir);
    }

    @Override
    public void run(RunnerContext context) throws Exception {
        if (context.getCli().hasOption(CLI_CMD)) {
            System.out.println("my cli is called");
        }
//        Throwable ex = new RuntimeException("test exception");
//        log.error("test ex", ex);

        log.debug("beforeStart=" + context.getConfigDir());
        SummerRunner plugin = context.getGuiceInjector().getInstance(Key.get(SummerRunner.class, Names.named("myplugin")));
        plugin.run(null);

        //MqttClient.send("hello mqtt1", "hello mqtt2");
        //HttpClient httpClient = new HttpClient();
        //httpClient.send("hello http1", "hello http2");
    }
}
