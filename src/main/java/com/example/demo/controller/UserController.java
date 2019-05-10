package com.example.demo.controller;

import com.example.demo.jwt.JwtAuthenticationRequest;
import com.example.demo.jwt.JwtAuthenticationResponse;
import com.example.demo.jwt.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@RestController
@Api(value = "Users", description = "REST API for Users", tags = { "Users" })
public class UserController {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, @Qualifier("currentUserDetailsService") UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/auth")
    @ApiOperation(value = "User authorization")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authorization"),
            @ApiResponse(code = 401, message = "User UNAUTHORIZED")})
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(
                JwtAuthenticationResponse
                        .builder()
                        .username(userDetails.getUsername())
                        .token(token)
                        .build());
    }

    @GetMapping("/test")

    public CompletableFuture<ResponseEntity<?>> test() {
        System.out.println("method was started " + LocalTime.now());
        try {
            Thread.sleep(10000);         // demonstrate long execution
        } catch (InterruptedException ignored) {
        }
        System.out.println("method was finished " + LocalTime.now());
        return CompletableFuture.completedFuture(ResponseEntity.ok("test"));
    }



}
