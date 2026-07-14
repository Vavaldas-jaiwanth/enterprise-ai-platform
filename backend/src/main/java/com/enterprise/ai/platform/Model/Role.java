package com.enterprise.ai.platform.Model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Role {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Instant createdAt;

    Role()
    {

    }

    public Role(Long id, String name, Instant createdAt)
    {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
