package com.ib.auth.controller;

import com.ib.auth.entity.LoginRequest;
import com.ib.auth.entity.User;
import com.ib.auth.entity.UserResponse;
import com.ib.auth.repository.UserRepository;
import com.ib.auth.security.JwtUtil;
import com.ib.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public AuthController(AuthService authService, AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

//    @PostMapping("/login")
//    public UserResponse login(@RequestBody LoginRequest req) {
////        User user = authService.login(req.getUsername(), req.getPassword());
////        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
//        return authService.login(req);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User user = userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(
                Map.of("token", token)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        return ResponseEntity.ok(
                Map.of(
                        "username", authentication.getName(),
                        "authorities", authentication.getAuthorities()
                )
        );
    }
}

