package com.example.demo.controller;

import com.example.demo.jwt.JwtAuthenticationResponse;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.jwt.JwtAuthenticationRequest;
import com.example.demo.service.CompletableFutureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@Api(value = "Users", description = "REST API for Users", tags = { "Users" })
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    private final CompletableFutureService completableFutureService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, @Qualifier("currentUserDetailsService") UserDetailsService userDetailsService, CompletableFutureService completableFutureService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.completableFutureService = completableFutureService;
    }

    @PostMapping(value = "/auth")
    @ApiOperation(value = "User authorization")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authorization"),
            @ApiResponse(code = 401, message = "User UNAUTHORIZED")})
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(
                JwtAuthenticationResponse
                        .builder()
                        .username(userDetails.getUsername())
                        .token(token)
                        .build());
    }

    @GetMapping("/sayHello")
    public ResponseEntity<?> sayHello() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = completableFutureService.sayHello();
        String s = completableFuture.get();
        completableFuture.thenRun(() -> System.out.println("method was finished : " + new Date()));
        return ResponseEntity.ok(s);
    }


}
