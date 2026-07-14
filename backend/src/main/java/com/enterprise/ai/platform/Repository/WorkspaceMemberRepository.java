package com.enterprise.ai.platform.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.ai.platform.Model.WorkspaceMember;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {
    List<WorkspaceMember> findByUserId(UUID userId);
}
