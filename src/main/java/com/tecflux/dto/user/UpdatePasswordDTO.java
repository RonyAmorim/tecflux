package com.tecflux.dto.user;

public record UpdatePasswordDTO(
        String oldPassword,
        String newPassword
) { }
