package org.example.WebServer.Controllers;

import org.example.Client.MainFunctions.GetDevice.GetDevice;
import org.example.WebServer.Entities.Device;
import org.example.WebServer.Interaction.DeviceTypeMapping;
import org.example.WebServer.Repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Device>> getAllDevices() {
        // Fetch all devices from the repository
        List<Device> devices_in_rep = deviceRepository.findAll();
        List<Device> devices = new ArrayList<>(devices_in_rep);

        for (Device device : devices) {
            GetDevice real_device = new GetDevice(device.getIotId(), DeviceTypeMapping.convertStringToDeviceType(device.getType()));
            try {
                real_device.execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            device.setValues(real_device.getValues());
            device.setId(device.getId());
        }

        // Return the list of devices with 200 OK
        return new ResponseEntity<>(devices, HttpStatus.OK); // 200 OK
    }
}