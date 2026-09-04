package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.TeamCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.TeamResponse;
import com.nicolasmartineli.taskflow_api.dtos.TeamUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.DuplicateResourceException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.TeamMapper;
import com.nicolasmartineli.taskflow_api.models.Team;
import com.nicolasmartineli.taskflow_api.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository repository;
    private final TeamMapper mapper;

    public TeamResponse create(TeamCreateRequest request) {
        if (repository.existsByName(request.name())) {
            throw new DuplicateResourceException("Team", "name", request.name());
        }
        Team team = mapper.toEntity(request);

        team = repository.save(team);

        return mapper.toResponse(team);

    }

    public TeamResponse findById(UUID id) {

        return repository.findById(id)
                .map(mapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

    }

    public TeamResponse update(UUID id, TeamUpdateRequest request) {
        Team team = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        if (!team.getName().equals(request.name()) && repository.existsByName(request.name())) {
            throw new DuplicateResourceException("Team", "name", request.name());
        }

        mapper.updateEntityFromRequest(request, team);

        team = repository.save(team);

        return mapper.toResponse(team);
    }

    public void delete(UUID id) {
        Team team = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        repository.delete(team);
    }
}
