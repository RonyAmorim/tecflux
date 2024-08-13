package com.tecflux.dto.user;

public record LoginRequestDTO(
        String email,
        String password
) {}
