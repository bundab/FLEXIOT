package org.example.AAS.MQTT.Client.RestApiCalls.Generate;

import org.example.AAS.MQTT.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.AAS.MQTT.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.AAS.MQTT.Client.Enums.AAS.SubmodelType;
import org.example.AAS.MQTT.Client.Enums.DeviceType;
import org.example.AAS.MQTT.Client.Enums.ServerType;
import org.example.AAS.MQTT.Client.Functions.ReadFileFromResources;
import org.json.JSONObject;


public class GenerateJson {

    // Generate Json File //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param id
     * @param serverType
     * @param deviceType
     * @return
     */
    public JSONObject generateJson(String id, ServerType serverType, DeviceType deviceType) {
        String path = "JsonFiles/" + serverType + "/" + deviceType + "/Aas.json";
        JSONObject jsonObject = readJsonFile(id, path);
        return jsonObject;
    }

    /**
     *
     * @param id
     * @param serverType
     * @param deviceType
     * @param submodelType
     * @return
     */
    public JSONObject generateJson(String id, ServerType serverType, DeviceType deviceType, SubmodelType submodelType) {
        String path = "JsonFiles/" + serverType + "/" + deviceType + "/" + submodelType + "Submodel.json";
        JSONObject jsonObject = readJsonFile(id, path);
        return jsonObject;
    }

    /**
     *
     * @param id
     * @param deviceType
     * @param staticSubmodelElementType
     * @return
     */
    public JSONObject generateJson(String id, DeviceType deviceType, StaticSubmodelElementType staticSubmodelElementType) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Static/" + staticSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path);
        return jsonObject;
    }


    public JSONObject generateJson(String id, DeviceType deviceType, StaticSubmodelElementType staticSubmodelElementType, String value) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Static/" + staticSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path, value);
        return jsonObject;
    }

    /**
     *
     * @param id
     * @param deviceType
     * @param dynamicSubmodelElementType
     * @return
     */
    public JSONObject generateJson(String id, DeviceType deviceType, DynamicSubmodelElementType dynamicSubmodelElementType, String value) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Dynamic/" + dynamicSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path, value);
        return jsonObject;
    }

    /**
     *
     * @param id
     * @param deviceType
     * @param dynamicSubmodelElementType
     * @return
     */
    public JSONObject generateJson(String id, DeviceType deviceType, DynamicSubmodelElementType dynamicSubmodelElementType, float value) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Dynamic/" + dynamicSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path, value);
        return jsonObject;
    }


    // Read data from Json file ////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param id
     * @param path
     * @return
     */
    private JSONObject readJsonFile(String id, String path) {
        String jsonText = new ReadFileFromResources().execute(path);

        // Replace template ID with the actual ID
        jsonText = jsonText.replace("TemplateID", id);

        return new JSONObject(jsonText);
    }

    /**
     *
     * @param id
     * @param path
     * @param value
     * @return
     */
    private JSONObject readJsonFile(String id, String path, String value) {
        String jsonText = new ReadFileFromResources().execute(path);

        // Replace template ID and String value with the actual ID and String value
        jsonText = jsonText.replace("TemplateID", id);
        jsonText = jsonText.replace("TemplateValue", value);

        return new JSONObject(jsonText);
    }

    /**     Maybe we don't need this...
     *
     * @param id
     * @param path
     * @param value
     * @return
     */
    private JSONObject readJsonFile(String id, String path, float value) {
        String jsonText = new ReadFileFromResources().execute(path);

        // Replace template ID and String value with the actual ID and String value
        jsonText = jsonText.replace("TemplateID", id);
        jsonText = jsonText.replace("\"TemplateValue\"", String.valueOf(value));

        return new JSONObject(jsonText);
    }
}
