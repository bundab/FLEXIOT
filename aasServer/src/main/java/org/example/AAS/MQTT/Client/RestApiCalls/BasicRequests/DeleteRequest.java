package org.example.AAS.MQTT.Client.RestApiCalls.BasicRequests;

import org.example.AAS.MQTT.Client.RestApiCalls.AAS.AasRequestStrategy;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DeleteRequest implements AasRequestStrategy {

    @Override
    public Object execute(String urlString, JSONObject body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        if (body != null) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        connection.disconnect();
        return null;
    }
}