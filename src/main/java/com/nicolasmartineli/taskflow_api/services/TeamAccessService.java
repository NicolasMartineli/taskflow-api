package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import com.nicolasmartineli.taskflow_api.repositories.TeamMembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamAccessService {

    private final TeamMembershipRepository teamMembershipRepository;
    private final CurrentUserService currentUserService;

    public boolean isMember(UUID teamId) {
        User user = currentUserService.getCurrentUser();
        return teamMembershipRepository.existsByTeamIdAndUserId(teamId, user.getId());
    }

    public void checkMember(UUID teamId) {
        User user = currentUserService.getCurrentUser();
        if (!teamMembershipRepository.existsByTeamIdAndUserId(teamId, user.getId())) {
            log.warn("Access denied: user is not a team member: userId={}, teamId={}", user.getId(), teamId);
            throw new ResourceNotFoundException("Team not found with id: " + teamId);
        }
    }

    public boolean isTeamAdmin(UUID teamId) {
        User user = currentUserService.getCurrentUser();
        return teamMembershipRepository.findByTeamIdAndUserId(teamId, user.getId())
                .map(m -> m.getRoleInTeam() == TeamRole.ADMIN)
                .orElse(false);
    }

    public void checkAdmin(UUID teamId) {
        User user = currentUserService.getCurrentUser();
        boolean isAdmin = teamMembershipRepository.findByTeamIdAndUserId(teamId, user.getId())
                .map(m -> m.getRoleInTeam() == TeamRole.ADMIN)
                .orElse(false);

        if (!isAdmin) {
            log.warn("Access denied: user is not a team admin: userId={}, teamId={}", user.getId(), teamId);
            throw new ResourceNotFoundException("Team not found with id: " + teamId);
        }
    }

    public void checkSelfAccess(UUID targetUserId) {
        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(targetUserId)) {
            log.warn("Access denied: user attempted cross-user operation: userId={}, targetUserId={}",
                    currentUser.getId(), targetUserId);
            throw new ResourceNotFoundException("User not found with id: " + targetUserId);
        }
    }
}