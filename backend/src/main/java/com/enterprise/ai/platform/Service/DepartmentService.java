package com.enterprise.ai.platform.Service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.enterprise.ai.platform.DTO.DepartmentDTO;
import com.enterprise.ai.platform.DTO.DepartmentRequestDTO;
import com.enterprise.ai.platform.DTO.DepartmentResponseDTO;
import com.enterprise.ai.platform.DTO.UserResponseDTO;
import com.enterprise.ai.platform.Exception.ResourceAlreadyExistsException;
import com.enterprise.ai.platform.Exception.ResourceNotFoundException;
import com.enterprise.ai.platform.Model.Department;
import com.enterprise.ai.platform.Model.Role;
import com.enterprise.ai.platform.Model.User;
import com.enterprise.ai.platform.Model.UserRole;
import com.enterprise.ai.platform.Repository.DepartmentRepository;
import com.enterprise.ai.platform.Repository.UserRepository;
import com.enterprise.ai.platform.Specification.DepartmentSpecification;
import com.enterprise.ai.platform.Specification.UserSpecification;

@Service
public class DepartmentService 
{
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    public DepartmentResponseDTO create(DepartmentRequestDTO request)
    {
        if(departmentRepository.findByName(request.getName()).isPresent())
        {
            throw new ResourceAlreadyExistsException("Department with name " + request.getName() + " already exists");
        }
        
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setActive(true);
        department.setCreatedAt(Instant.now());
        department.setUpdatedAt(Instant.now());

        department = departmentRepository.save(department);

        return new DepartmentResponseDTO(department.getId(), department.getName(), department.getDescription(), department.isActive(),department.getCreatedAt());
    }

    public Page<DepartmentResponseDTO> getAll(Pageable pageable, Boolean active, String search)
    {
        Specification<Department> spec=DepartmentSpecification.filterDepartments(active,search);
        Page<Department> departmentPage=departmentRepository.findAll(spec,pageable);
        return departmentPage.map(department -> new DepartmentResponseDTO(department.getId(), department.getName(), department.getDescription(), department.isActive(),department.getCreatedAt()));
    }

    public DepartmentResponseDTO getById(UUID deptId)
    {
        Department department = departmentRepository.findById(deptId).orElseThrow(()->new ResourceNotFoundException("Department not found with id: "+deptId));
        return new DepartmentResponseDTO(department.getId(), department.getName(), department.getDescription(), department.isActive(),department.getCreatedAt());
    }

    public DepartmentResponseDTO update(UUID deptId,DepartmentRequestDTO request)   
    {
        Department department = departmentRepository.findById(deptId).orElseThrow(()->new ResourceNotFoundException("Department not found with id: "+deptId));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setUpdatedAt(Instant.now());
        departmentRepository.save(department);
        return new DepartmentResponseDTO(department.getId(), department.getName(), department.getDescription(), department.isActive(),department.getCreatedAt());
    }

    public void enable(UUID deptId)
    {
        Department department=departmentRepository.findById(deptId).orElseThrow(()->new ResourceNotFoundException("Department not found with id: "+deptId));
        department.setActive(true);
        department.setUpdatedAt(Instant.now());
        departmentRepository.save(department);
    }

    public void disable(UUID deptId)
    {
        Department department=departmentRepository.findById(deptId).orElseThrow(()->new ResourceNotFoundException("Department not found with id: "+deptId));
        department.setActive(false);
        department.setUpdatedAt(Instant.now());
        departmentRepository.save(department);
    }

    public Page<UserResponseDTO> getUsersByDepartmentId(UUID deptId,Pageable pageable)
    {
        Page<User> userPage=userRepository.findByDepartmentId(deptId,pageable);
        return userPage.map(user -> {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setEnabled(user.isEnabled());
        response.setMustChangePassword(user.isMustChangePassword());
        response.setCreatedAt(user.getCreatedAt());
        response.setDepartment(new DepartmentDTO(user.getDepartment().getId(),user.getDepartment().getName()));
        response.setRoles(user.getRoles().stream().map(UserRole::getRole).map(Role::getName).collect(java.util.stream.Collectors.toList()));
        return response;
        });
    }
}
