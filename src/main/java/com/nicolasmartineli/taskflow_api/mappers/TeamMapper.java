package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {

    Team toEntity(TeamCreateRequest teamCreateRequest);

    @Mapping(target = "members", ignore = true)
    TeamResponse toResponse(Team team);

    void updateEntityFromRequest(TeamUpdateRequest teamUpdateRequest, @MappingTarget Team team);


}
