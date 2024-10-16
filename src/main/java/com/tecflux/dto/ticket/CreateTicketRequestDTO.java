package com.tecflux.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para criação de um novo ticket.
 */
public record CreateTicketRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título não pode ter mais de 150 caracteres")
        String title,

        String description,

        @NotNull(message = "O usuário é obrigatório")
        Long userId,

        Long userAssignedId, // Pode ser null

        @NotNull(message = "A prioridade é obrigatória")
        Long priorityId,

        @NotNull(message = "O departamento é obrigatório")
        Long departmentId,

        Long categoryId, // Pode ser null

        LocalDateTime dueDate // Pode ser null, dependendo da lógica de negócio
) {}
