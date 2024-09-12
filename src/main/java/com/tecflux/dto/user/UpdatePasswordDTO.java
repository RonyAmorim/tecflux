package com.tecflux.dto.user;

public record UpdatePasswordDTO(
        Long userId,
        String oldPassword,
        String newPassword
) { }
