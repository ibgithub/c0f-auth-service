package com.ib.auth.controller;

import com.ib.auth.dto.JwtResponse;
import com.ib.auth.dto.UserDto;
import com.ib.auth.entity.LoginRequest;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    @PostMapping("/login")
//    public UserResponse login(@RequestBody LoginRequest req) {
////        User user = authService.login(req.getUsername(), req.getPassword());
////        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
//        return authService.login(req);
//    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );
        return new JwtResponse(token);
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

