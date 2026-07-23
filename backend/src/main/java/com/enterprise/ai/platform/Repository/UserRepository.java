package com.enterprise.ai.platform.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.ai.platform.Model.User;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface UserRepository extends JpaRepository<User,UUID>, JpaSpecificationExecutor<User> { 

    Optional<User> findByEmail(String email);

    org.springframework.data.domain.Page<User> findByDepartmentId(UUID departmentId, org.springframework.data.domain.Pageable pageable);
}
