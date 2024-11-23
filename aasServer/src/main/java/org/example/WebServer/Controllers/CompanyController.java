package org.example.WebServer.Controllers;

import org.example.WebServer.Entities.Company;
import org.example.WebServer.Entities.Device;
import org.example.WebServer.Repositories.CompanyRepository;
import org.example.WebServer.Entities.Person;
import org.example.WebServer.Repositories.DeviceRepository;
import org.example.WebServer.Repositories.PersonRepository;
import org.example.WebServer.RequestBodies.CreateDeviceRequest;
import org.example.WebServer.RequestBodies.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

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
    public String register(@RequestBody Company company) {
        if (companyRepository.findByName(company.getName()) != null) {
            return "Company already exists!";
        }

        String hashedPassword = passwordEncoder.encode(company.getPassword());
        company.setPassword(hashedPassword);

        companyRepository.save(company);
        return "Company registered successfully: " + company.getName();
    }

    @PostMapping("/addPerson/{username}")
    public String addPersonToCompany(@PathVariable String username,
                                     @RequestBody LoginRequest loginRequest) {

        Company company = companyRepository.findByName(loginRequest.name);

        if (company == null) {
            return "Company not found!";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.password, company.getPassword())) {
            return "Invalid password for company!";
        }

        Person person = personRepository.findByUsername(username);

        if (person == null) {
            return "Person not found!";
        }

        if (person.getCompany() != null && person.getCompany().getName().equals(loginRequest.name)) {
            return "Person is already in this company!";
        }

        person.setCompany(company);
        personRepository.save(person);

        company.getEmployees().add(person);
        companyRepository.save(company);

        return "Person added to company!";
    }


    @GetMapping("/users")
    public List<String> getAllUsernamesInCompany(@RequestBody LoginRequest lr) {
        Company company = companyRepository.findByName(lr.name);

        if (company == null || !passwordEncoder.matches(lr.password, company.getPassword())) {
            return List.of();
        }


        return company.getEmployees().stream()
                .map(Person::getUsername)
                .toList();
    }

    @GetMapping("/list")
    public List<String> getAllCompanies() {
        List<Company> people = companyRepository.findAll();
        return people.stream()
                .map(Company::getName)
                .toList();
    }

    @GetMapping("/devices")
    public List<String> getAllDevicesInCompany(@RequestBody LoginRequest lr) {
        Company company = companyRepository.findByName(lr.name);

        if (company != null) {
            return company.getDevices().stream()
                    .map(Device::getType)
                    .toList();
        }
        return List.of();
    }

    @PostMapping("/create_device")
    public String createDevice(@RequestBody CreateDeviceRequest cdr)
    {
        Company company = companyRepository.findByName(cdr.login.name);

        if (company == null || !passwordEncoder.matches(cdr.login.password, company.getPassword())) {
            return "Login unsuccessful";
        }

        Device device = new Device();
        device.setType(cdr.type);
        company.getDevices().add(device);
        deviceRepository.insert(device);
        companyRepository.save(company);

        return "Device created.";
    }
}
