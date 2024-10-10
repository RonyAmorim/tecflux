package com.tecflux.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título não pode ter mais de 150 caracteres")
        String title,

        String description,

        @NotNull(message = "A categoria é obrigatória")
        Long categoryId,

        @NotNull(message = "O usuário é obrigatório")
        Long userId,

        Long userAssignedId,

        @NotNull(message = "A prioridade é obrigatória")
        Long priorityId
) {}
