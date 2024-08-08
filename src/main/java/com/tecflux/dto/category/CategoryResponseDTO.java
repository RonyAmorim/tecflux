package com.tecflux.dto.category;

import com.tecflux.entity.Category;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description)
{
    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getDescription());
    }
}
