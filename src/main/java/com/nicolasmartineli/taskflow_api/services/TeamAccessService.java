package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import com.nicolasmartineli.taskflow_api.repositories.TeamMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamAccessService {

    private final TeamMembershipRepository teamMembershipRepository;
    private final CurrentUserService currentUserService;

    public Boolean isMember(UUID teamId) {
        User user = currentUserService.getCurrentUser();
        return teamMembershipRepository.existsByTeamIdAndUserId(teamId, user.getId());

    }
    public void checkMember(UUID teamId){
        if(!isMember(teamId)){
            throw new ResourceNotFoundException("You don't have access to this team.");
        }
    }

    public Boolean isTeamAdmin(UUID teamId){
        User user = currentUserService.getCurrentUser();
        return teamMembershipRepository.findByTeamIdAndUserId(teamId, user.getId())
                .map(m -> m.getRoleInTeam() == TeamRole.ADMIN)
                .orElse(false);
    }

    public void checkAdmin(UUID teamId){
        if(!isTeamAdmin(teamId)){
            throw new ResourceNotFoundException("Team not found with id: " + teamId);
        }
    }

    public void checkSelfAccess(UUID targetUserId) {
        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(targetUserId)) {
            throw new ResourceNotFoundException("User not found with id: " + targetUserId);
        }
    }
}
