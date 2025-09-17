package com.booking.service;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.booking.config.JwtUtil;
import com.booking.dto.LoginRequest;
import com.booking.dto.LoginResponse;
import com.booking.entity.User;
import com.booking.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public AuthService(AuthenticationManager authManager, JwtUtil jwtUtil, UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    public LoginResponse login(LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );

            String username = auth.getName();
            List<String> roles = auth.getAuthorities()
                                     .stream()
                                     .map(GrantedAuthority::getAuthority)
                                     .collect(Collectors.toList());

            User user = userRepo.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(username, roles);

            // expiration can be hardcoded or obtained from jwtUtil
            return new LoginResponse(token, 0);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
