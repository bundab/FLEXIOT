package org.example.AAS.MQTT.Client.RestApiCalls.Generate;

import org.example.AAS.MQTT.Client.Enums.AAS.DynamicSubmodelElementType;
import org.example.AAS.MQTT.Client.Enums.AAS.StaticSubmodelElementType;
import org.example.AAS.MQTT.Client.Enums.AAS.SubmodelType;
import org.example.AAS.MQTT.Client.Enums.ServerType;

public class GenerateUrl {
    private final String aasRegistryUrlBasic = "http://localhost:4000/registry/api/v1/registry";
    private final String aasServerUrlBasic = "http://localhost:4001/aasServer/shells";

    /**
     *
     * @param serverType
     * @return
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
     *
     * @param serverType
     * @param id
     * @return
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
     *
     * @param serverType
     * @param id
     * @param submodelType
     * @return
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
     * Static Submodel Element Url, only for AAS Server!
     * The Submodel Element has only Short ID.
     * @param id
     * @param staticSubmodelElementType
     * @return
     */
    public String generateUrl(String id, StaticSubmodelElementType staticSubmodelElementType) {
        return this.aasServerUrlBasic + "/" + id + "/aas/submodels/" + SubmodelType.Static + id + "Short/submodel/submodelElements/" + staticSubmodelElementType + id + "Short";
    }

    /**
     * Dynamic Submodel Element Url, only for AAS Server!
     * The Submodel Element has only Short ID.
     * @param id
     * @param dynamicSubmodelElementType
     * @return
     */
    public String generateUrl(String id, DynamicSubmodelElementType dynamicSubmodelElementType) {
        return this.aasServerUrlBasic + "/" + id + "/aas/submodels/" + SubmodelType.Dynamic + id + "Short/submodel/submodelElements/" + dynamicSubmodelElementType + id + "Short";
    }
}
