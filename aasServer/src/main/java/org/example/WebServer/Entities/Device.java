package org.example.WebServer.Entities;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "device") // MongoDB collection
public class Device {
    @Id
    private String id;

    private String type;

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
}
