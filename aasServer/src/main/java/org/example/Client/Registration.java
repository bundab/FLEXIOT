package org.example.Client;

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
import org.example.Client.RestApiCalls.Generate.GenerateJson;
import org.example.Client.RestApiCalls.Generate.GenerateUrl;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registration {
    // MQTT
    private static final String BROKER_URL = "tcp://localhost:1883";
    private final String topic = "DevicesTopicManager";

    private String ID = "pobra1";
    private DeviceType dt = DeviceType.AcSensor;

    // Registry Server
    private String regAasUrl;
    private JSONObject regAasJson;

    private String regStaticSmUrl;
    private JSONObject regStaticSmJson;
    private String regDynamicSmUrl;
    private JSONObject regDynamicSmJson;

    //AAS Server
    private String aasAasUrl;
    private JSONObject aasAasJson;

    private String aasStaticSmUrl;
    private JSONObject aasStaticSmJson;
    private String aasDynamicSmUrl;
    private JSONObject aasDynamicSmJson;

    private ArrayList<String> staticElements = new ArrayList<>();
    private ArrayList<String> dynamicElements = new ArrayList<>();

    private final ArrayList<String> staticElementsUrls = new ArrayList<>();
    private final ArrayList<String> dynamicElementsUrls = new ArrayList<>();
    private final ArrayList<JSONObject> staticElementsJsons = new ArrayList<>();
    private final ArrayList<JSONObject> dynamicElementsJsons = new ArrayList<>();

    public Registration(String id, DeviceType dt) {
        this.ID = id;
        this.dt = dt;

        // AAS Registry Server Registration Variables
        this.regAasUrl = new GenerateUrl().generateUrl(ServerType.AasRegistry, this.ID);
        this.regAasJson = new GenerateJson().generateJson(this.ID, ServerType.AasRegistry, this.dt);

        this.regStaticSmUrl = new GenerateUrl().generateUrl(ServerType.AasRegistry, this.ID, SubmodelType.Static);
        this.regStaticSmJson = new GenerateJson().generateJson(this.ID, ServerType.AasRegistry, this.dt, SubmodelType.Static);
        this.regDynamicSmUrl = new GenerateUrl().generateUrl(ServerType.AasRegistry, this.ID, SubmodelType.Dynamic);
        this.regDynamicSmJson = new GenerateJson().generateJson(this.ID, ServerType.AasRegistry, this.dt, SubmodelType.Dynamic);


        // AAS Server Registration Variables
        this.aasAasUrl = new GenerateUrl().generateUrl(ServerType.AasServer, this.ID);
        this.aasAasJson = new GenerateJson().generateJson(this.ID, ServerType.AasServer, this.dt);

        this.aasStaticSmUrl = new GenerateUrl().generateUrl(ServerType.AasServer, this.ID, SubmodelType.Static);
        this.aasStaticSmJson = new GenerateJson().generateJson(this.ID, ServerType.AasServer, this.dt, SubmodelType.Static);
        this.aasDynamicSmUrl = new GenerateUrl().generateUrl(ServerType.AasServer, this.ID, SubmodelType.Dynamic);
        this.aasDynamicSmJson = new GenerateJson().generateJson(this.ID, ServerType.AasServer, this.dt, SubmodelType.Dynamic);

        // AAS Server Generate Variables for SubmodelElements
        this.staticElements = new DetectFiles().execute("JsonFiles/AasServer/" + dt + "/Static");
        this.dynamicElements = new DetectFiles().execute("JsonFiles/AasServer/" + dt + "/Dynamic");

        // DEBUG!!! ///////////////////////////////////////////////////////////////////////
        for(int i = 0; i < this.staticElements.size(); i++) {
            System.out.println("Static: " + this.staticElements.get(i));
            //System.out.println("Dynamic: " + this.dynamicElements.get(i));
        }
        for(int i = 0; i < this.dynamicElements.size(); i++) {
            //System.out.println("Static: " + this.staticElements.get(i));
            System.out.println("Dynamic: " + this.dynamicElements.get(i));
        }
        //////////////////////////////////////////////////////////////////////////////////

        // Generate Urls for Submodel Elements
        for(int i = 0; i < this.staticElements.size(); i++) {
            this.staticElementsUrls.add(new GenerateUrl().generateUrl(this.ID, StaticSubmodelElementType.valueOf(this.staticElements.get(i))));
        }

        for(int i = 0; i < this.dynamicElements.size(); i++) {
            this.dynamicElementsUrls.add(new GenerateUrl().generateUrl(this.ID, DynamicSubmodelElementType.valueOf(this.dynamicElements.get(i))));
        }

        // DEBUG!!! ////////////////////////////////////////////////////////////////////////
        for(int i = 0; i < this.staticElements.size(); i++) {
            System.out.println("Static blablabla: " + this.staticElementsUrls.get(i));
            //System.out.println("Dynamic blablabla: " + this.dynamicElementsUrls.get(i));
        }
        for(int i = 0; i < this.dynamicElements.size(); i++) {
            //System.out.println("Static blablabla: " + this.staticElementsUrls.get(i));
            System.out.println("Dynamic blablabla: " + this.dynamicElementsUrls.get(i));
        }
        ////////////////////////////////////////////////////////////////////////////////////

        // Generate Jsons for Submodel Elements
        for(int i = 0; i < this.staticElements.size(); i++) {
            this.staticElementsJsons.add(new GenerateJson().generateJson(this.ID, this.dt, StaticSubmodelElementType.valueOf(this.staticElements.get(i))));
        }
        for(int i = 0; i < this.dynamicElements.size(); i++) {
            // Choose between String and Float type values
            if(new GenerateJson().generateJson(this.ID, this.dt, DynamicSubmodelElementType.valueOf(this.dynamicElements.get(i)), "").toString().contains("\"valueType\": \"float\",")) {
                this.dynamicElementsJsons.add(new GenerateJson().generateJson(this.ID, this.dt, DynamicSubmodelElementType.valueOf(this.dynamicElements.get(i)), 0));
            }
            else{
                this.dynamicElementsJsons.add(new GenerateJson().generateJson(this.ID, this.dt, DynamicSubmodelElementType.valueOf(this.dynamicElements.get(i)), ""));
            }
        }
    }

    public void execute() throws Exception {
        // TODO: Implement this!!! The client side will send the topics precisely during the registration process with MQTT publishing (Add/Delete Topic for the Manager)!!!

        // AAS Registry Server's REST API calls
        System.out.println(this.regAasUrl);
        new AasRequestContext(RequestType.Put).executeRequest(this.regAasUrl, this.regAasJson);
        
        new AasRequestContext(RequestType.Put).executeRequest(this.regStaticSmUrl, this.regStaticSmJson);
        new AasRequestContext(RequestType.Put).executeRequest(this.regDynamicSmUrl, this.regDynamicSmJson);

        // AAS Server's REST API Calls
        // AAS and Submodels
        new AasRequestContext(RequestType.Put).executeRequest(this.aasAasUrl, this.aasAasJson);

        System.out.println(this.aasStaticSmUrl);
        new AasRequestContext(RequestType.Put).executeRequest(this.aasStaticSmUrl, this.aasStaticSmJson);
        new AasRequestContext(RequestType.Put).executeRequest(this.aasDynamicSmUrl, this.aasDynamicSmJson);

        // Static and Dynamic Submodel Elements
        for(int i = 0; i < this.staticElements.size(); i++) {
            new AasRequestContext(RequestType.Put).executeRequest(this.staticElementsUrls.get(i), this.staticElementsJsons.get(i));
        }
        for(int i = 0; i < this.dynamicElements.size(); i++) {
            new AasRequestContext(RequestType.Put).executeRequest(this.dynamicElementsUrls.get(i), this.dynamicElementsJsons.get(i));
        }

        executeMqttRegistration();
    }

    // Implement the MQTT registration function, that sends the topics needed to be registered.
    private void executeMqttRegistration() {
        String[] messages = new String[this.dynamicElements.size()];
        try {
            // Kliens inicializálása (egyedi azonosítóval)
            MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId());

            // Kapcsolódás a brokerhez
            client.connect();
            System.out.println("Connected to MQTT broker: " + BROKER_URL);

            // Generate mseeages
            for (int i = 0; i < this.dynamicElements.size(); i++) {
                messages[i] = "ADD." + this.dt + "/" + this.ID + "/" + this.dynamicElements.get(i);
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
