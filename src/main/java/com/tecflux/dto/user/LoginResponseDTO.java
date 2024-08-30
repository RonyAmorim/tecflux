package com.tecflux.dto.user;

import com.tecflux.entity.User;

import java.time.Instant;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        Long id,
        String name,
        String email,
        String phone,
        Instant createdAt,
        Instant lastLogin,
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
                user.getDepartment() != null ? user.getDepartment().getId() : null,
                user.getCompany() != null ? user.getCompany().getId() : null
        );
    }
}
