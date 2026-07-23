package com.enterprise.ai.platform.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.enterprise.ai.platform.Model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID>,JpaSpecificationExecutor<Department> {
    Optional<Department> findByName(String name);
}
