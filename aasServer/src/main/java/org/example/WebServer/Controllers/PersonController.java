package org.example.WebServer.Controllers;

import org.example.WebServer.Entities.Device;
import org.example.WebServer.Repositories.DeviceRepository;
import org.example.WebServer.Entities.Person;
import org.example.WebServer.Repositories.PersonRepository;
import org.example.WebServer.RequestBodies.CreateDeviceRequest;
import org.example.WebServer.RequestBodies.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String register(@RequestBody Person person) {

        if (personRepository.findByUsername(person.getUsername()) != null) {
            return "Username already exists!";
        }

        String hashedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(hashedPassword);


        personRepository.save(person);
        return "Person registered successfully: " + person.getUsername();
    }

    @GetMapping("/usernames")
    public List<String> getAllUsernames() {
        List<Person> people = personRepository.findAll();
        return people.stream()
                .map(Person::getUsername)
                .toList();
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {

        Person person = personRepository.findByUsername(username);

        if (person != null && passwordEncoder.matches(password, person.getPassword())) {
            return "Login successful! Age: " + person.getAge();
        }

        return "Invalid credentials.";
    }

    @GetMapping("/devices")
    public List<String> getAllUsernamesInCompany(@RequestBody LoginRequest lr) {
        Person person = personRepository.findByUsername(lr.name);

        if (person != null) {
            return person.getDevices().stream()
                    .map(Device::getType)
                    .toList();
        }
        return List.of();
    }

    @PostMapping("/create_device")
    public String createDevice(@RequestBody CreateDeviceRequest cdr)
    {
        Person person = personRepository.findByUsername(cdr.login.name);

        if (person == null || !passwordEncoder.matches(cdr.login.password, person.getPassword())) {
            return "Login unsuccessful";
        }

        Device device = new Device();
        device.setType(cdr.type);
        person.getDevices().add(device);
        deviceRepository.insert(device);
        personRepository.save(person);

        return "Device created.";
    }

}
