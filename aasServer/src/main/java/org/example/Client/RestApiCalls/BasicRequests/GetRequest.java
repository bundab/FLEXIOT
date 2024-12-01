package org.example.Client.RestApiCalls.BasicRequests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.example.Client.RestApiCalls.AAS.AasRequestStrategy;
import org.json.JSONArray;
import org.json.JSONObject;


public class GetRequest implements AasRequestStrategy {

    /**
     * Description: This method represents an HTTP GET request, that is part of "AasStrategyContext" Strategy Pattern.
     * @param urlString The URL for the HTTP GET request.
     * @param body In the case of HTTP GET request, this method DOESN'T use this param, so it's allowed to be null!
     * @return This method returns with String value of the retrieved data.
     * @throws Exception It throws: "Failed: HTTP error code: " exception, if the call didn't succeed.
     */
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
        return jsonResponse;//jsonResponse.trim().startsWith("[") ? new JSONArray(jsonResponse) : new JSONObject(jsonResponse);
    }
}
