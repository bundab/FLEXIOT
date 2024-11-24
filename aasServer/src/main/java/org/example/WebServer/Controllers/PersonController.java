package org.example.WebServer.Controllers;

import org.example.Client.Enums.DeviceType;
import org.example.Client.MainFunctions.RegisterDevice.RegisterDevice;
import org.example.WebServer.Entities.Device;
import org.example.WebServer.Interaction.DeviceTypeMapping;
import org.example.WebServer.Repositories.DeviceRepository;
import org.example.WebServer.Entities.Person;
import org.example.WebServer.Repositories.PersonRepository;
import org.example.WebServer.RequestBodies.AddDeviceRequest;
import org.example.WebServer.RequestBodies.CreateDeviceRequest;
import org.example.WebServer.RequestBodies.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
        // Check if the username already exists
        if (personRepository.findByUsername(request.name) != null) {
            return new ResponseEntity<>("Username already exists!", HttpStatus.CONFLICT); // 409 Conflict
        }


        // Hash the password and save the person
        String hashedPassword = passwordEncoder.encode(request.password);
        Person person = new Person();
        person.setUsername(request.name);
        person.setPassword(hashedPassword);

        personRepository.insert(person);

        // Return success response
        String responseMessage = "Person registered successfully: " + person.getUsername();
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED); // 201 Created
    }


    @GetMapping("/usernames")
    public ResponseEntity<List<Person>> getAllUsers() {
        // Fetch all persons from the repository
        List<Person> people = personRepository.findAll();

        // Return the list of persons with 200 OK
        return new ResponseEntity<>(people, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/login")
    public ResponseEntity<Person> login(@RequestBody LoginRequest lr) {

        Person person = personRepository.findByUsername(lr.name);

        if (person != null && passwordEncoder.matches(lr.password, person.getPassword())) {
            //return ResponseEntity.ok("Login successful! Age: " + person.getAge());
            return ResponseEntity.ok(person);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(person);
    }

    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevicesForPerson(@RequestBody LoginRequest lr) {
        // Find the person by username
        Person person = personRepository.findByUsername(lr.name);

        // If the person exists, return their devices
        if (person != null) {
            return new ResponseEntity<>(person.getDevices(), HttpStatus.OK); // 200 OK
        }

        // If the person doesn't exist, return 404 Not Found
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
    }


    @PostMapping("/create_device")
    public ResponseEntity<String> createDevice(@RequestBody CreateDeviceRequest cdr) {
        // Find the person by username
        Person person = personRepository.findByUsername(cdr.login.name);

        // Check if the person exists and validate the password
        if (person == null || !passwordEncoder.matches(cdr.login.password, person.getPassword())) {
            return new ResponseEntity<>("Login unsuccessful", HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Create the new device and associate it with the person
        Device device = new Device();
        device.setType(cdr.type);
        person.getDevices().add(device);

        // Save the device and update the person
        deviceRepository.insert(device);
        personRepository.save(person);

        RegisterDevice real_device = new RegisterDevice(device.getId(), DeviceTypeMapping.convertStringToDeviceType(cdr.type), cdr.login.name);
        try {
            real_device.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Return success response
        return new ResponseEntity<>("Device created successfully.", HttpStatus.CREATED); // 201 Created
    }
/*
    @PostMapping("/add_device")
    public ResponseEntity<String> addDevice(@RequestBody AddDeviceRequest adr) {

        // Find the person by username
        Person person = personRepository.findByUsername(adr.login.name);

        // Check if the person exists and validate the password
        if (person == null || !passwordEncoder.matches(adr.login.password, person.getPassword())) {
            return new ResponseEntity<>("Login unsuccessful", HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        Optional<Device> device = deviceRepository.findById(adr.Id);
        if (device.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        person.getDevices().add(device.get());
        // Save the the person
        personRepository.save(person);

        // Return success response
        return new ResponseEntity<>("Device created successfully.", HttpStatus.CREATED); // 201 Created
    }
*/

}
