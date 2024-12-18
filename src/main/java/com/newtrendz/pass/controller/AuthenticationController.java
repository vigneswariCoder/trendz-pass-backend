package com.newtrendz.pass.controller;

import com.newtrendz.pass.dto.JwtResponse;
import com.newtrendz.pass.dto.RefreshTokenDto;
import com.newtrendz.pass.dto.RefreshTokenResponse;
import com.newtrendz.pass.dto.SigninDto;
import com.newtrendz.pass.entity.RefreshToken;
import com.newtrendz.pass.entity.User;
import com.newtrendz.pass.exceptions.MasterNotFoundException;
import com.newtrendz.pass.repository.DocumentMasterRepository;
import com.newtrendz.pass.repository.RefreshTokenRepository;
import com.newtrendz.pass.repository.UserRepository;
import com.newtrendz.pass.service.DocumentMasterService;
import com.newtrendz.pass.security.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtBlackListService jwtBlackListService;

    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentMasterRepository documentMasterRepository;

    @Autowired
    private DocumentMasterService documentMasterService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody SigninDto signinDto) {
        try {
            String name = signinDto.getName() != null ? signinDto.getName() : null;
            System.out.println(" Name: " + name);
            System.out.println("SigninDto: " + signinDto);

            Optional<User> userOptional = userRepository.findByEmailOrNameOrPhoneNumber(name);
            System.out.println("User optional: "+ userOptional);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(signinDto.getPassword(), user.getPassword())) {
                    System.out.println(signinDto.getPassword());
                    Authentication authenticate = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user.getName(), signinDto.getPassword())
                    );

                    if (authenticate.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authenticate);
                        UserDetailsImplementation userDetails = UserDetailsImplementation.build(user);

                        String jwtToken = jwtService.generateJwtToken(user.getName());

                        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

                        return ResponseEntity.ok().body(new JwtResponse(
                                userDetails.getId(),
                                userDetails.getUsername(),
                                userDetails.getEmail(),
                                userDetails.getPhoneNumber(),
                                userDetails.getStatus(),
                                userDetails.getDeletedStatus(),
                                refreshToken.getToken(),
                                jwtToken
                        ));
                    }
                    else {
                        return ResponseEntity.notFound().build();
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String token) {

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);

            jwtBlackListService.addToBlacklist(jwt);

            String username = jwtService.extractUserName(jwt);
            Optional<User> userDetails = userRepository.findByEmail(username);

            if (userDetails.isPresent()) {
                String userId = userDetails.get().getId();

                ObjectId objectId = new ObjectId(userId);
                refreshTokenService.deleteByUserId(userId);

                SecurityContextHolder.getContext().setAuthentication(null);

                return ResponseEntity.ok("Logged out successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid token!");
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserDetail)
                .map(user -> {
                    String token = jwtService.generateJwtToken(user.getEmail());
                    return ResponseEntity.ok(new RefreshTokenResponse(
                                    token,
                                    refreshToken
                            )
                    );
                })
                .orElseThrow(() -> new MasterNotFoundException("Refresh token is not in database!.." + refreshToken));
    }

    @PostMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody SigninDto signinDto){
        String encryptedPassword = passwordEncoder.encode(signinDto.getPassword());
        Optional<User> existsUser = this.userRepository.findByEmail(signinDto.getName());
        if (existsUser.isPresent()) {
            existsUser.get().setPassword(encryptedPassword);
            User updatedUsers = userRepository.save(existsUser.get());
            return ResponseEntity.ok().body("Password Updated SuccessFully..! with Name "+updatedUsers.getName());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> changeProfile(@RequestPart(name = "profile") MultipartFile profile){
        UserDetailsImplementation userDetails = (UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String documentFor = "profile";
        documentMasterService.addDocuments(List.of(profile),userDetails.getId(),documentFor);
        return ResponseEntity.ok().body("Profile Photo Updated..!");
    }
    @PostMapping("/expired_signout")
    public ResponseEntity<?> deleteUserData(@RequestParam String userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                refreshTokenRepository.deleteByUserId(userId);
                return ResponseEntity.ok("User data and refresh tokens deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            log.error("Error while deleting user data: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user data.");
        }
    }

}
