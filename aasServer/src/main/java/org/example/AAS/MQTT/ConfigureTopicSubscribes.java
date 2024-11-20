package org.example.AAS.MQTT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.example.AAS.MQTT.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.AAS.MQTT.Client.Enums.DeviceType;
import org.example.AAS.MQTT.Client.Enums.ServerType;
import org.example.AAS.MQTT.Client.RestApiCalls.Generate.GenerateUrl;
import org.example.AAS.MQTT.Client.Enums.RequestType;
import org.example.AAS.MQTT.Client.RestApiCalls.AAS.AasRequestContext;
import org.example.Client.Functions.DetectFiles;
import org.json.JSONObject;

import java.util.ArrayList;

// TODO: Add every topic subscribe to the communication at the start of the AAS Server! (NOT DONE!!!)
public class ConfigureTopicSubscribes {
    // MQTT
    private static final String BROKER_URL = "tcp://localhost:1883";
    private final String topic = "DevicesTopicManager";

    public void executeTopicConfiguration() throws Exception {
        String[] aasIds = getAasIds();
        ArrayList<String> messages = new ArrayList<>();

        try {
            // Kliens inicializálása (egyedi azonosítóval)
            MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId());

            // Kapcsolódás a brokerhez
            client.connect();
            System.out.println("Connected to MQTT broker: " + BROKER_URL);

            // Generate messages
            for (int i = 0; i < aasIds.length; i++) {
                String deviceType = new AasRequestContext(RequestType.Get).executeRequest(new GenerateUrl().generateUrl(aasIds[i], StaticSubmodelElementType.Type), new JSONObject()).toString();

                if (deviceType.contains("AcSensor")) {
                    deviceType = DeviceType.AcSensor.toString();
                } else if (deviceType.contains("PowerSensor")) {
                    deviceType = DeviceType.PowerSensor.toString();
                } else if (deviceType.contains("HumiditySensor")) {
                    deviceType = DeviceType.HumiditySensor.toString();
                } else if (deviceType.contains("SoundSensor")) {
                    deviceType = DeviceType.SoundSensor.toString();
                } else if (deviceType.contains("AdvancedWeatherSensor")) {
                    deviceType = DeviceType.AdvancedWeatherSensor.toString();
                }

                ArrayList<String> dynamicElements = new DetectFiles().execute("JsonFiles/AasServer/" + deviceType + "/Dynamic");

                for(int j = 0; j < dynamicElements.size(); j++) {
                    messages.add("ADD." + deviceType + "/" + aasIds[i] + "/" + dynamicElements.get(j));
                    System.out.println(messages.get(j));
                }
            }

            // Üzenetek publikálása
            for (String message : messages) {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(1); // QoS beállítása (0, 1, vagy 2)
                client.publish(topic, mqttMessage);
                System.out.println("Published message on topic " + topic + ": " + message);
            }

            // Kapcsolat bontása
            client.disconnect();
            System.out.println("Disconnected from MQTT broker.");

        } catch (MqttException e) {
            System.err.println("Error while publishing messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String[] getAasIds() throws Exception {
        // ObjectMapper inicializálása a JSON feldolgozásához
        ObjectMapper objectMapper = new ObjectMapper();

        // A JSON szöveg betöltése JsonNode-ba
        JsonNode rootArray = objectMapper.readTree(new AasRequestContext(RequestType.Get).executeRequest(new GenerateUrl().generateUrl(ServerType.AasRegistry), new JSONObject()).toString());

        // Az "identification.id" mezők kigyűjtése
        String[] Ids = rootArray.findValues("identification").stream()
                .map(node -> node.get("id").asText())  // Kiválasztja az "id" mezőt
                .toArray(String[]::new);  // Átalakítja String[]-re

        ArrayList<String> aasIds = new ArrayList<>();
        for(int i = 0; i < Ids.length; i = i + 4) {
            aasIds.add(Ids[i]);
            //System.out.println(Ids[i]);
        }
        String[] realAasIds = new String[aasIds.size()];
        for(int i = 0; i < realAasIds.length; i++) {
            realAasIds[i] = aasIds.get(i);
            System.out.println(realAasIds[i]);
        }
        return realAasIds;
    }
}
