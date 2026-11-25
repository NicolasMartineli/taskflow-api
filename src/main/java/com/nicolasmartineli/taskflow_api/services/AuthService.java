package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.login.LoginRequest;
import com.nicolasmartineli.taskflow_api.dtos.login.LoginResponse;
import com.nicolasmartineli.taskflow_api.security.CustomUserDetails;
import com.nicolasmartineli.taskflow_api.security.JwtService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MeterRegistry meterRegistry;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            meterRegistry.counter("taskflow.login.attempts", "result", "failure").increment();
            throw ex;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Jwt jwt = jwtService.generateToken(userDetails);

        long expiresIn = Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).getSeconds();

        meterRegistry.counter("taskflow.login.attempts", "result", "success").increment();
        log.info("User authenticated: userId={}", userDetails.getUser().getId());

        return new LoginResponse(jwt.getTokenValue(), "Bearer", expiresIn);
    }

}