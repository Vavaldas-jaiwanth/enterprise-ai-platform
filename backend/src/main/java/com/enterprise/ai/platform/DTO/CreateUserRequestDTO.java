package com.enterprise.ai.platform.DTO;

import java.util.UUID;

public class CreateUserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private UUID departmentId;
    private String systemRole;

    public CreateUserRequestDTO()
    {

    }

    public CreateUserRequestDTO(String firstName, String lastName, String email, UUID departmentId, String systemRole)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.departmentId = departmentId;
        this.systemRole = systemRole;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public UUID getDepartmentId() { return departmentId; }
    public String getSystemRole() { return systemRole; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }
    public void setsystemRole(String systemRole) { this.systemRole = systemRole; }
}
