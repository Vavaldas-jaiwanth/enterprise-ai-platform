package com.enterprise.ai.platform.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.enterprise.ai.platform.DTO.AuthenticatedUserDTO;
import com.enterprise.ai.platform.DTO.DepartmentDTO;
import com.enterprise.ai.platform.DTO.LoginRequestDTO;
import com.enterprise.ai.platform.DTO.LoginResponseDTO;
import com.enterprise.ai.platform.DTO.WorkspaceDTO;
import com.enterprise.ai.platform.Model.CustomUserDetails;
import com.enterprise.ai.platform.Model.Department;
import com.enterprise.ai.platform.Model.RefreshToken;
import com.enterprise.ai.platform.Model.User;
import com.enterprise.ai.platform.Model.WorkspaceMember;
import com.enterprise.ai.platform.Repository.UserRepository;
import com.enterprise.ai.platform.Repository.WorkspaceMemberRepository;
import com.enterprise.ai.platform.Utils.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceMemberRepository workspaceMemberRepository;

    @org.springframework.beans.factory.annotation.Value("${app.default.password}")
    private String defaultPassword;

    @org.springframework.transaction.annotation.Transactional
    public void forgotPassword(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setMustChangePassword(true);
            userRepository.save(user);
        });
    }

    @org.springframework.transaction.annotation.Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String jwtToken = jwtUtils.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

        AuthenticatedUserDTO authenticatedUserDTO = new AuthenticatedUserDTO();
        authenticatedUserDTO.setId(userDetails.getUser().getId());
        authenticatedUserDTO.setFirstName(userDetails.getUser().getFirstName());
        authenticatedUserDTO.setLastName(userDetails.getUser().getLastName());
        authenticatedUserDTO.setEmail(userDetails.getUser().getEmail());
        authenticatedUserDTO.setSystemRoles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        populateDepartmentAndWorkspaces(userDetails.getUser(), authenticatedUserDTO);

        long expiresIn = jwtUtils.getExpirationTime();

        return new LoginResponseDTO(
                jwtToken,
                refreshToken.getToken(),
                expiresIn,
                userDetails.getUser().isMustChangePassword(),
                authenticatedUserDTO
        );
    }

    @org.springframework.transaction.annotation.Transactional
    public AuthenticatedUserDTO me()
    {
        AuthenticatedUserDTO authUser = new AuthenticatedUserDTO();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!(principal instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated properly");
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        
        User freshUser = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        authUser.setId(freshUser.getId());
        authUser.setFirstName(freshUser.getFirstName());
        authUser.setLastName(freshUser.getLastName());
        authUser.setEmail(freshUser.getEmail());
        authUser.setSystemRoles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        populateDepartmentAndWorkspaces(freshUser, authUser);

        return authUser;
    }

    public void logout(String refreshToken)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!(principal instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated properly");
        }

        refreshTokenService.deleteByToken(refreshToken);
    }

    @org.springframework.transaction.annotation.Transactional
    public void changePassword(String oldPassword,String newPassword)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!(principal instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated properly");
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
        {
            throw new RuntimeException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);

        userRepository.save(user);
    }

    private void populateDepartmentAndWorkspaces(User user, AuthenticatedUserDTO authUser) {
        Department dept = user.getDepartment();
        if (dept != null) {
            DepartmentDTO deptDto = new DepartmentDTO();
            deptDto.setId(dept.getId());
            deptDto.setName(dept.getName());
            authUser.setDepartment(deptDto);
        } else {
            authUser.setDepartment(null);
        }

        List<WorkspaceMember> memberships = workspaceMemberRepository.findByUserId(user.getId());
        if (memberships != null && !memberships.isEmpty()) {
            List<WorkspaceDTO> workspaces = memberships.stream().map(m -> {
                WorkspaceDTO wDto = new WorkspaceDTO();
                if (m.getWorkspace() != null) {
                    wDto.setId(m.getWorkspace().getId());
                    wDto.setName(m.getWorkspace().getName());
                }
                if (m.getWorkspaceRole() != null) {
                    wDto.setWorkspaceRole(m.getWorkspaceRole().getName());
                }
                return wDto;
            }).collect(Collectors.toList());
            authUser.setWorkspaces(workspaces);
        } else {
            authUser.setWorkspaces(null);
        }
    }
}
