package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.team.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Team toEntity(TeamCreateRequest teamCreateRequest);

    @Mapping(target = "members", ignore = true)
    TeamResponse toResponse(Team team);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateEntityFromRequest(TeamUpdateRequest teamUpdateRequest, @MappingTarget Team team);


}
