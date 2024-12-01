package org.example.AAS.MQTT.Client.Functions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ReadFileFromResources {

    /**
     * Description: This method reads a file from the resource directory.
     * @param path The relative path of the file to be read within the resource directory.
     * @return Returns the content of the specified file.
     */
    public String execute(String path) {

        String jsonText = "";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("File doesn't exist: " + path);
            }

            // Fájl tartalmának beolvasása Stringként
            jsonText = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // JSON tartalom kiírása
            System.out.println("Read file: " + jsonText); // Szép formázás

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonText;
    }
}
