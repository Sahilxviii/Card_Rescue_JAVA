package com.sahil.cardrescue.service;

import com.sahil.cardrescue.dto.ForgotPasswordRequest;
import com.sahil.cardrescue.dto.ResetPasswordRequest;
import com.sahil.cardrescue.entity.PasswordResetToken;
import com.sahil.cardrescue.entity.User;
import com.sahil.cardrescue.repository.PasswordResetTokenRepository;
import com.sahil.cardrescue.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public String forgotPassword(ForgotPasswordRequest request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        String subject = "Card Rescue - Password Reset";

        String body =
                "Hello " + user.getName() + ",\n\n" +
                "You requested to reset your password.\n\n" +
                "Use this reset token:\n\n" +
                token + "\n\n" +
                "This token will expire in 15 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\nCard Rescue Team";

        emailService.sendMatchNotification(user.getEmail(), subject, body);

        return "Password reset token sent to email";
    }

    public String resetPassword(ResetPasswordRequest request) {

        if (request.getToken() == null || request.getToken().isBlank()) {
            throw new RuntimeException("Reset token is required");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new RuntimeException("New password is required");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Reset token already used");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return "Password reset successfully";
    }
}