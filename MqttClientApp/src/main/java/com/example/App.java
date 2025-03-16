package com.example; // Ensure this matches your directory structure

import com.hivemq.client.mqtt.mqtt3.*;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        // Replace with your HiveMQ Cloud details
        String broker = "83467e1f42404e919a3a50d3714f9bd4.s1.eu.hivemq.cloud"; // Your HiveMQ Cloud URL
        String username = "hivemq.webclient.1742148171061"; // Your HiveMQ Cloud username
        String password = "0<b:APF8v@mEUd?12Sts"; // Your HiveMQ Cloud password (modify if needed)
        String topic = "test/topic"; // MQTT Topic

        // Create MQTT Client
        Mqtt3AsyncClient client = Mqtt3Client.builder()
                .identifier("java-client") // Unique Client ID
                .serverHost(broker)
                .serverPort(8883) // Secure MQTT Port
                .sslWithDefaultConfig() // Required for SSL/TLS
                .simpleAuth()
                    .username(username)
                    .password(password.getBytes(StandardCharsets.UTF_8)) // Ensure password encoding
                    .applySimpleAuth()
                .buildAsync();

        // Connect to the broker
        client.connect().whenComplete((connAck, throwable) -> {
            if (throwable != null) {
                System.err.println("❌ Connection failed: " + throwable.getMessage());
            } else {
                System.out.println("✅ Connected to HiveMQ Cloud!");

                // Publish a message
                client.publishWith()
                        .topic(topic)
                        .payload("Hello from Java MQTT over HiveMQ Cloud!".getBytes(StandardCharsets.UTF_8))
                        .send()
                        .whenComplete((pubAck, pubThrowable) -> {
                            if (pubThrowable != null) {
                                System.err.println("❌ Publish failed: " + pubThrowable.getMessage());
                            } else {
                                System.out.println("✅ Message published successfully!");
                            }
                            client.disconnect();
                        });
            }
        });

        // Keep program running to ensure message is sent
        try {
            Thread.sleep(5000); // Wait 5 seconds before exit
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
