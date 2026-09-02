package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.UserResponse;
import com.nicolasmartineli.taskflow_api.dtos.UserUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.EmailAlreadyExistsException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.UserMapper;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UserResponse> findAll() {
        List<User> all = repository.findAll();

        return mapper.toListResponse(all);
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
