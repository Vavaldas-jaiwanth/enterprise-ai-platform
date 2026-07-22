package com.enterprise.ai.platform.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.enterprise.ai.platform.DTO.CreateUserRequestDTO;
import com.enterprise.ai.platform.DTO.UserResponseDTO;
import com.enterprise.ai.platform.DTO.DepartmentDTO;
import com.enterprise.ai.platform.DTO.UpdateProfileDTO;
import com.enterprise.ai.platform.Exception.ResourceAlreadyExistsException;
import com.enterprise.ai.platform.Exception.ResourceNotFoundException;
import com.enterprise.ai.platform.Model.Department;
import com.enterprise.ai.platform.Model.Role;
import com.enterprise.ai.platform.Model.User;
import com.enterprise.ai.platform.Model.UserRole;
import com.enterprise.ai.platform.Repository.DepartmentRepository;
import com.enterprise.ai.platform.Repository.RoleRepository;
import com.enterprise.ai.platform.Repository.UserRepository;
import com.enterprise.ai.platform.Repository.UserRoleRepository;
import com.enterprise.ai.platform.Specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

@Service
public class UserService 
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.default.password}")
    private String userPassword;

    public UserResponseDTO createUser(CreateUserRequestDTO request) 
    {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) 
        {
            throw new ResourceAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        User user=new User();
        UserResponseDTO response = new UserResponseDTO();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setEnabled(true);
        user.setMustChangePassword(true);
        user.setCreatedAt(java.time.Instant.now());
        user.setUpdatedAt(java.time.Instant.now());

        Role role = roleRepository.findByName(request.getSystemRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role '" + request.getSystemRole() + "' not found"));
        UserRole userRole = new UserRole(user, role);
        Department department = departmentRepository.findById(request.getDepartmentId())
                                            .orElseThrow(() -> new ResourceNotFoundException("Department with the id " + request.getDepartmentId() + " not found"));
        user.setDepartment(department);
        user.setRoles(List.of(userRole));
        userRepository.save(user);
        
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
    }

    public Page<UserResponseDTO> getAllUsers(String search, UUID departmentId, Boolean enabled, Boolean mustChangePassword, Pageable pageable)
    {
        Specification<User> spec = UserSpecification.filterUsers(search, departmentId, enabled, mustChangePassword);
        Page<User> userPage = userRepository.findAll(spec, pageable);
        
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

    public UserResponseDTO getUserById(UUID id)
    {
        User user = userRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("User with the id " + id + " not found"));
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
    }

    public void enableUser(UUID userId)
    {
        User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User with the id " + userId + " not found"));
        user.setEnabled(true);
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);
    }

    public void disableUser(UUID userId)
    {
        User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User with the id " + userId + " not found"));
        user.setEnabled(false);
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);
    }

    public UserResponseDTO updateUserProfile(UUID userId,UpdateProfileDTO request)
    {
        User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User with the id " + userId + " not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);
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
    }

    public UserResponseDTO changeUserDepartment(UUID userId,UUID departmentId)
    {
        User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User with the id " + userId + " not found"));
        Department department = departmentRepository.findById(departmentId)
                                            .orElseThrow(() -> new ResourceNotFoundException("Department with the id " + departmentId + " not found"));
        user.setDepartment(department);
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);
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
    }   

    public UserResponseDTO changeUserRole(UUID userId,List<String> roles)
    {
        User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("User with the id " + userId + " not found"));
        user.getRoles().clear();
        for(String role:roles)
        {
            Role r = roleRepository.findByName(role)
                                    .orElseThrow(() -> new ResourceNotFoundException("Role with the name " + role + " not found"));
            user.getRoles().add(new UserRole(user,r));
        }
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);
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
    }   
}
