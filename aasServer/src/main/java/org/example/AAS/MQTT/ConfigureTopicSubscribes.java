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


public class ConfigureTopicSubscribes {
    // MQTT
    private static final String BROKER_URL = "tcp://localhost:1883";
    private final String topic = "DevicesTopicManager";

    /**
     * Description: Generates the MQTT topic subscriptions based on the registered AASs in the MongoDB database.
     * @throws Exception Error during MQTT topic Subscription.
     */
    public void executeTopicConfiguration() throws Exception {
        String[] aasIds = getAasIds();
        ArrayList<String> messages = new ArrayList<>();

        try {
            // Initializing client (with a unique identifier)
            MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId());

            // Connect to the Broker
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

            // Publishing messages
            for (String message : messages) {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(1); // QoS beállítása (0, 1, vagy 2)
                client.publish(topic, mqttMessage);
                System.out.println("Published message on topic " + topic + ": " + message);
            }

            // Disconnect from the Broker
            client.disconnect();
            System.out.println("Disconnected from MQTT broker.");

        } catch (MqttException e) {
            System.err.println("Error while publishing messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Description: It receives the IDs of the registered AAS.
     * @return AAS IDs in String[] format.
     * @throws Exception Exception during getting the IDs of the AASs.
     */
    private String[] getAasIds() throws Exception {
        // Initializing ObjectMapper for JSON processing
        ObjectMapper objectMapper = new ObjectMapper();

        // Loading JSON text into a JsonNode
        JsonNode rootArray = objectMapper.readTree(new AasRequestContext(RequestType.Get).executeRequest(new GenerateUrl().generateUrl(ServerType.AasRegistry), new JSONObject()).toString());

        // Collecting the fields of 'identification.id'
        String[] Ids = rootArray.findValues("identification").stream()
                .map(node -> node.get("id").asText())  // Selecting the 'id' field
                .toArray(String[]::new);  // Convert to String[]

        ArrayList<String> aasIds = new ArrayList<>();
        for(int i = 0; i < Ids.length; i = i + 4) {
            aasIds.add(Ids[i]);
            //System.out.println(Ids[i]);
        }
        String[] realAasIds = new String[aasIds.size()];
        for(int i = 0; i < realAasIds.length; i++) {
            realAasIds[i] = aasIds.get(i);
            //System.out.println(realAasIds[i]);
        }
        return realAasIds;
    }
}
