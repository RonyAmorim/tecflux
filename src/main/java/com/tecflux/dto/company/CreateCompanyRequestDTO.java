package com.tecflux.dto.company;

import jakarta.validation.constraints.Pattern;

public record CreateCompanyRequestDTO(
        String name,
        @Pattern(regexp = "\\d{14}", message = "CNPJ inválido")
        String cnpj,
        String address,
        String phone
) {
}
