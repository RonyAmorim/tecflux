package com.tecflux.dto.priority;

import com.tecflux.entity.Priority;

public record PriorityResponseDTO(
        Long id,
        String level,
        String description
) {

        public static PriorityResponseDTO fromEntity(Priority priority) {
            return new PriorityResponseDTO(
                    priority.getId(),
                    priority.getLevel(),
                    priority.getDescription()
            );
        }
}
