package com.tecflux.dto.user;

import java.util.Set;

public record UpdateUserRequestDTO(
        String name,
        String email,
        String password,
        String phone,
        String position,
        Long departmentId,
        Set<String> roles
) {
}
