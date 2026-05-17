// api/AuthController.java
package com.pedasco.apex.api;

import com.pedasco.apex.dto.request.LoginRequest;
import com.pedasco.apex.dto.response.LoginResponse;
import com.pedasco.apex.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // یوزرنیم و پسورد رو verify میکنه
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // role رو از authentication میگیره
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_VIEWER")
                .replace("ROLE_", "");

        String token = jwtUtil.generateToken(authentication.getName(), role);

        return ResponseEntity.ok(new LoginResponse(token, authentication.getName(), role));
    }
}