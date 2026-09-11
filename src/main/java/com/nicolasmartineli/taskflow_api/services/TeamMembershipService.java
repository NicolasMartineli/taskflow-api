package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.teammembership.TeamMembershipCreateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.BusinessRuleException;
import com.nicolasmartineli.taskflow_api.exceptions.DuplicateResourceException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.TeamMemberShipMapper;
import com.nicolasmartineli.taskflow_api.models.Team;
import com.nicolasmartineli.taskflow_api.models.TeamMembership;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import com.nicolasmartineli.taskflow_api.repositories.TeamMembershipRepository;
import com.nicolasmartineli.taskflow_api.repositories.TeamRepository;
import com.nicolasmartineli.taskflow_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamMembershipService {

    private final TeamMemberShipMapper mapper;
    private final TeamMembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void addMember(UUID teamId, TeamMembershipCreateRequest request) {

        if (membershipRepository.existsByTeamIdAndUserId(teamId, request.userId())) {
            throw new DuplicateResourceException("TeamMembership", "userId", request.userId());

        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        TeamMembership membership = mapper.toEntity(request, team, user);

        membershipRepository.save(membership);

    }
    @Transactional
    public void removeMember(UUID teamId, UUID idUser) {
        TeamMembership membership = membershipRepository.findByTeamIdAndUserId(teamId, idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Team or user not found with id"));

        boolean isLastAdmin = membership.getRoleInTeam() == TeamRole.ADMIN
                && membershipRepository.countByTeamIdAndRoleInTeam(teamId, TeamRole.ADMIN) <= 1;

        if (isLastAdmin) {
            throw new BusinessRuleException("Cannot remove the last ADMIN of the team");

        }
        membershipRepository.delete(membership);

    }

}


