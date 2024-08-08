package com.tecflux.dto.category;

public record CreateCategoryRequestDTO(
        String name,
        String description,
        Long departmentId
) {
}
