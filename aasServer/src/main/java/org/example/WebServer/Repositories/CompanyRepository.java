package org.example.WebServer.Repositories;

import org.example.WebServer.Entities.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Company findByName(String name);
}

