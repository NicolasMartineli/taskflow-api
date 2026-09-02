package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.UserResponse;
import com.nicolasmartineli.taskflow_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements LocationHeaderUriBuilder {

    private final UserService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateRequest user) {

        UserResponse userResponse = service.create(user);

        URI location = buildLocationUri(userResponse.id());

        return ResponseEntity.created(location).build();

    }
}
