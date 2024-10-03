package com.tecflux.dto.user;

import java.util.Set;

public record CreateUserRequestDTO(
        String name,
        String email,
        String password,
        String phone,
        String position,
        Long departmentId,
        Long companyId,
        Set<String> roles
) {
}
