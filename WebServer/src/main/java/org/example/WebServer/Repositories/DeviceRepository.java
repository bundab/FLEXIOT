package org.example.WebServer.Repositories;

import org.example.WebServer.Entities.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
}