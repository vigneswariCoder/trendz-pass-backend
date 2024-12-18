package com.newtrendz.pass.security;

import com.newtrendz.pass.entity.User;
import com.newtrendz.pass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nameOrEmailOrPhoneNumber) throws UsernameNotFoundException {
        Optional<User> userInfo = userRepository.findByEmail(nameOrEmailOrPhoneNumber);
        if (!userInfo.isPresent()) {
            userInfo = userRepository.findByPhoneNumber(nameOrEmailOrPhoneNumber);
        }
        if (!userInfo.isPresent()) {
            userInfo = userRepository.findByName(nameOrEmailOrPhoneNumber);
        }
        return userInfo.map(UserDetailsImplementation::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + nameOrEmailOrPhoneNumber));
    }

}
