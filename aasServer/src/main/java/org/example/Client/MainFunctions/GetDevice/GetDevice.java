package org.example.Client.MainFunctions.GetDevice;

import org.example.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.Client.Enums.DeviceType;
import org.example.Client.Enums.RequestType;
import org.example.Client.Functions.DetectFiles;
import org.example.Client.RestApiCalls.AAS.AasRequestContext;
import org.example.Client.RestApiCalls.Generate.GenerateUrl;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetDevice {

    private String ID = "pobra1";

    private final ArrayList<String> staticElements;
    private final ArrayList<String> dynamicElements;

    private final ArrayList<String> staticElementsUrls = new ArrayList<>();
    private final ArrayList<String> dynamicElementsUrls = new ArrayList<>();

    private final ArrayList<String> values = new ArrayList<>();

    public GetDevice(String id, DeviceType dt) {
        this.ID = id;

        // AAS Server Generate Variables for SubmodelElements
        this.staticElements = new DetectFiles().execute("JsonFiles/AasServer/" + dt + "/Static");
        this.dynamicElements = new DetectFiles().execute("JsonFiles/AasServer/" + dt + "/Dynamic");

        // Generate Urls for Submodel Elements
        for(int i = 0; i < this.staticElements.size(); i++) {
            this.staticElementsUrls.add(new GenerateUrl().generateUrl(this.ID, StaticSubmodelElementType.valueOf(this.staticElements.get(i))) + "/value");
        }

        for(int i = 0; i < this.dynamicElements.size(); i++) {
            this.dynamicElementsUrls.add(new GenerateUrl().generateUrl(this.ID, DynamicSubmodelElementType.valueOf(this.dynamicElements.get(i))) + "/value");
        }
    }

    public ArrayList<String> execute() throws Exception {

        // Static and Dynamic Submodel Elements
        for(int i = 0; i < this.staticElements.size(); i++) {
            values.add(String.valueOf(new AasRequestContext(RequestType.Get).executeRequest(this.staticElementsUrls.get(i), new JSONObject())));
        }
        for(int i = 0; i < this.dynamicElements.size(); i++) {
            values.add(String.valueOf(new AasRequestContext(RequestType.Get).executeRequest(this.dynamicElementsUrls.get(i), new JSONObject())));
        }

        // Just for Debugging...
        for (String value : values) {
            System.out.println(value);
        }

        return values;
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
