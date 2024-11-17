package org.example.Client.RestApiCalls.BasicRequests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.example.Client.RestApiCalls.AAS.AasRequestStrategy;
import org.json.JSONArray;
import org.json.JSONObject;


public class GetRequest implements AasRequestStrategy {

    @Override
    public Object execute(String urlString, JSONObject body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        connection.disconnect();

        String jsonResponse = response.toString();
        return jsonResponse.trim().startsWith("[") ? new JSONArray(jsonResponse) : new JSONObject(jsonResponse);
    }
}
