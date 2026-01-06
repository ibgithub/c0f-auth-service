package com.ib.auth.service;

import com.ib.auth.entity.LoginRequest;
import com.ib.auth.entity.User;
import com.ib.auth.entity.UserResponse;
import com.ib.auth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public User login(String username, String rawPassword) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//        return user;
//    }
    public UserResponse login(LoginRequest request) {

        System.out.println(">>> AUTH SERVICE LOGIN");

        Optional<User> user = userRepository.findByUsername(request.getUsername());
//                .orElseThrow(() -> {
//                    System.out.println(">>> USER NOT FOUND");
//                    return new BadCredentialsException("Invalid username/password");
//                });
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        System.out.println(">>> USER FOUND");
        System.out.println(">>> DB PASSWORD = " + user.get().getPassword());

        boolean match = passwordEncoder.matches(
                request.getPassword(),
                user.get().getPassword()
        );

        System.out.println(">>> PASSWORD MATCH = " + match);

        if (!match) {
            throw new BadCredentialsException("Invalid username/password");
        }

        return new UserResponse(user.get().getId(), user.get().getUsername(), user.get().getRole());
    }
}
