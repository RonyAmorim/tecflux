package com.tecflux.dto.ticket;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para atualização de um ticket existente.
 */
public record UpdateTicketRequestDTO(
        @Size(max = 150, message = "O título não pode ter mais de 150 caracteres")
        String title,

        String description,

        Long userAssignedId, // Pode ser null para desatribuir

        Long priorityId,

        Long departmentId,

        Long categoryId,

        LocalDateTime dueDate,

        Long statusId // Permite alterar o status
) {}
