package com.newtrendz.pass.repository;

import com.newtrendz.pass.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
