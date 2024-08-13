package com.tecflux.dto.user;

import java.util.Set;

public record CreateUserRequestDTO(
        String username,
        String email,
        String password,
        String phone,
        Long departmentId,
        Long companyId,
        Set<String> roles
) {
}
