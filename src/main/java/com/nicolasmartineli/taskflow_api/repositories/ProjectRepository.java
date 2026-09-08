package com.nicolasmartineli.taskflow_api.repositories;

import com.nicolasmartineli.taskflow_api.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    boolean existsByNameAndTeamId(String name, UUID teamId);

    Page<Project> findByTeamId(UUID teamId, Pageable pageable);

    boolean existsByNameAndTeamIdAndIdNot(String name, UUID teamId, UUID id);
}
