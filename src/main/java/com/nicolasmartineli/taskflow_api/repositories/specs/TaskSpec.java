package com.nicolasmartineli.taskflow_api.repositories.specs;

import com.nicolasmartineli.taskflow_api.models.Task;
import com.nicolasmartineli.taskflow_api.models.enums.TaskPriority;
import com.nicolasmartineli.taskflow_api.models.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TaskSpec {

    public static Specification<Task> projectIdEquals(UUID projectId) {
        return ((root, query, cb) -> {
            if (projectId == null) {
                return null;
            }

            return cb.equal(root.get("project").get("id"), projectId);
        });
    }

    public static Specification<Task> statusEqual(TaskStatus status) {
        return ((root, query, cb) -> {
            if (status == null) {
                return null;
            }

            return cb.equal(root.get("status"), status);
        });
    }

    public static Specification<Task> priorityEqual(TaskPriority priority) {
        return ((root, query, cb) -> {
            if (priority == null) {
                return null;
            }

            return cb.equal(root.get("priority"), priority);
        });
    }

    public static Specification<Task> assigneeIdEquals(UUID assigneeId) {
        return ((root, query, cb) -> {
            if (assigneeId == null) {
                return null;
            }

            return cb.equal(root.get("assignee").get("id"), assigneeId);
        });
    }


}


