package com.enterprise.ai.platform.DTO;

import java.util.UUID;

public class WorkspaceDTO {
    
    private UUID id;
    private String name;
    private String workspaceRole;

    public WorkspaceDTO()
    {

    }

    public WorkspaceDTO(UUID id, String name,String workspaceRole)
    {
        this.id=id;
        this.name=name;
        this.workspaceRole=workspaceRole;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getWorkspaceRole() { return workspaceRole; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setWorkspaceRole(String workspaceRole) { this.workspaceRole = workspaceRole; }

    @Override
    public String toString() {
        return "WorkspaceDTO [id=" + id + ", name=" + name + ", workspaceRole=" + workspaceRole + "]";
    }
    
}
