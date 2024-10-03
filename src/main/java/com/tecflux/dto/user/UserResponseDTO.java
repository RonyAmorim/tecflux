package com.tecflux.dto.user;

import com.tecflux.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phone,
        String position,
        Instant createdAt,
        Instant updatedAt,
        Instant lastLogin,
        Long companyId,
        Long departmentId,
        Set<String> roles
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getRawEmail(),
                user.getRawPhone(),
                user.getPosition(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin(),
                user.getCompany().getId(),
                user.getDepartment().getId(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
    }
}
