package org.example.Client.RestApiCalls.BasicRequests;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.example.Client.RestApiCalls.AAS.AasRequestStrategy;
import org.json.JSONObject;


public class DeleteRequest implements AasRequestStrategy {

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
