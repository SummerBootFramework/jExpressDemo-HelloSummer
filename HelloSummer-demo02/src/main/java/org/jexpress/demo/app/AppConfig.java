package org.jexpress.demo.app;

import java.io.File;
import java.util.Properties;
import org.summerboot.jexpress.boot.config.BootConfig;
import org.summerboot.jexpress.boot.config.ConfigUtil;
import org.summerboot.jexpress.boot.config.annotation.ImportResource;

/**
 *
 * @author Changski Tie Zheng Zhang
 */
@ImportResource("cfg_app.properties")
public class AppConfig extends BootConfig {

    public static final AppConfig cfg = new AppConfig();

    private AppConfig() {
    }

    @Override
    protected void loadCustomizedConfigs(File cfgFile, boolean isReal, ConfigUtil helper, Properties props) throws Exception {
        System.out.println(isReal);
    }

    @Override
    public void shutdown() {
    }
}
