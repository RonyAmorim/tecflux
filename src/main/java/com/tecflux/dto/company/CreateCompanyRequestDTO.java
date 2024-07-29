package com.tecflux.dto.company;

public record CreateCompanyRequestDTO(
        String name,
        String cnpj,
        String address,
        String phone
) {
}
