package org.jexpress.demo.mqtt;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class MqttClient {
    public static void send(String... messages) throws MqttException {
        MqttConnectionOptions connOpts = ClientConfig.cfg.buildConnectionOptions();
        connOpts.setCleanStart(false);

        MqttClientPersistence customPersistence = new MqttDefaultFilePersistence(".");
        MqttAsyncClient asyncClient = ClientConfig.cfg.build(customPersistence);
        IMqttToken token = asyncClient.connect(connOpts);
        token.waitForCompletion();
        System.out.println("MQTT Connected");

        try {
            int qos = ClientConfig.cfg.getDefaultQos();
            String topic = ClientConfig.cfg.getDefaultTopic();
            boolean isRetained = ClientConfig.cfg.isDefaultRetain();

            for (String message : messages) {
                message += " " + System.currentTimeMillis();
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(qos);
                mqttMessage.setRetained(isRetained);
                token = asyncClient.publish(topic, mqttMessage);
                token.waitForCompletion();
                System.out.println("MQTT Published message: " + message);
            }
        } finally {
            ClientConfig.cfg.shutdown(asyncClient);
        }
    }
}
