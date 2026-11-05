package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.team.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.team.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.BusinessRuleException;
import com.nicolasmartineli.taskflow_api.exceptions.DuplicateResourceException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.TeamMapper;
import com.nicolasmartineli.taskflow_api.models.Team;
import com.nicolasmartineli.taskflow_api.models.TeamMembership;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import com.nicolasmartineli.taskflow_api.repositories.ProjectRepository;
import com.nicolasmartineli.taskflow_api.repositories.TeamMembershipRepository;
import com.nicolasmartineli.taskflow_api.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository repository;
    private final TeamMapper mapper;
    private final CurrentUserService currentUserService;
    private final TeamMembershipRepository teamMembershipRepository;
    private final ProjectRepository projectRepository;
    private final TeamAccessService teamAccessService;

    @Transactional
    public TeamResponse create(TeamCreateRequest request) {
        User creator = currentUserService.getCurrentUser();

        if (repository.existsByName(request.name())) {
            throw new DuplicateResourceException("Team", "name", request.name());
        }

        Team team = repository.save(mapper.toEntity(request));

        TeamMembership membership = new TeamMembership();
        membership.setTeam(team);
        membership.setUser(creator);
        membership.setRoleInTeam(TeamRole.ADMIN);
        teamMembershipRepository.save(membership);

        return mapper.toResponse(team, List.of(membership));
    }

    @Transactional(readOnly = true)
    public TeamResponse findById(UUID id) {
        return toResponse(findTeamOrThrow(id));
    }

    @Transactional
    public TeamResponse update(UUID id, TeamUpdateRequest request) {
        Team team = findTeamOrThrow(id);

        teamAccessService.checkAdmin(team.getId());

        if (!team.getName().equals(request.name()) && repository.existsByName(request.name())) {
            throw new DuplicateResourceException("Team", "name", request.name());
        }

        mapper.updateEntityFromRequest(request, team);

        return toResponse(repository.save(team));
    }

    @Transactional
    public void delete(UUID id) {
        Team team = findTeamOrThrow(id);

        teamAccessService.checkAdmin(team.getId());

        if (projectRepository.existsByTeamId(id)) {
            throw new BusinessRuleException("Cannot delete a team that still has projects");
        }

        teamMembershipRepository.deleteByTeamId(id);
        repository.delete(team);
    }

    private Team findTeamOrThrow(UUID id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        teamAccessService.checkMember(team.getId());

        return team;
    }

    private TeamResponse toResponse(Team team) {
        List<TeamMembership> memberships = teamMembershipRepository.findByTeamId(team.getId());
        return mapper.toResponse(team, memberships);
    }
}