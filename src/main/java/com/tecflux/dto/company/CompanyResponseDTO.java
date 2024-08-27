package com.tecflux.dto.company;

import com.tecflux.entity.Company;

import java.time.LocalDateTime;

public record CompanyResponseDTO(
        Long id,
        String name,
        String cnpj,
        String address,
        String phone,
        LocalDateTime createdAt) {

    public static CompanyResponseDTO fromEntity(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getRawCnpj(),
                company.getRawAddress(),
                company.getRawPhone(),
                company.getCreatedAt()
        );
    }
}
