package com.tecflux.dto.user;

import java.util.Set;

public record RegisterUserRequestDTO(
        String name,
        String email,
        String phone,
        String position,
        Long departmentId,
        Long companyId,
        Set<String> roles
) {
}


