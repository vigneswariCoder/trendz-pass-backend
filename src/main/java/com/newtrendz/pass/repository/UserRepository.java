package com.newtrendz.pass.repository;

import com.newtrendz.pass.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ $or: [ { 'email': ?0 }, { 'name': ?0 }, { 'phone_number': ?0 } ] }")
    Optional<User> findByEmailOrNameOrPhoneNumber(String value);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByDeletedStatus(int i);
}

