package com.nicolasmartineli.taskflow_api.repositories.specs;

import com.nicolasmartineli.taskflow_api.models.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpec {

    public static Specification<User> nameLike(String name) {

        return ((root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return null;
            }
            return cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
        });

    }

    public static Specification<User> emailLike(String email) {

        return ((root, query, cb) -> {
            if (email == null || email.isBlank()) {
                return null;
            }
            return cb.like(cb.upper(root.get("email")), "%" + email.toUpperCase() + "%");
        });
    }
}
