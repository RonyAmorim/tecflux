package com.tecflux.dto.status;

import com.tecflux.entity.Status;

public record StatusResponseDTO(
        Long id,
        String name,
        String description
) {

        public static StatusResponseDTO fromEntity(Status status) {
            return new StatusResponseDTO(
                    status.getId(),
                    status.getName(),
                    status.getDescription()
            );
        }
}
