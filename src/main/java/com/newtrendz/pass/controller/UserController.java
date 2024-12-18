package com.newtrendz.pass.controller;

import com.newtrendz.pass.entity.User;
import com.newtrendz.pass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User saveUser = userService.createUser(user);
        return ResponseEntity.ok(saveUser);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<User> getUserBYId(@PathVariable("id") String id){
        User viewUser = userService.getUserById(id);
        return ResponseEntity.ok(viewUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id,@RequestBody User user){
        User update = userService.updateUser(id,user);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id){
        String message = userService.deleteUser(id);
        return ResponseEntity.ok(message);
    }
}
