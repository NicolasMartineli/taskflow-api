package com.nicolasmartineli.taskflow_api.controllers;

import com.nicolasmartineli.taskflow_api.dtos.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.UserResponse;
import com.nicolasmartineli.taskflow_api.dtos.UserUpdateRequest;
import com.nicolasmartineli.taskflow_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {

        UserResponse user = service.findById(id);

        return ResponseEntity.ok(user);

    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> findByNameAndEmail(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        Page<UserResponse> byNameAndEmail = service.findByNameAndEmail(name, email, pageable);

        return ResponseEntity.ok(byNameAndEmail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest user) {

        service.update(id, user);

        return ResponseEntity.noContent().build();
    }

}
