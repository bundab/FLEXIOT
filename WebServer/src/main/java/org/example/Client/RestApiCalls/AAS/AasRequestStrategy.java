package org.example.Client.RestApiCalls.AAS;

import org.json.JSONObject;


public interface AasRequestStrategy {
    Object execute(String url, JSONObject body) throws Exception;
}
