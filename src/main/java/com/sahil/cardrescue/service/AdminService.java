package com.sahil.cardrescue.service;

import com.sahil.cardrescue.dto.UserResponse;
import com.sahil.cardrescue.dto.UserUpdateRequest;
import com.sahil.cardrescue.entity.User;
import com.sahil.cardrescue.repository.FoundCardRepository;
import com.sahil.cardrescue.repository.LostCardRepository;
import com.sahil.cardrescue.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoundCardRepository foundCardRepository;

    @Autowired
    private LostCardRepository lostCardRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Transactional
    public String deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (currentUser.getId().equals(id)) {
            throw new RuntimeException("Admin cannot delete own account");
        }

        foundCardRepository.deleteByUserId(id);
        lostCardRepository.deleteByUserId(id);
        userRepository.deleteById(id);

        return "User deleted successfully";
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {

            String role = request.getRole().toUpperCase();

            if (!role.equals("USER") && !role.equals("ADMIN")) {
                throw new RuntimeException("Role must be USER or ADMIN");
            }

            user.setRole(role);
        }

        User saved = userRepository.save(user);

        return mapToUserResponse(saved);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}