package com.tecflux.dto.ticket;

import com.tecflux.dto.ticket.TicketResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * DTO para resposta paginada de tickets.
 */
@Getter
@AllArgsConstructor
public class TicketPaginationResponseDTO {
    private List<TicketResponseDTO> tickets;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
