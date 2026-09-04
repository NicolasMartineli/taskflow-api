package com.nicolasmartineli.taskflow_api.repositories;

import com.nicolasmartineli.taskflow_api.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {


    boolean existsByName(String name);
}
