package com.enterprise.ai.platform.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.ai.platform.Model.CustomUserDetails;
import com.enterprise.ai.platform.Model.User;
import com.enterprise.ai.platform.Model.UserRole;
import com.enterprise.ai.platform.Repository.UserRepository;
import com.enterprise.ai.platform.Repository.UserRoleRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());

        List<GrantedAuthority> authorities = userRoles.stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName().toUpperCase()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user, authorities);
    }
}
