package com.tecflux.dto.user;

import java.util.Set;

public record UpdateUserRequestDTO(
        String username,
        String email,
        String password,
        String phone,
        Long departmentId,
        Set<String> roles
) {
}
