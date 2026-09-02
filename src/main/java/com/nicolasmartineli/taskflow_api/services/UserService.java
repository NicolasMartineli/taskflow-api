package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.UserResponse;
import com.nicolasmartineli.taskflow_api.dtos.UserUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.EmailAlreadyExistsException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.UserMapper;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.repositories.UserRepository;
import com.nicolasmartineli.taskflow_api.repositories.specs.UserSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final UserRepository repository;

    public UserResponse create(UserCreateRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("This email already exists");

        }
        User entity = mapper.toEntity(request);

        User user = repository.save(entity);

        return mapper.toResponse(user);

    }

    public UserResponse findById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return mapper.toResponse(user);
    }

    public Page<UserResponse> findByNameAndEmail(String name, String email, Pageable pageable) {

        Specification<User> specs = Specification.where((UserSpec.nameLike(name)).and(UserSpec.emailLike(email)));

        return repository.findAll(specs, pageable).map(user -> mapper.toResponse(user));

    }

    public UserResponse update(UUID id, UserUpdateRequest request) {

        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("This email already exists");

        }

        mapper.updateEntityFromRequest(request, user);
        repository.save(user);

        return mapper.toResponse(user);
    }

    public void delete(UUID id) {

        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        repository.delete(user);
    }

}
