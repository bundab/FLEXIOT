package org.example.Client.Functions;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetectFiles {

    public ArrayList<String> execute(String path) {
        try {
            // Resources mappa elérési útjának lekérdezése
            URL resourceUrl = getClass().getClassLoader().getResource(path);
            if (resourceUrl == null) {
                throw new RuntimeException("Nem található a megadott mappa: " + path);
            }

            // Mappa megnyitása
            File folder = new File(resourceUrl.toURI());
            if (!folder.isDirectory()) {
                throw new RuntimeException("A megadott út nem mappa: " + path);
            }

            // Fájlok neveinek beolvasása és kiterjesztés eltávolítása
            List<String> fileNames = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    int dotIndex = fileName.lastIndexOf('.');
                    if (dotIndex > 0) {
                        fileName = fileName.substring(0, dotIndex); // Kiterjesztés levágása
                    }
                    fileNames.add(fileName);
                }
            }

            // Fájlnevek rendezése ábécé sorrendbe
            Collections.sort(fileNames);

            // Eredmény tömbbé alakítása
            return new ArrayList<String>(fileNames);
        } catch (Exception e) {
            throw new RuntimeException("Hiba történt a fájlok beolvasása során: " + e.getMessage(), e);
        }
    }
}
