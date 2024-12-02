package org.jexpress.demo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.summerboot.jexpress.boot.config.BootConfig;
import org.summerboot.jexpress.boot.config.ConfigUtil;
import org.summerboot.jexpress.boot.config.annotation.Config;
import org.summerboot.jexpress.boot.config.annotation.ConfigHeader;
import org.summerboot.jexpress.boot.config.annotation.ImportResource;

import java.io.File;
import java.util.Properties;

@ImportResource("cfg_app.properties")
public class MyConfig extends BootConfig {

    public static void main(String[] args) {
        String t = generateTemplate(MyConfig.class);
        System.out.println(t);
    }

    public static final MyConfig cfg = new MyConfig();

    private MyConfig() {
    }

    @ConfigHeader(title = "My Header description")
    @JsonIgnore
    @Config(key = "my.licenseKey", validate = Config.Validate.Encrypted, required = true)
    protected volatile String licenseKey;

    @Config(key = "my.licenseKeys", defaultValue = "0 0/5 14,18 * * ? ; 0 10,44 14 ? 3 WED", collectionDelimiter = ";")
    protected volatile String[] licenseKesy;

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
