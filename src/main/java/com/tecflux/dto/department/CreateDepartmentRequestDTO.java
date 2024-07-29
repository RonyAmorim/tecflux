package com.tecflux.dto.department;

public record CreateDepartmentRequestDTO(
        String name,
        String description,
        Long companyId
) {
}
