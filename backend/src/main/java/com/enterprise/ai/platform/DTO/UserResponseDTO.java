package com.enterprise.ai.platform.DTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserResponseDTO 
{
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private boolean mustChangePassword;
    private DepartmentDTO department;
    private List<String> roles;
    private Instant createdAt;

    public UserResponseDTO() {
    }

    public UserResponseDTO(UUID id, String firstName, String lastName, String email, boolean enabled, boolean mustChangePassword, DepartmentDTO department, List<String> roles, Instant createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
        this.mustChangePassword = mustChangePassword;
        this.department = department;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean isMustChangePassword() {
        return mustChangePassword;
    }
    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }
    public DepartmentDTO getDepartment() {
        return department;
    }
    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}