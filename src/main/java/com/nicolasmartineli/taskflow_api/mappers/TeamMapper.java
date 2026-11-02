package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.team.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamMemberResponse;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.models.Team;
import com.nicolasmartineli.taskflow_api.models.TeamMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Team toEntity(TeamCreateRequest teamCreateRequest);

    @Mapping(target = "id", source = "team.id")
    @Mapping(target = "name", source = "team.name")
    @Mapping(target = "members", source = "memberships")
    TeamResponse toResponse(Team team, List<TeamMembership> memberships);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    TeamMemberResponse toMemberResponse(TeamMembership membership);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    void updateEntityFromRequest(TeamUpdateRequest teamUpdateRequest, @MappingTarget Team team);
}