package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user");
        }

        return userRepository.findByEmail(jwt.getSubject())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        "Authenticated user no longer exists"));
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
