package org.jexpress.demo.app.event;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jexpress.demo.app.MyInitializer;
import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.boot.event.AppLifecycleHandler;
import org.summerboot.jexpress.boot.event.AppLifecycleListener;
import org.summerboot.jexpress.nio.IdleEventMonitor;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Singleton
@Service(binding = AppLifecycleListener.class)
public class MyAppLifecycleHandler extends AppLifecycleHandler {
    private static final String CLI_CMD = "mycli";
    private static final java.util.logging.Logger jul = java.util.logging.Logger.getLogger(MyInitializer.class.getName());

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


    /**
     * called when application paused or resumed by configuration/pause file or BottController's ${context-root}/status?pause=true|false
     *
     * @param healthOk
     * @param paused
     * @param serviceStatusChanged
     * @param reason
     */
    @Override
    public void onApplicationStatusUpdated(SummerApplication.AppContext context, boolean healthOk, boolean paused, boolean serviceStatusChanged, String reason) throws Exception {
        super.onApplicationStatusUpdated(context, healthOk, paused, serviceStatusChanged, reason);
        System.out.println("My application status updated");
    }

    /**
     * @param healthOk
     * @param paused
     * @param retryIndex
     * @param nextInspectionIntervalSeconds
     */
    @Override
    public void onHealthInspectionFailed(SummerApplication.AppContext context, boolean healthOk, boolean paused, long retryIndex, int nextInspectionIntervalSeconds) throws Exception {
        super.onHealthInspectionFailed(context, healthOk, paused, retryIndex, nextInspectionIntervalSeconds);
        System.out.println("My health inspection failed");
    }

    @Override
    public void onIdle(IdleEventMonitor idleEventMonitor) throws Exception {
        System.out.println("My idle event monitor: " + idleEventMonitor.getName() + " " + idleEventMonitor.toString());
        switch (idleEventMonitor.getName()) {
            case "GRPCServer":
                TimeUnit.SECONDS.sleep(1);
                break;
            case "NioServer":
                TimeUnit.SECONDS.sleep(2);
                break;
        }
    }
}
