package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.user.UserCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.user.UserResponse;
import com.nicolasmartineli.taskflow_api.dtos.user.UserSummary;
import com.nicolasmartineli.taskflow_api.dtos.user.UserUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserCreateRequest userCreateRequest);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);

    UserSummary toSummary(User user);
}
