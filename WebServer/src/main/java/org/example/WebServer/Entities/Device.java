package org.example.WebServer.Entities;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "device") // MongoDB collection
public class Device {
    @Id
    private String id;

    private String type;

    private String iotId;

    private ArrayList<String> values = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getValues()
    {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }
}
