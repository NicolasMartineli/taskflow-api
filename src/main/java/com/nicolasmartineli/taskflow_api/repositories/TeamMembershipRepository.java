package com.nicolasmartineli.taskflow_api.repositories;

import com.nicolasmartineli.taskflow_api.models.TeamMembership;
import com.nicolasmartineli.taskflow_api.models.enums.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamMembershipRepository extends JpaRepository<TeamMembership, UUID> {

    boolean existsByTeamIdAndUserId(UUID teamId,UUID userId);

    Optional<TeamMembership> findByTeamIdAndUserId(UUID teamId, UUID userId);

    long countByTeamIdAndRoleInTeam(UUID teamId, TeamRole roleInTeam);
}
