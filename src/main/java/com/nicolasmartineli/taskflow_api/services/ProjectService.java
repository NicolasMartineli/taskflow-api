package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.project.ProjectCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectResponse;
import com.nicolasmartineli.taskflow_api.dtos.project.ProjectUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.DuplicateResourceException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.ProjectMapper;
import com.nicolasmartineli.taskflow_api.models.Project;
import com.nicolasmartineli.taskflow_api.models.Team;
import com.nicolasmartineli.taskflow_api.repositories.ProjectRepository;
import com.nicolasmartineli.taskflow_api.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final TeamRepository teamRepository;
    private final ProjectMapper mapper;
    private final ProjectRepository repository;

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + request.teamId()));

        if (repository.existsByNameAndTeamId(request.name(), request.teamId())) {
            throw new DuplicateResourceException("Project", "name", request.name());
        }
        Project project = mapper.toEntity(request);
        project.setTeam(team);

        Project save = repository.save(project);

        return mapper.toResponse(save);


    }

    @Transactional
    public ProjectResponse findById(UUID idProject) {

        Project project = repository.findById(idProject)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + idProject));

        return mapper.toResponse(project);
    }

    @Transactional
    public Page<ProjectResponse> findByTeamId(UUID teamId, Pageable pageable) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResourceNotFoundException("Team not found with id: " + teamId);
        }

        return repository.findByTeamId(teamId, pageable).map(mapper::toResponse);

    }

    @Transactional
    public ProjectResponse update(UUID idProject, ProjectUpdateRequest request) {

        Project project = repository.findById(idProject)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + idProject));

        if (repository.existsByNameAndTeamIdAndIdNot(request.name(), project.getTeam().getId(), idProject)) {
            throw new DuplicateResourceException("Project", "name", request.name());
        }

        mapper.updateEntityFromRequest(request, project);

        Project saveProject = repository.save(project);

        return mapper.toResponse(saveProject);
    }

    @Transactional
    public void delete(UUID idProject) {
        Project project = repository.findById(idProject)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + idProject));

        repository.delete(project);
    }
}
