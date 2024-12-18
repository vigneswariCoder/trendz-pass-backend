package com.newtrendz.pass.service;

import com.newtrendz.pass.entity.User;
import com.newtrendz.pass.exceptions.DuplicateEntryException;
import com.newtrendz.pass.exceptions.MasterNotFoundException;
import com.newtrendz.pass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        Optional<User> userEmail = userRepository.findByEmail(user.getEmail());
        if(userEmail.isPresent()){
            throw new DuplicateEntryException("Email Already Taken..!" +user.getEmail());
        }
        String hashedPassword = passwordEncoder.encode("123");
        user.setPassword(hashedPassword);
        user.setStatus(1);
        user.setDeletedStatus(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(()->new MasterNotFoundException("Role Not Found With This ID : "+id));
    }

    public User updateUser(String id, User user) {
        User getUser = userRepository.findById(id).orElseThrow(()->new MasterNotFoundException("User Not Found With This ID : "+id));
        getUser.setName(user.getName());
        getUser.setEmail(user.getEmail());
        getUser.getPassword();
        getUser.setPhoneNumber(user.getPhoneNumber());
        getUser.setStatus(user.getStatus());
        getUser.getDeletedStatus();
        getUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(getUser);
    }

    public String deleteUser(String id) {
        User deleteByUser = userRepository.findById(id).orElseThrow();
        deleteByUser.setDeletedStatus(1);
        deleteByUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(deleteByUser);
        return "Deleted Successfully..!";
    }

    public List<User> getAllUser() {
        return userRepository.findByDeletedStatus(0);
    }
}
