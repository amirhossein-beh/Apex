package com.pedasco.apex.service;

import com.pedasco.apex.domain.entity.User;
import com.pedasco.apex.domain.enums.UserRole;
import com.pedasco.apex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;



    @Service
    @RequiredArgsConstructor
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public List<User> getAll() {
            return userRepository.findAll();
        }

        public User create(String username, String password, UserRole role) {
            if (userRepository.findByUsername(username).isPresent())
                throw new RuntimeException("Username already exists");

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            return userRepository.save(user);
        }

        public User changePassword(UUID userId, String newPassword) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        }

        public void delete(UUID userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!user.isDeletable())
                throw new RuntimeException("This user cannot be deleted");
            userRepository.delete(user);
        }

        public User toggleActive(UUID userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!user.isDeletable())
                throw new RuntimeException("Cannot deactivate admin user");
            user.setActive(!user.isActive());
            return userRepository.save(user);
        }
    }

