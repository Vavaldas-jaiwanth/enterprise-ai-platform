package com.enterprise.ai.platform.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.enterprise.ai.platform.Model.Department;

import jakarta.persistence.criteria.Predicate;

public class DepartmentSpecification 
{
    public static Specification<Department> filterDepartments(Boolean active,String search)
    {
        return (root,query,criteriaBuilder)->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Predicate nameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                predicates.add(criteriaBuilder.or(nameMatch));
            }
            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }    
}
