package url_shortener.project.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import url_shortener.project.dto.JwtResponse;
import url_shortener.project.dto.LoginRequest;
import url_shortener.project.dto.RegisterRequest;
import url_shortener.project.entity.UserEntity;
import url_shortener.project.repository.UserRepository;
import url_shortener.project.security.JwtUtils;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {

        // Check karo ki email pehle se registered toh nahi hai
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email pehle se registered hai!");
        }

        // Naya user banao aur password ko encrypt (encode) karo
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User successfully register ho gaya!");
    }

    // 2. Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Email aur Password authenticate karo
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Token generate karo
        String email = authentication.getName();
        String jwt = tokenProvider.generateToken(email);

        // Response mein JWT token bhej do
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}