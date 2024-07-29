package com.tecflux.dto.department;

import com.tecflux.entity.Department;

public record DepartmentResponseDTO(Long id, String name, String description, Long companyId) {
    public static DepartmentResponseDTO fromEntity(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getCompany().getId()
        );
    }
}
