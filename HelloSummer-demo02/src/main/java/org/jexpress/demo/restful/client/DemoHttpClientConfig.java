package org.jexpress.demo.restful.client;

import org.jexpress.demo.mqtt.MyMqttClientConfig;
import org.summerboot.jexpress.boot.config.annotation.ImportResource;
import org.summerboot.jexpress.integration.httpclient.HttpClientConfig;

@ImportResource("cfg_httpclient.properties")
public class DemoHttpClientConfig extends HttpClientConfig {
    public static void main(String... args) {
        String t = generateTemplate(MyMqttClientConfig.class);
        System.out.println(t);
    }

    public static final DemoHttpClientConfig cfg = new DemoHttpClientConfig();

    private DemoHttpClientConfig() {
    }
}
