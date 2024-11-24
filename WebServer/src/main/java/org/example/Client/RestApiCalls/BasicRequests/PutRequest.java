package org.example.Client.RestApiCalls.BasicRequests;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.example.Client.RestApiCalls.AAS.AasRequestStrategy;
import org.json.JSONObject;


public class PutRequest implements AasRequestStrategy {

    @Override
    public Object execute(String urlString, JSONObject body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        connection.disconnect();
        return null;
    }
}
