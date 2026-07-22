package com.enterprise.ai.platform.Config;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.ai.platform.Model.Department;
import com.enterprise.ai.platform.Model.Role;
import com.enterprise.ai.platform.Model.User;
import com.enterprise.ai.platform.Model.UserRole;
import com.enterprise.ai.platform.Repository.DepartmentRepository;
import com.enterprise.ai.platform.Repository.RoleRepository;
import com.enterprise.ai.platform.Repository.UserRepository;
import com.enterprise.ai.platform.Repository.UserRoleRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // 1. Seed Roles
        Role superAdminRole = seedRole("SUPER_ADMIN");
        Role adminRole = seedRole("ADMIN");
        Role userRole = seedRole("USER");

        // 2. Seed a Default Department (Required for User)
        Department defaultDept = departmentRepository.findByName("Administration")
            .orElseGet(() -> {
                Department dept = new Department();
                dept.setName("Administration");
                dept.setDescription("System Administration Department");
                dept.setActive(true);
                dept.setCreatedAt(Instant.now());
                dept.setUpdatedAt(Instant.now());
                return departmentRepository.save(dept);
            });

        // Seed an extra department for testing
        departmentRepository.findByName("Engineering")
            .orElseGet(() -> {
                Department dept = new Department();
                dept.setName("Engineering");
                dept.setDescription("Software Engineering Department");
                dept.setActive(true);
                dept.setCreatedAt(Instant.now());
                dept.setUpdatedAt(Instant.now());
                return departmentRepository.save(dept);
            });

        // 3. Seed Super Admin User
        

        Optional<User> existingUser = userRepository.findByEmail(adminEmail);
        
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setFirstName("Super");
            user.setLastName("Admin");
            user.setEmail(adminEmail);
            user.setPassword(passwordEncoder.encode(adminPassword)); // Default password
            user.setEnabled(true);
            user.setMustChangePassword(true);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            user.setDepartment(defaultDept);
            
            user = userRepository.save(user);

            // 4. Assign Super_Admin role
            UserRole userRoleMapping = new UserRole();
            userRoleMapping.setUser(user);
            userRoleMapping.setRole(superAdminRole);
            userRoleMapping.setAssignedAt(Instant.now());
            
            userRoleRepository.save(userRoleMapping);
            
            System.out.println("Super Admin user created: " + adminEmail + " / " + adminPassword);
        }
    }

    private Role seedRole(String name) {
        return roleRepository.findByName(name)
            .orElseGet(() -> {
                Role role = new Role(null, name, Instant.now());
                return roleRepository.save(role);
            });
    }
}
