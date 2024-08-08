package com.tecflux.dto.priority;

public record CreatePriorityRequestDTO(
        String level,
        String description,
        Long departmentId
) {
}
