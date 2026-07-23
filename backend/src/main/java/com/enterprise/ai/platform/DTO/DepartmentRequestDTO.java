package com.enterprise.ai.platform.DTO;

public class DepartmentRequestDTO {
    
    private String name;
    private String description;
    public DepartmentRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public DepartmentRequestDTO() {
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
}
