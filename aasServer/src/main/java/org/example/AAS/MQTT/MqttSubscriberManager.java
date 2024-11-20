package org.example.AAS.MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.example.AAS.MQTT.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.AAS.MQTT.Client.Enums.DeviceType;
import org.example.AAS.MQTT.Client.Enums.RequestType;
import org.example.AAS.MQTT.Client.RestApiCalls.AAS.AasRequestContext;
import org.example.AAS.MQTT.Client.RestApiCalls.Generate.GenerateJson;
import org.example.AAS.MQTT.Client.RestApiCalls.Generate.GenerateUrl;
import org.json.JSONObject;

import java.util.concurrent.*;

public class MqttSubscriberManager {

    private final MqttClient client;
    private final ExecutorService executor;
    private final ConcurrentHashMap<String, Boolean> subscriptions = new ConcurrentHashMap<>();

    public MqttSubscriberManager(String brokerUrl, int maxThreads) throws MqttException {
        this.client = new MqttClient(brokerUrl, MqttClient.generateClientId());

        // Beállítjuk a callback-et a bejövő üzenetek kezeléséhez
        this.client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Received message from topic " + topic + ": " + new String(message.getPayload()));

                //TODO: Send PUT request to change the data in the AAS database too! Or do the MongoDB implementation... Topic: DeviceType/ID/SubmodelElementType (Done)
                String[] id = topic.split("/");

                for(int i = 0; i < id.length; i++) {
                    System.out.println(id[i] + "\n");
                }

                System.out.println(Float.parseFloat(new String(message.getPayload())) + "\n");

                System.out.println(DynamicSubmodelElementType.valueOf(id[2]));

                String url = new GenerateUrl().generateUrl(id[1], DynamicSubmodelElementType.valueOf(id[2]));
                JSONObject json = new GenerateJson().generateJson(id[1], DeviceType.valueOf(id[0]), DynamicSubmodelElementType.valueOf(id[2]), Float.parseFloat(new String(message.getPayload())));

                System.out.println("URL: " + url);
                System.out.println("JSON: " + json.toString());

                new AasRequestContext(RequestType.Put).executeRequest(url, json);
                System.out.println("Ha ez jo akkor fasz tudja mi a baj...");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Üzenetküldés lezárása - nem szükséges itt semmit tenni előfizetőként
            }
        });

        this.client.connect();
        this.executor = Executors.newFixedThreadPool(maxThreads);
    }

    // Új topicra való feliratkozás események alapján
    public void handleEvent(String eventType, String topic) {
        System.out.println("Itt is jo meg...\n");
        switch (eventType) {
            case "ADD":
                System.out.println("1. Itt is jo meg...\n");
                subscribeToTopic(topic);
                System.out.println("2. Itt is jo meg...\n");
                break;
            case "REMOVE":
                unsubscribeFromTopic(topic);
                break;
            default:
                System.out.println("Unknown event type: " + eventType);
        }
    }

    // Feliratkozás a megadott topicra
    private void subscribeToTopic(String topic) {
        if (subscriptions.containsKey(topic)) {
            System.out.println("Already subscribed to topic: " + topic);
            return;
        }

        executor.submit(() -> {
            try {
                client.subscribe(topic);
                subscriptions.put(topic, true);
                System.out.println("Subscribed to topic: " + topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    // Leiratkozás a megadott topicról
    private void unsubscribeFromTopic(String topic) {
        if (!subscriptions.containsKey(topic)) {
            System.out.println("No subscription found for topic: " + topic);
            return;
        }

        executor.submit(() -> {
            try {
                client.unsubscribe(topic);
                subscriptions.remove(topic);
                System.out.println("Unsubscribed from topic: " + topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    // ThreadPoolExecutor és MQTT kliens bezárása
    public void shutdown() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            client.disconnect();
            System.out.println("Disconnected from broker");
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
