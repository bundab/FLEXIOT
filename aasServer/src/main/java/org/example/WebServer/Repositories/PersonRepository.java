package org.example.WebServer.Repositories;

import org.example.WebServer.Entities.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
    Person findByUsername(String username);
}

