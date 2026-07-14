package com.enterprise.ai.platform.DTO;

import java.util.UUID;

public class DepartmentDTO {
    
    private UUID id;
    private String name;

    public DepartmentDTO()
    {

    }

    public DepartmentDTO(UUID id,String name)
    {
        this.id=id;
        this.name=name;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
