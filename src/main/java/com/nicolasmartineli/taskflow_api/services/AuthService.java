package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.login.LoginRequest;
import com.nicolasmartineli.taskflow_api.dtos.login.LoginResponse;
import com.nicolasmartineli.taskflow_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Jwt jwt = jwtService.generateToken(userDetails);

        long expiresIn = Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).getSeconds();

        return new LoginResponse(jwt.getTokenValue(), "Bearer", expiresIn);
    }

}
