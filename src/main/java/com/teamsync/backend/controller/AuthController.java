package com.teamsync.backend.controller;

import com.teamsync.backend.model.User;
import com.teamsync.backend.repository.UserRepository;
import com.teamsync.backend.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    // DTOs
    public static class AuthRequest {
        @NotBlank(message = "Username is mandatory")
        public String username;

        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        public String password;

        @Email(message = "Email should be valid")
        public String email;
        public String firstName;
        public String lastName;
        public String profilePictureUrl;
        public Set<@NotBlank(message = "Role cannot be blank") String> roles;
    }

    public static class AuthResponse {
        public User user;
        public String jwt;

        public AuthResponse(User user, String jwt) {
            this.user = user;
            this.jwt = jwt;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.warn("Login attempt with invalid data: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
            );
        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {}", authRequest.username);
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        final String jwt = jwtUtil.generateToken(authRequest.username);
        Optional<User> existingUser = userRepository.findByUsername(authRequest.username);
        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User does NOT exist.");
        }
        logger.info("User authenticated successfully: {}", authRequest.username);
        return ResponseEntity.ok(new AuthResponse(existingUser.get(), jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Optional<User> existingUser = userRepository.findByUsername(authRequest.username);
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = User.builder()
                .username(authRequest.username)
                .password(passwordEncoder.encode(authRequest.password))
                .email(authRequest.email)
                .firstName(authRequest.firstName)
                .lastName(authRequest.lastName)
                .profilePictureUrl(authRequest.profilePictureUrl)
                .roles(authRequest.roles)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
