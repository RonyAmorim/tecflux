package com.tecflux.dto.user;

import com.tecflux.entity.Role;
import com.tecflux.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        Long id,
        String name,
        String email,
        String phone,
        Instant createdAt,
        Instant lastLogin,
        Set<String> roles,
        Long departmentId,
        Long companyId
) {
    public static LoginResponseDTO fromEntity(User user, String accessToken) {
        return new LoginResponseDTO(
                accessToken,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getRawEmail(),
                user.getRawPhone(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()),
                user.getDepartment() != null ? user.getDepartment().getId() : null,
                user.getCompany() != null ? user.getCompany().getId() : null
        );
    }
}
