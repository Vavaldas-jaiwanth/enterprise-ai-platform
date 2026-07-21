package com.enterprise.ai.platform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.ai.platform.Model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, java.util.UUID> {
    Optional<Role> findByName(String name);
}
