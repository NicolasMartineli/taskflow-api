package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.UserResponse;
import com.nicolasmartineli.taskflow_api.dtos.UserUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserCreateRequest userCreateRequest);

    UserResponse toResponse(User user);

    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);

    List<UserResponse> toListResponse(List<User> users);
}
