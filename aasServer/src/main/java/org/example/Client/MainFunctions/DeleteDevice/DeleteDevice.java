package org.example.Client.MainFunctions.DeleteDevice;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.example.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.Client.Enums.AAS.SubmodelType;
import org.example.Client.Enums.DeviceType;
import org.example.Client.Enums.RequestType;
import org.example.Client.Enums.ServerType;
import org.example.Client.Functions.DetectFiles;
import org.example.Client.RestApiCalls.AAS.AasRequestContext;
import org.example.Client.RestApiCalls.Generate.GenerateUrl;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeleteDevice {
    // MQTT
    private static final String BROKER_URL = "tcp://localhost:1883";
    private final String topic = "DevicesTopicManager";

    private String ID = "pobra1";
    private DeviceType dt = DeviceType.AcSensor;

    // Registry Server
    private String regAasUrl;

    private String regStaticSmUrl;
    private String regDynamicSmUrl;

    //AAS Server
    private String aasAasUrl;

    private String aasStaticSmUrl;
    private String aasDynamicSmUrl;

    private ArrayList<String> staticElements = new ArrayList<>();
    private ArrayList<String> dynamicElements = new ArrayList<>();

    private final ArrayList<String> staticElementsUrls = new ArrayList<>();
    private final ArrayList<String> dynamicElementsUrls = new ArrayList<>();

    public DeleteDevice(String id, DeviceType dt) {
        this.ID = id;
        this.dt = dt;

        // AAS Registry Server DeleteDevice Variables
        this.regAasUrl = new GenerateUrl().generateUrl(ServerType.AasRegistry, this.ID);

        this.regStaticSmUrl = new GenerateUrl().generateUrl(ServerType.AasRegistry, this.ID, SubmodelType.Static);
        this.regDynamicSmUrl = new GenerateUrl().generateUrl(ServerType.AasRegistry, this.ID, SubmodelType.Dynamic);


        // AAS Server RegisterDevice Variables
        this.aasAasUrl = new GenerateUrl().generateUrl(ServerType.AasServer, this.ID);

        this.aasStaticSmUrl = new GenerateUrl().generateUrl(ServerType.AasServer, this.ID, SubmodelType.Static);
        this.aasDynamicSmUrl = new GenerateUrl().generateUrl(ServerType.AasServer, this.ID, SubmodelType.Dynamic);

        // AAS Server Generate Variables for SubmodelElements
        this.staticElements = new DetectFiles().execute("JsonFiles/AasServer/" + dt + "/Static");
        this.dynamicElements = new DetectFiles().execute("JsonFiles/AasServer/" + dt + "/Dynamic");

        // Generate Urls for Submodel Elements
        for(int i = 0; i < this.staticElements.size(); i++) {
            this.staticElementsUrls.add(new GenerateUrl().generateUrl(this.ID, StaticSubmodelElementType.valueOf(this.staticElements.get(i))));
        }

        for(int i = 0; i < this.dynamicElements.size(); i++) {
            this.dynamicElementsUrls.add(new GenerateUrl().generateUrl(this.ID, DynamicSubmodelElementType.valueOf(this.dynamicElements.get(i))));
        }
    }

    public void execute() throws Exception {
        // TODO: Implement this!!! The client side will send the topics precisely during the registration process with MQTT publishing (Add/Delete Topic for the Manager)!!!
        // TODO: Fordított sorrendben kell meghívni a Delet Request-eket, előbb Property, aztán submodel és végül aas!!!

        executeMqttDeleteDevice();

        // AAS Registry Server's REST API calls
        new AasRequestContext(RequestType.Delete).executeRequest(this.regStaticSmUrl, new JSONObject());
        new AasRequestContext(RequestType.Delete).executeRequest(this.regDynamicSmUrl, new JSONObject());
        new AasRequestContext(RequestType.Delete).executeRequest(this.regAasUrl, new JSONObject());

        // AAS Server's REST API Calls
        System.out.println(this.aasStaticSmUrl);
        System.out.println(this.aasDynamicSmUrl);
        System.out.println(this.aasAasUrl);

        for(int i = 0; i < this.staticElements.size(); i++) {
            new AasRequestContext(RequestType.Delete).executeRequest(this.staticElementsUrls.get(i), new JSONObject());
        }
        for(int i = 0; i < this.dynamicElements.size(); i++) {
            new AasRequestContext(RequestType.Delete).executeRequest(this.dynamicElementsUrls.get(i), new JSONObject());
        }

        // TODO: I have no idea what the fuck is going on, but this deleting shit doesn't work...
        new AasRequestContext(RequestType.Delete).executeRequest(this.aasStaticSmUrl, new JSONObject());
        new AasRequestContext(RequestType.Delete).executeRequest(this.aasDynamicSmUrl, new JSONObject());
        new AasRequestContext(RequestType.Delete).executeRequest(this.aasAasUrl, new JSONObject());


    }

    // Implement the MQTT registration function, that sends the topics needed to be registered.
    private void executeMqttDeleteDevice() {
        String[] messages = new String[this.dynamicElements.size()];
        try {
            // Kliens inicializálása (egyedi azonosítóval)
            MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId());

            // Kapcsolódás a brokerhez
            client.connect();
            System.out.println("Connected to MQTT broker: " + BROKER_URL);

            // Generate mseeages
            for (int i = 0; i < this.dynamicElements.size(); i++) {
                messages[i] = "REMOVE." + this.dt + "/" + this.ID + "/" + this.dynamicElements.get(i);
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
}
