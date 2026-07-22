package com.enterprise.ai.platform.Specification;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.enterprise.ai.platform.Model.User;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterUsers(String search, UUID departmentId, Boolean enabled, Boolean mustChangePassword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchPattern);
                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern);
                Predicate emailMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern);
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch, emailMatch));
            }

            if (departmentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), departmentId));
            }

            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), enabled));
            }

            if (mustChangePassword != null) {
                predicates.add(criteriaBuilder.equal(root.get("mustChangePassword"), mustChangePassword));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
