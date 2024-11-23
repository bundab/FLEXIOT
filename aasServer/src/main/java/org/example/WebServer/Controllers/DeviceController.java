package org.example.WebServer.Controllers;

import org.example.Client.Enums.DeviceType;
import org.example.Client.MainFunctions.GetDevice.GetDevice;
import org.example.WebServer.Entities.Device;
import org.example.WebServer.Repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository2;

    @GetMapping("/list")
    public List<String> getAllDevices() {
        List<Device> devices = deviceRepository2.findAll();
        return devices.stream()
                .map(Device::getType)
                .toList();
    }
}