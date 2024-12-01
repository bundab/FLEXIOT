package org.example.AAS.MQTT.Client.RestApiCalls.BasicRequests;

import org.example.AAS.MQTT.Client.RestApiCalls.AAS.AasRequestStrategy;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;


public class DeleteRequest implements AasRequestStrategy {

    /**
     * Description: This method represents an HTTP DELETE request, that is part of "AasStrategyContext" Strategy Pattern.
     * @param urlString The URL for the HTTP DELETE request.
     * @param body In the case of HTTP DELETE request, this method DOESN'T use this param, so it's allowed to be null!
     * @return This method returns with null, because the HTTP DELETE doesn't have any returns except the response.
     * @throws Exception It throws: "Failed: HTTP error code: " exception, if the call didn't succeed.
     */
    @Override
    public Object execute(String urlString, JSONObject body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        connection.disconnect();
        return null;
    }
}
