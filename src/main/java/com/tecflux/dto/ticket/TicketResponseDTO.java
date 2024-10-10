package com.tecflux.dto.ticket;

import com.tecflux.entity.Ticket;
import java.time.LocalDateTime;

public record TicketResponseDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long categoryId,
        Long userId,
        Long userAssignedId,
        Long priorityId,
        Long statusId
) {
    // Método estático para conversão de entidade para DTO
    public static TicketResponseDTO fromEntity(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getCategory().getId(),
                ticket.getUser().getId(),
                ticket.getUserAssigned() != null ? ticket.getUserAssigned().getId() : null,
                ticket.getPriority().getId(),
                ticket.getStatus().getId()
        );
    }
}
