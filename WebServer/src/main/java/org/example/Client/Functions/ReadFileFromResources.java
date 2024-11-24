package org.example.Client.Functions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ReadFileFromResources {

    /**
     *
     * @param path
     * @return
     */
    public String execute(String path) {

        String jsonText = "";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("Nem található a fájl: " + path);
            }

            // Fájl tartalmának beolvasása Stringként
            jsonText = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // JSON tartalom kiírása
            System.out.println("Beolvasott fájl: " + jsonText); // Szép formázás

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonText;
    }
}
