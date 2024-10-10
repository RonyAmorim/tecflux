package com.tecflux.controller;

import com.tecflux.dto.ApiResponse;
import com.tecflux.dto.ticket.CreateTicketRequestDTO;
import com.tecflux.dto.ticket.TicketResponseDTO;
import com.tecflux.dto.ticket.UpdateTicketRequestDTO;
import com.tecflux.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Endpoint para criar um novo ticket.
     *
     * @param requestDTO DTO contendo os dados para criação do ticket.
     * @return DTO do ticket criado com status 201.
     */
    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody CreateTicketRequestDTO requestDTO) {
        TicketResponseDTO responseDTO = ticketService.createTicket(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Endpoint para obter um ticket por seu ID.
     *
     * @param id ID do ticket.
     * @return DTO do ticket com status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        TicketResponseDTO responseDTO = ticketService.getTicketById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Endpoint para listar todos os tickets com paginação.
     *
     * @param pageable Informações de paginação.
     * @return Página de DTOs de tickets com status 200.
     */
    @GetMapping
    public ResponseEntity<Page<TicketResponseDTO>> listTickets(Pageable pageable) {
        Page<TicketResponseDTO> tickets = ticketService.listTickets(pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Endpoint para atualizar um ticket existente.
     *
     * @param id         ID do ticket a ser atualizado.
     * @param requestDTO DTO contendo os dados para atualização.
     * @return DTO do ticket atualizado com status 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateTicketRequestDTO requestDTO) {
        TicketResponseDTO responseDTO = ticketService.updateTicket(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Endpoint para deletar um ticket por seu ID.
     *
     * @param id ID do ticket a ser deletado.
     * @return Resposta sem conteúdo com status 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
