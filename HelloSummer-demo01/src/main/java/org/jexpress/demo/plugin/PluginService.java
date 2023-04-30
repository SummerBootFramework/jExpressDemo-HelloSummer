package org.jexpress.demo.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import org.apache.commons.cli.Options;
import org.summerboot.jexpress.boot.SummerInitializer;
import org.summerboot.jexpress.boot.SummerRunner;
import org.summerboot.jexpress.boot.annotation.Order;
import org.summerboot.jexpress.boot.annotation.Service;

@Service(named = "myplugin")
@Order(2)
public class PluginService implements SummerInitializer, SummerRunner {

    private static final Logger log = LogManager.getLogger(PluginService.class);

    @Override
    public void initCLI(Options options) {
        log.info("");
    }

    @Override
    public void initApp(File configDir) {
        log.info(configDir);
    }

    @Override
    public void run(RunnerContext context) throws Exception {
        log.info(PluginConfig.cfg.getLicenseKey());
    }

}
