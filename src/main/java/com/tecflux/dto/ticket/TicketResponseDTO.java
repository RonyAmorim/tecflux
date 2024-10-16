package com.tecflux.dto.ticket;

import com.tecflux.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * DTO para resposta de um ticket.
 */
@Getter
@AllArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private String userName;
    private String userAssignedName;
    private String priorityName;
    private String statusName;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    /**
     * Converte uma entidade Ticket para um DTO.
     *
     * @param ticket Entidade Ticket.
     * @return DTO TicketResponseDTO.
     */
    public static TicketResponseDTO fromEntity(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory() != null ? ticket.getCategory().getName() : null,
                ticket.getUser().getName(),
                ticket.getUserAssigned() != null ? ticket.getUserAssigned().getName() : null,
                ticket.getPriority().getLevel(),
                ticket.getStatus().getName(),
                ticket.getDepartment().getName(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getDueDate()
        );
    }
}
