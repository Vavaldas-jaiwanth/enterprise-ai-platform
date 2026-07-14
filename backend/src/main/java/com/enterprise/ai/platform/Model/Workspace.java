package com.enterprise.ai.platform.Model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false)
    private String name;

    private String description;

    private Boolean isPublic;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;

    public Workspace() {
    }

    public Workspace(String name, String description, Boolean isPublic, Boolean isActive, Instant createdAt, Instant updatedAt) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Boolean getIsPublic() { return isPublic; }
    public Boolean getIsActive() { return isActive; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Workspace [id=" + id + ", name=" + name + ", description=" + description + ", isPublic=" + isPublic
                + ", isActive=" + isActive + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
    
}
