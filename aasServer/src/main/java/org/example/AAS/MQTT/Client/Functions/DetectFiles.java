package org.example.AAS.MQTT.Client.Functions;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetectFiles {

    /**
     * Description: Detects the existing files in a directory.
     * @param path The relative path of the folder to be mapped within the resource directory.
     * @return ArrayList<String> type, which contains the names of the files in the specified folder, without extensions, sorted in alphabetical order.
     */
    public ArrayList<String> execute(String path) {
        try {
            // Retrieving the path of the resources folder
            URL resourceUrl = getClass().getClassLoader().getResource(path);
            if (resourceUrl == null) {
                throw new RuntimeException("Directory can not be found: " + path);
            }

            // Open the folder
            File folder = new File(resourceUrl.toURI());
            if (!folder.isDirectory()) {
                throw new RuntimeException("The given path is not a directory: " + path);
            }

            // Reading file names and removing extensions
            List<String> fileNames = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    int dotIndex = fileName.lastIndexOf('.');
                    if (dotIndex > 0) {
                        fileName = fileName.substring(0, dotIndex); // Trim the extension
                    }
                    fileNames.add(fileName);
                }
            }

            // Sorting file names in alphabetical order
            Collections.sort(fileNames);

            // Converting the result into an array
            return new ArrayList<String>(fileNames);
        } catch (Exception e) {
            throw new RuntimeException("An error has been occurred during file reading: " + e.getMessage(), e);
        }
    }
}
