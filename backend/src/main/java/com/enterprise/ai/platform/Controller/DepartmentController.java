package com.enterprise.ai.platform.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.ai.platform.DTO.DepartmentRequestDTO;
import com.enterprise.ai.platform.DTO.DepartmentResponseDTO;
import com.enterprise.ai.platform.DTO.UserResponseDTO;
import com.enterprise.ai.platform.Service.DepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController 
{

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentRequestDTO request)
    {
        return ResponseEntity.ok(departmentService.create(request));   
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<DepartmentResponseDTO>> getAllDepartments
    (
        @RequestParam(required=false)Boolean active,
        @RequestParam(required=false)String search,
        @PageableDefault(size = 10) Pageable pageable
    )
    {
        return ResponseEntity.ok(departmentService.getAll(pageable,active,search));
    }

    @GetMapping("/{deptId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and principal.user.department.id == #deptId)")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable UUID deptId)
    {
        return ResponseEntity.ok(departmentService.getById(deptId));
    }

    @PutMapping("/{deptId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and principal.user.department.id == #deptId)")
    public ResponseEntity<DepartmentResponseDTO> updateDepartmentById(@PathVariable UUID deptId,@RequestBody DepartmentRequestDTO request)
    {
        return ResponseEntity.ok(departmentService.update(deptId,request));
    }

    @PatchMapping("/{deptId}/enable")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and principal.user.department.id == #deptId)")
    public ResponseEntity<?> enableDepartment(@PathVariable UUID deptId)
    {
        departmentService.enable(deptId);
        return ResponseEntity.noContent().build();
    }   

    @PatchMapping("/{deptId}/disable")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and principal.user.department.id == #deptId)")
    public ResponseEntity<?> disableDepartment(@PathVariable UUID deptId)
    {
        departmentService.disable(deptId);
        return ResponseEntity.noContent().build();
    }   

    @GetMapping("/{deptId}/users")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and principal.user.department.id == #deptId)")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByDepartmentId(@PathVariable UUID deptId,@PageableDefault(size = 10) Pageable pageable)
    {
        return ResponseEntity.ok(departmentService.getUsersByDepartmentId(deptId,pageable));
    }
}
