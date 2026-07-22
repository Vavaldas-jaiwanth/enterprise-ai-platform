package com.enterprise.ai.platform.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.ai.platform.DTO.CreateUserRequestDTO;
import com.enterprise.ai.platform.DTO.UpdateProfileDTO;
import com.enterprise.ai.platform.DTO.UserResponseDTO;
import com.enterprise.ai.platform.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/")
    public ResponseEntity<UserResponseDTO> createuser(@RequestBody CreateUserRequestDTO request)
    {
    
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) UUID departmentId,
        @RequestParam(required = false) Boolean enabled,
        @RequestParam(required = false) Boolean mustChangePassword,
        @PageableDefault(size = 10) Pageable pageable
    )
    {
        return ResponseEntity.ok(userService.getAllUsers(search, departmentId, enabled, mustChangePassword, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id)
    {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{userId}/enable")
    public ResponseEntity<?> enableUser(@PathVariable UUID userId)
    {
        userService.enableUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/disable")
    public ResponseEntity<?> disableUser(@PathVariable UUID userId)
    {
        userService.disableUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable UUID userId,@RequestBody UpdateProfileDTO request)
    {
        return ResponseEntity.ok(userService.updateUserProfile(userId,request));
    }

    @PatchMapping("/{userId}/departments")
    public ResponseEntity<UserResponseDTO> changeUserDepartment(@PathVariable UUID userId,@RequestBody java.util.Map<String, UUID> body)
    {
        return ResponseEntity.ok(userService.changeUserDepartment(userId, body.get("departmentId")));
    }
    
    @PatchMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> changeUserRole(@PathVariable UUID userId,@RequestBody java.util.Map<String, java.util.List<String>> body)
    {
        return ResponseEntity.ok(userService.changeUserRole(userId, body.get("roles")));
    }
    
}
