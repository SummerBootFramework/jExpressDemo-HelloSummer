package org.jexpress.demo.app;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jexpress.demo.grpc.client.GrpcClientConfig1;
import org.jexpress.demo.grpc.client.Hello1Client;
import org.summerboot.jexpress.boot.SummerInitializer;
import org.summerboot.jexpress.boot.SummerRunner;
import org.summerboot.jexpress.boot.annotation.Order;

import java.io.File;
import java.util.concurrent.TimeUnit;

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

    /**
     * @param configDir
     */
    @Override
    public void initAppBeforeIoC(File configDir) {
        log.info(configDir);
    }

    @Override
    public void initAppAfterIoC(File configDir, Injector guiceInjector) {
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

        int max = 0;//1000000;
        for (int i = 0; i < max; i++) {
            log.debug("load logs " + i);
        }

        GrpcClientConfig1 grpcCfg1 = GrpcClientConfig1.cfg;
        Hello1Client c1 = new Hello1Client().withConfig(grpcCfg1);
        //GrpcClientConfig2 grpcCfg2 = GrpcClientConfig2.cfg;
        //Hello1Client c2 = new Hello1Client().withConfig(grpcCfg2);
        c1.connect();
        //c2.connect();
        int i = 0;
        boolean flag = false;
        while (flag) {
            try {
                String response1 = c1.hello("1John" + i, "1Doe" + i);
                log.info("response1=" + response1);
                TimeUnit.SECONDS.sleep(6);
                //String response2 = c2.hello("2John" + i, "2Doe" + i);
                //log.info("response2=" + response2);
            } catch (Throwable ex) {
                log.error("error", ex);
            }
        }
    }
}
