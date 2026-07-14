package com.enterprise.ai.platform.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enterprise.ai.platform.DTO.AuthenticatedUserDTO;
import com.enterprise.ai.platform.DTO.LoginRequestDTO;
import com.enterprise.ai.platform.DTO.LoginResponseDTO;
import com.enterprise.ai.platform.DTO.TokenRefreshRequestDTO;
import com.enterprise.ai.platform.DTO.TokenRefreshResponseDTO;
import com.enterprise.ai.platform.Model.CustomUserDetails;
import com.enterprise.ai.platform.Model.RefreshToken;
import com.enterprise.ai.platform.Service.AuthService;
import com.enterprise.ai.platform.Service.CustomUserDetailsService;
import com.enterprise.ai.platform.Service.RefreshTokenService;
import com.enterprise.ai.platform.Utils.JwtUtils;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDTO> refresh(@RequestBody TokenRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
        String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new TokenRefreshResponseDTO(token, requestRefreshToken));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDTO> auth_me()
    {
        return ResponseEntity.ok(authService.me());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRefreshRequestDTO request)
    {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logout successful");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody com.enterprise.ai.platform.DTO.ChangePasswordRequestDTO request)
    {
        authService.changePassword(request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}