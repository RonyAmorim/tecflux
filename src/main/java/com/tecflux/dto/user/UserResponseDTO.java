package com.tecflux.dto.user;

import com.tecflux.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String phone,
        Instant createdAt,
        Instant lastLogin,
        Set<String> roles
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRawEmail(),
                user.getRawPhone(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
    }
}
