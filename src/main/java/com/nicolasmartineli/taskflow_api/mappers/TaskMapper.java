package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.task.TaskCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.Task;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    Task toEntity(TaskCreateRequest taskCreateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    void updateEntityFromRequest(TaskUpdateRequest taskUpdateRequest, @MappingTarget Task task);

    TaskResponse toResponse(Task task);

}
