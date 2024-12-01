package org.example.Client.RestApiCalls.Generate;

import org.example.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.Client.Enums.AAS.SubmodelType;
import org.example.Client.Enums.DeviceType;
import org.example.Client.Enums.ServerType;
import org.example.Client.Functions.ReadFileFromResources;
import org.json.JSONObject;


public class GenerateJson {

    // Generate Json File //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Description: Generate the JSON Object of an AAS.
     * @param id The ID of the IoT Device.
     * @param serverType It selects the Registry or the AAS Server.
     * @param deviceType Sets the type of the device that the AAS will represent.
     * @return The generated JSONObject type value of the AAS.
     */
    public JSONObject generateJson(String id, ServerType serverType, DeviceType deviceType) {
        String path = "JsonFiles/" + serverType + "/" + deviceType + "/Aas.json";
        JSONObject jsonObject = readJsonFile(id, path);
        return jsonObject;
    }

    /**
     * Description: Generate the JSON Object of an AAS's Submodel.
     * @param id The ID of the IoT Device.
     * @param serverType It selects the Registry or the AAS Server.
     * @param deviceType Sets the type of the device that the AAS will represent.
     * @param submodelType Sets the type of the AAS's Submodel to Static or Dynamic.
     * @return The generated JSONObject type value of the AAS's Submodel.
     */
    public JSONObject generateJson(String id, ServerType serverType, DeviceType deviceType, SubmodelType submodelType) {
        String path = "JsonFiles/" + serverType + "/" + deviceType + "/" + submodelType + "Submodel.json";
        JSONObject jsonObject = readJsonFile(id, path);
        return jsonObject;
    }

    /**
     * Description: Generate the JSON Object of an AAS's Static Submodel's SubmodelElement.
     * @param id The ID of the IoT Device.
     * @param deviceType Sets the type of the device that the AAS will represent.
     * @param staticSubmodelElementType Sets the SubmodelElement's type of the Static Submodel.
     * @return The generated JSONObject type value of the AAS's Static Submodel's SubmodelElement.
     */
    public JSONObject generateJson(String id, DeviceType deviceType, StaticSubmodelElementType staticSubmodelElementType) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Static/" + staticSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path);
        return jsonObject;
    }

    /**
     * Description: Generate the JSON Object of an AAS's Static Submodel's SubmodelElement with a String value.
     * @param id The ID of the IoT Device.
     * @param deviceType Sets the type of the device that the AAS will represent.
     * @param staticSubmodelElementType Sets the SubmodelElement's type of the Static Submodel.
     * @param value The value of the "String type" SubmodelElement.
     * @return The generated JSONObject type value of the AAS's Static Submodel's SubmodelElement with a String value.
     */
    public JSONObject generateJson(String id, DeviceType deviceType, StaticSubmodelElementType staticSubmodelElementType, String value) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Static/" + staticSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path, value);
        return jsonObject;
    }

    /**
     * Description: Generate the JSON Object of an AAS's Dynamic Submodel's SubmodelElement with String value.
     * @param id The ID of the IoT Device.
     * @param deviceType Sets the type of the device that the AAS will represent.
     * @param dynamicSubmodelElementType Sets the SubmodelElement's type of the Dynamic Submodel.
     * @param value The value of the "String type" SubmodelElement.
     * @return The generated JSONObject type value of the AAS's Dynamic Submodel's SubmodelElement with String value.
     */
    public JSONObject generateJson(String id, DeviceType deviceType, DynamicSubmodelElementType dynamicSubmodelElementType, String value) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Dynamic/" + dynamicSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path, value);
        return jsonObject;
    }

    /**
     * Description: Generate the JSON Object of an AAS's Dynamic Submodel's SubmodelElement with float value.
     * @param id The ID of the IoT Device.
     * @param deviceType Sets the type of the device that the AAS will represent.
     * @param dynamicSubmodelElementType Sets the SubmodelElement's type of the Dynamic Submodel.
     * @param value The value of the "float type" SubmodelElement.
     * @return The generated JSONObject type value of the AAS's Dynamic Submodel's SubmodelElement with float value.
     */
    public JSONObject generateJson(String id, DeviceType deviceType, DynamicSubmodelElementType dynamicSubmodelElementType, float value) {
        String path = "JsonFiles/AasServer/" + deviceType + "/Dynamic/" + dynamicSubmodelElementType + ".json";
        JSONObject jsonObject = readJsonFile(id, path, value);
        return jsonObject;
    }


    // Read data from Json file ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Description: Read the JSON File of an AAS, Submodel, or SubmodelElement and replace the template IDs with the actual ones.
     * @param id The ID of the IoT Device.
     * @param path The relative path of the JSON file in the resource directory.
     * @return The adjusted JSONObject type value of the AAS, or Submodel, or SubmodelElement that has been read from the file.
     */
    private JSONObject readJsonFile(String id, String path) {
        String jsonText = new ReadFileFromResources().execute(path);

        // Replace template ID with the actual ID
        jsonText = jsonText.replace("TemplateID", id);

        return new JSONObject(jsonText);
    }

    /**
     * Description: Read the JSON File of an SubmodelElement and replace the template IDs and the String template value with the actual ones.
     * @param id The ID of the IoT Device.
     * @param path The relative path of the JSON file in the resource directory.
     * @param value The value of the "String type" SubmodelElement.
     * @return The adjusted JSONObject type value of the AAS's Submodel, that has been read from the file.
     */
    private JSONObject readJsonFile(String id, String path, String value) {
        String jsonText = new ReadFileFromResources().execute(path);

        // Replace template ID and String value with the actual ID and String value
        jsonText = jsonText.replace("TemplateID", id);
        jsonText = jsonText.replace("TemplateValue", value);

        return new JSONObject(jsonText);
    }

    /**
     * Description: Read the JSON File of an SubmodelElement and replace the template IDs and the float template value with the actual ones.
     * @param id The ID of the IoT Device.
     * @param path The relative path of the JSON file in the resource directory.
     * @param value The value of the "float type" SubmodelElement.
     * @return The adjusted JSONObject type value of the SubmodelElement, that has been read from the file.
     */
    private JSONObject readJsonFile(String id, String path, float value) {
        String jsonText = new ReadFileFromResources().execute(path);

        // Replace template ID and String value with the actual ID and String value
        jsonText = jsonText.replace("TemplateID", id);
        jsonText = jsonText.replace("\"TemplateValue\"", String.valueOf(value));

        return new JSONObject(jsonText);
    }
}
