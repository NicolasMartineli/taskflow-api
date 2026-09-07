package com.nicolasmartineli.taskflow_api.mappers;

import com.nicolasmartineli.taskflow_api.dtos.TeamMembershipCreateRequest;
import com.nicolasmartineli.taskflow_api.models.Team;
import com.nicolasmartineli.taskflow_api.models.TeamMembership;
import com.nicolasmartineli.taskflow_api.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMemberShipMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "roleInTeam", source = "teamMembershipCreateRequest.roleInTeam")
    @Mapping(target = "team", source = "team")
    @Mapping(target = "user", source = "user")
    TeamMembership toEntity(
            TeamMembershipCreateRequest teamMembershipCreateRequest,
            Team team,
            User user);
}
