package org.example.Client.RestApiCalls.Generate;

import org.example.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.Client.Enums.ServerType;
import org.example.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.Client.Enums.AAS.SubmodelType;

public class GenerateUrl {
    private final String aasRegistryUrlBasic = "http://localhost:4000/registry/api/v1/registry";
    private final String aasServerUrlBasic = "http://localhost:4001/aasServer/shells";

    /**
     * Description: Generates the URL of the AASs on the AAS Server or the Registry.
     * @param serverType Selects the AAS Server or the Registry.
     * @return The URL in String format.
     */
    public String generateUrl(ServerType serverType) {
        if(serverType == ServerType.AasRegistry)
        {
            return this.aasRegistryUrlBasic;
        } else {
            return this.aasServerUrlBasic;
        }
    }

    /**
     * Description: Generates the URL of the AAS based on the AAS Server or the Registry and the Iot Device's ID.
     * @param serverType Selects the AAS Server or the Registry.
     * @param id The ID of the IoT Device.
     * @return The URL in String format.
     */
    public String generateUrl(ServerType serverType, String id) {
        if(serverType == ServerType.AasRegistry)
        {
            return this.aasRegistryUrlBasic + "/" + id;
        } else {
            return this.aasServerUrlBasic + "/" + id;
        }
    }

    /**
     * Generates the URL of the AAS based on the AAS Server or the Registry, the Iot Device's ID and the preferred Submodel type.
     * @param serverType Selects the AAS Server or the Registry.
     * @param id The ID of the IoT Device.
     * @param submodelType Selects the Static or the Dynamic Submodel.
     * @return The URL in String format.
     */
    public String generateUrl(ServerType serverType, String id, SubmodelType submodelType) {
        if(serverType == ServerType.AasRegistry)
        {
            return this.aasRegistryUrlBasic + "/" + id + "/submodels/" + submodelType.toString() + id;
        } else {
            // charAt(0), because we need IDShort, not the ID
            return this.aasServerUrlBasic + "/" + id + "/aas/submodels/" + submodelType.toString() + id + "Short";
        }
    }

    /**
     * Generates the URL of the AAS based on the Iot Device's ID and the preferred Static SubmodelElement type.
     * Bonus Info: Static Submodel Element Url, only for AAS Server! The Submodel Element has only Short ID.
     * @param id The ID of the IoT Device.
     * @param staticSubmodelElementType Selects the preferred Static Submodel's SubmodelElement.
     * @return The URL in String format.
     */
    public String generateUrl(String id, StaticSubmodelElementType staticSubmodelElementType) {
        return this.aasServerUrlBasic + "/" + id + "/aas/submodels/" + SubmodelType.Static + id + "Short/submodel/submodelElements/" + staticSubmodelElementType + id + "Short";
    }

    /**
     * Generates the URL of the AAS based on the Iot Device's ID and the preferred Dynamic SubmodelElement type.
     * Bonus Info: Dynamic Submodel Element Url, only for AAS Server! The Submodel Element has only Short ID.
     * @param id The ID of the IoT Device.
     * @param dynamicSubmodelElementType Selects the preferred Dynamic Submodel's SubmodelElement.
     * @return The URL in String format.
     */
    public String generateUrl(String id, DynamicSubmodelElementType dynamicSubmodelElementType) {
        return this.aasServerUrlBasic + "/" + id + "/aas/submodels/" + SubmodelType.Dynamic + id + "Short/submodel/submodelElements/" + dynamicSubmodelElementType + id + "Short";
    }
}
