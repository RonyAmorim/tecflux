package com.tecflux.dto.company;

public record UpdateComapnyRequestDTO(
        String name,
        String address,
        String phone
) {
}
