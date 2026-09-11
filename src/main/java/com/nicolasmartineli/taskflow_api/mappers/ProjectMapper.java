package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.project.ProjectCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectResponse;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectSummary;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {

    ProjectResponse toResponse(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "team", ignore = true)
    Project toEntity(ProjectCreateRequest projectCreateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "team", ignore = true)
    void updateEntityFromRequest(ProjectUpdateRequest projectUpdateRequest, @MappingTarget Project project);

    ProjectSummary toSummary(Project project);
}
