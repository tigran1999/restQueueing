package com.example.demo.controller;

import com.example.demo.jwt.JwtAuthenticationRequest;
import com.example.demo.jwt.JwtAuthenticationResponse;
import com.example.demo.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, @Qualifier("currentUserDetailsService") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong email or password");
        }
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(
                JwtAuthenticationResponse
                        .builder()
                        .username(userDetails.getUsername())
                        .token(token)
                        .build());
    }




}
