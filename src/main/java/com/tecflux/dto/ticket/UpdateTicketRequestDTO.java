package com.tecflux.dto.ticket;

import jakarta.validation.constraints.Size;

public record UpdateTicketRequestDTO(
        @Size(max = 150, message = "O título não pode ter mais de 150 caracteres")
        String title,

        String description,

        Long categoryId,

        Long userId,

        Long userAssignedId,

        Long priorityId,

        Long statusId
) {}
