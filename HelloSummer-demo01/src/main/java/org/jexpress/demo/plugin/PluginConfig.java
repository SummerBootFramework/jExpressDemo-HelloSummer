package org.jexpress.demo.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.util.Properties;
import org.summerboot.jexpress.boot.config.BootConfig;
import org.summerboot.jexpress.boot.config.ConfigUtil;
import org.summerboot.jexpress.boot.config.annotation.Config;
import org.summerboot.jexpress.boot.config.annotation.ConfigHeader;
import org.summerboot.jexpress.boot.config.annotation.ImportResource;

/**
 *
 * @author Changski Tie Zheng Zhang
 */
@ImportResource("cfg_plugin.properties")
public class PluginConfig extends BootConfig {

    public static void main(String[] args) {
        String t = generateTemplate(PluginConfig.class);
        System.out.println(t);
    }

    public static final PluginConfig cfg = new PluginConfig();

    private PluginConfig() {
    }

    @ConfigHeader(title = "My Header description")
    @JsonIgnore
    @Config(key = "plugin.licenseKey", validate = Config.Validate.Encrypted, required = false)
    protected volatile String licenseKey;

    @Override
    protected void loadCustomizedConfigs(File cfgFile, boolean isNotMock, ConfigUtil helper, Properties props) throws Exception {
    }

    @Override
    public void shutdown() {
    }

    public String getLicenseKey() {
        return licenseKey;
    }
}
