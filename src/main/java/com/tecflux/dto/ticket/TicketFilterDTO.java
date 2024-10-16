package com.tecflux.dto.ticket;

import java.time.LocalDateTime;

/**
 * DTO para filtragem de tickets.
 */
public record TicketFilterDTO(
        LocalDateTime createdAtStart,
        LocalDateTime createdAtEnd,
        Long categoryId,
        Long userId,
        Long userAssignedId,
        Long priorityId,
        Long statusId,
        LocalDateTime dueDateStart,
        LocalDateTime dueDateEnd
) {}