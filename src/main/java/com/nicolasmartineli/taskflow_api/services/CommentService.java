package com.nicolasmartineli.taskflow_api.services;

import com.nicolasmartineli.taskflow_api.dtos.comment.CommentCreateRequest;
import com.nicolasmartineli.taskflow_api.dtos.comment.CommentResponse;
import com.nicolasmartineli.taskflow_api.exceptions.BusinessRuleException;
import com.nicolasmartineli.taskflow_api.exceptions.ResourceNotFoundException;
import com.nicolasmartineli.taskflow_api.mappers.CommentMapper;
import com.nicolasmartineli.taskflow_api.models.Comment;
import com.nicolasmartineli.taskflow_api.models.Task;
import com.nicolasmartineli.taskflow_api.models.User;
import com.nicolasmartineli.taskflow_api.repositories.CommentRepository;
import com.nicolasmartineli.taskflow_api.repositories.TaskRepository;
import com.nicolasmartineli.taskflow_api.repositories.TeamMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final CommentMapper mapper;
    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;
    private final TeamAccessService teamAccessService;

    @Transactional
    public CommentResponse create(UUID taskId, CommentCreateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        UUID teamId = task.getProject().getTeam().getId();

        teamAccessService.checkMember(teamId);
        if (!teamMembershipRepository.existsByTeamIdAndUserId(teamId, currentUserService.getCurrentUserId())) {
            throw new BusinessRuleException("Only team members can comment on tasks");
        }

        User author = currentUserService.getCurrentUser();

        Comment comment = mapper.toEntity(request);
        comment.setTask(task);
        comment.setAuthor(author);

        return mapper.toResponse(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByTaskId(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        teamAccessService.checkMember(task.getProject().getTeam().getId());

        return commentRepository.findByTaskId(id).stream().map(mapper::toResponse).toList();

    }

    @Transactional
    public void delete(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        User currentUser = currentUserService.getCurrentUser();
        UUID teamId = comment.getTask().getProject().getTeam().getId();

        boolean isAuthor = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = teamAccessService.isTeamAdmin(teamId);

        if (!isAuthor && !isAdmin) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }

        commentRepository.delete(comment);
    }

}

