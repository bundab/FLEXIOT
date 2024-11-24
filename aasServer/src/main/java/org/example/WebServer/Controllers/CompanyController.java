package org.example.WebServer.Controllers;

import org.example.Client.MainFunctions.RegisterDevice.RegisterDevice;
import org.example.WebServer.Entities.Company;
import org.example.WebServer.Entities.Device;
import org.example.WebServer.Interaction.DeviceTypeMapping;
import org.example.WebServer.Repositories.CompanyRepository;
import org.example.WebServer.Entities.Person;
import org.example.WebServer.Repositories.DeviceRepository;
import org.example.WebServer.Repositories.PersonRepository;
import org.example.WebServer.RequestBodies.AddDeviceRequest;
import org.example.WebServer.RequestBodies.CreateDeviceRequest;
import org.example.WebServer.RequestBodies.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping("/register")
    public ResponseEntity<Company> register(@RequestBody Company company) {
        // Check if company already exists
        if (companyRepository.findByName(company.getName()) != null) {
            //return new ResponseEntity<>("Company already exists!", HttpStatus.CONFLICT); // 409 Conflict
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(company);
        }

        // Hash the password and save the company
        String hashedPassword = passwordEncoder.encode(company.getPassword());
        company.setPassword(hashedPassword);

        companyRepository.save(company);

        // Return success response
        String responseMessage = "Company registered successfully: " + company.getName();
       // return new ResponseEntity<>(responseMessage, HttpStatus.CREATED); // 201 Created
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @PostMapping("/login")
    public ResponseEntity<Company> login(@RequestBody LoginRequest lr) {

        Company company = companyRepository.findByName(lr.name);

        if (company != null && passwordEncoder.matches(lr.password, company.getPassword())) {
            //return ResponseEntity.ok("Login successful!");
            return ResponseEntity.status(HttpStatus.OK).body(company);
        }

        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(company);
    }

    @PostMapping("/addPerson/{username}")
    public ResponseEntity<String> addPersonToCompany(@PathVariable String username,
                                                     @RequestBody LoginRequest loginRequest) {
        // Find the company by name
        Company company = companyRepository.findByName(loginRequest.name);
        if (company == null) {
            return new ResponseEntity<>("Company not found!", HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // Validate the company's password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!loginRequest.password.equals(company.getPassword())) {
            return new ResponseEntity<>("Invalid password for company!", HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Find the person by username
        Person person = personRepository.findByUsername(username);
        if (person == null) {
            return new ResponseEntity<>("Person not found!", HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // Check if the person is already in the company
        if (person.getCompany() != null && person.getCompany().getName().equals(loginRequest.name)) {
            return new ResponseEntity<>("Person is already in this company!", HttpStatus.CONFLICT); // 409 Conflict
        }

        // Associate the person with the company
        person.setCompany(company);
        personRepository.save(person);

        company.getEmployees().add(person);
        companyRepository.save(company);

        // Return success response
        return new ResponseEntity<>("Person added to company!", HttpStatus.OK); // 200 OK
    }

    @GetMapping("/users")
    public ResponseEntity<List<Person>> getAllUsersInCompany(@RequestParam("name") String name,@RequestParam("password") String password) {
        // Find the company by name
        Company company = companyRepository.findByName(name);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 404 Not Found
        }

        // Validate the company's password
        if (!password.trim().equals(company.getPassword().trim())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Return the list of persons (employees) in the company with 200 OK
        return new ResponseEntity<>(company.getEmployees(), HttpStatus.OK); // 200 OK
    }


    @GetMapping("/list")
    public ResponseEntity<List<Company>> getAllCompanies() {
        // Fetch all companies from the repository
        List<Company> companies = companyRepository.findAll();

        // Return the list of companies with 200 OK
        return new ResponseEntity<>(companies, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevicesInCompany(@RequestBody LoginRequest lr) {
        // Find the company by name
        Company company = companyRepository.findByName(lr.name);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // Validate the company's password
        if (!passwordEncoder.matches(lr.password, company.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Return the list of devices with 200 OK
        return new ResponseEntity<>(company.getDevices(), HttpStatus.OK); // 200 OK
    }



    @PostMapping("/create_device")
    public ResponseEntity<String> createDevice(@RequestBody CreateDeviceRequest cdr) {
        // Find the company by name
        Company company = companyRepository.findByName(cdr.login.name);
        if (company == null) {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // Validate the company's password
        if (!passwordEncoder.matches(cdr.login.password, company.getPassword())) {
            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Create the device and associate it with the company
        Device device = new Device();
        device.setType(cdr.type);
        company.getDevices().add(device);

        // Save the device and update the company
        deviceRepository.insert(device);
        companyRepository.save(company);

        RegisterDevice real_device = new RegisterDevice(device.getId(), DeviceTypeMapping.convertStringToDeviceType(cdr.type), cdr.login.name);
        try {
            real_device.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Return success response
        return new ResponseEntity<>("Device created successfully", HttpStatus.CREATED); // 201 Created
    }

    /*
    @PostMapping("/add_device")
    public ResponseEntity<String> addDevice(@RequestBody AddDeviceRequest adr) {
        // Find the company by username
        Company company = companyRepository.findByName(adr.login.name);

        // Check if the company exists and validate the password
        if (company == null || !passwordEncoder.matches(adr.login.password, company.getPassword())) {
            return new ResponseEntity<>("Login unsuccessful", HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        Optional<Device> device = deviceRepository.findById(adr.Id);
        if (device.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        company.getDevices().add(device.get());
        // Save the the person
        companyRepository.save(company);

        // Return success response
        return new ResponseEntity<>("Device created successfully.", HttpStatus.CREATED); // 201 Created
    }

     */

}
