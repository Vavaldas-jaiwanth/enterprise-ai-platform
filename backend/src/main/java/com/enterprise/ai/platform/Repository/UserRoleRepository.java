package com.enterprise.ai.platform.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.ai.platform.Model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUserId(UUID userId);
}
