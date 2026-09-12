package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.task.TaskCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskResponse;
import com.nicolasmartineli.taskflow_api.dtos.task.TaskUpdateRequest;
import com.nicolasmartineli.taskflow_api.exceptions.BusinessRuleException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.TaskMapper;
import com.nicolasmartineli.taskflow_api.models.Project;
import com.nicolasmartineli.taskflow_api.models.Task;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;
import com.nicolasmartineli.taskflow_api.repositories.ProjectRepository;
import com.nicolasmartineli.taskflow_api.repositories.TaskRepository;
import com.nicolasmartineli.taskflow_api.repositories.TeamMembershipRepository;
import com.nicolasmartineli.taskflow_api.repositories.UserRepository;
import com.nicolasmartineli.taskflow_api.repositories.specs.TaskSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final TeamMembershipRepository teamMembershipRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse create(TaskCreateRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.projectId()));


        validateAssignee(request.assigneeId(), project.getTeam().getId());

        Task task = mapper.toEntity(request);
        task.setProject(project);

        if (request.assigneeId() != null) {
            User user = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.assigneeId()));

            task.setAssignee(user);
        }
        if (request.status() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        return mapper.toResponse(taskRepository.save(task));

    }

    public void validateAssignee(UUID assigneId, UUID teamId) {
        if (assigneId != null && !teamMembershipRepository.existsByTeamIdAndUserId(teamId, assigneId)) {
            throw new BusinessRuleException("Assignee must be a member of the project's team");
        }
    }

    @Transactional
    public TaskResponse findById(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));


        return mapper.toResponse(task);
    }

    @Transactional
    public TaskResponse update(UUID id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (request.assigneeId() != null) {
            validateAssignee(request.assigneeId(), task.getProject().getTeam().getId());

            User user = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.assigneeId()));
            task.setAssignee(user);
        }

        mapper.updateEntityFromRequest(request, task);

        return mapper.toResponse(taskRepository.save(task));

    }

    @Transactional
    public void delete(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);

    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> findAll(UUID projectId, TaskStatus status, TaskPriority priority, UUID assigneeId, Pageable pageable) {

        Specification<Task> specs = Specification.where
                        (TaskSpec.assigneeIdEquals(assigneeId))
                .and(TaskSpec.priorityEqual(priority))
                .and(TaskSpec.projectIdEquals(projectId))
                .and(TaskSpec.statusEqual(status));

        return taskRepository.findAll(specs, pageable).map(mapper::toResponse);
    }
}
