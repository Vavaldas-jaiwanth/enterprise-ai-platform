package com.enterprise.ai.platform.DTO;

import java.util.List;
import java.util.UUID;


public class AuthenticatedUserDTO {
    
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private DepartmentDTO department;
    private List<String> systemRoles;
    private List<WorkspaceDTO> workspaces;

    public AuthenticatedUserDTO()
    {

    }

    AuthenticatedUserDTO(UUID id, String firstName, String lastName, String email, DepartmentDTO department, List<String> systemRoles, List<WorkspaceDTO> workspaces)
    {
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.department=department;
        this.systemRoles=systemRoles;
        this.workspaces=workspaces;
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public DepartmentDTO getDepartment() { return department; }
    public List<String> getSystemRoles() { return systemRoles; }
    public List<WorkspaceDTO> getWorkspaces() { return workspaces; }

    public void setId(UUID id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(DepartmentDTO department) { this.department = department; }
    public void setSystemRoles(List<String> systemRoles) { this.systemRoles = systemRoles; }
    public void setWorkspaces(List<WorkspaceDTO> workspaces) { this.workspaces = workspaces; }

    @Override
    public String toString() {
        return "AuthenticatedUserDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", department=" + department + ", systemRoles=" + systemRoles + ", workspaces=" + workspaces + "]";
    }
    
}
