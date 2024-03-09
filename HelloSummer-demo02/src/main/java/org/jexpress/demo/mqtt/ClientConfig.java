package org.jexpress.demo.mqtt;

import org.jexpress.demo.grpc.client.HelloClientConfig;
import org.summerboot.jexpress.boot.config.annotation.ImportResource;
import org.summerboot.jexpress.nio.mqtt.MQTTClientConfig;

@ImportResource("cfg_mqttclient.properties")
public class ClientConfig extends MQTTClientConfig {
    public static final ClientConfig cfg = new ClientConfig();

    public static void main(String... args) {
        String t = generateTemplate(HelloClientConfig.class);
        System.out.println(t);
    }
}
