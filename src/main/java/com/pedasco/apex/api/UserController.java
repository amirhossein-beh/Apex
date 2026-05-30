package com.pedasco.apex.api;

import com.pedasco.apex.domain.entity.User;
import com.pedasco.apex.domain.enums.UserRole;
import com.pedasco.apex.dto.request.ChangePasswordRequest;
import com.pedasco.apex.dto.request.CreateUserRequest;
import com.pedasco.apex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(
                request.getUsername(),
                request.getPassword(),
                UserRole.valueOf(request.getRole())
        ));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changePassword(
            @PathVariable UUID id,
            @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(id, request.getNewPassword()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/{id}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> toggleActive(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.toggleActive(id));
    }
}
