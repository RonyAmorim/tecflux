package com.tecflux.controller;

import com.tecflux.dto.ticket.CreateTicketRequestDTO;
import com.tecflux.dto.ticket.TicketResponseDTO;
import com.tecflux.dto.ticket.UpdateTicketRequestDTO;
import com.tecflux.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Cria um novo ticket.
     *
     * @param requestDTO Dados para criação do ticket.
     * @return DTO do ticket criado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponseDTO createTicket(@RequestBody CreateTicketRequestDTO requestDTO) {
        return ticketService.createTicket(requestDTO);
    }

    /**
     * Busca tickets com base nos filtros fornecidos.
     *
     * @param createdAtStart Data de criação inicial.
     * @param createdAtEnd   Data de criação final.
     * @param categoryId     ID da categoria.
     * @param userId         ID do usuário que criou o ticket.
     * @param userAssignedId ID do usuário atribuído.
     * @param priorityId     ID da prioridade.
     * @param statusId       ID do status.
     * @param dueDateStart   Data de vencimento inicial.
     * @param dueDateEnd     Data de vencimento final.
     * @param departmentId   ID do departamento (novo filtro).
     * @param title          Título do ticket (novo filtro).
     * @param page           Número da página para paginação (opcional).
     * @param size           Tamanho da página para paginação (opcional).
     * @return Lista de DTOs dos tickets que correspondem aos filtros.
     */
    @GetMapping
    public List<TicketResponseDTO> getTickets(
            @RequestParam(value = "createdAtStart", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,

            @RequestParam(value = "createdAtEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd,

            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userAssignedId", required = false) Long userAssignedId,
            @RequestParam(value = "priorityId", required = false) Long priorityId,
            @RequestParam(value = "statusId", required = false) Long statusId,

            @RequestParam(value = "dueDateStart", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDateStart,

            @RequestParam(value = "dueDateEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDateEnd,

            // Novos parâmetros adicionados
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "title", required = false) String title,

            // Parâmetros de paginação (opcional)
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return ticketService.getTickets(
                createdAtStart,
                createdAtEnd,
                categoryId,
                userId,
                userAssignedId,
                priorityId,
                statusId,
                dueDateStart,
                dueDateEnd,
                departmentId,
                title
        );
    }

    /**
     * Busca um ticket pelo ID.
     *
     * @param id ID do ticket.
     * @return DTO do ticket encontrado.
     */
    @GetMapping("/{id}")
    public TicketResponseDTO getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    /**
     * Atualiza um ticket existente.
     *
     * @param id        ID do ticket a ser atualizado.
     * @param requestDTO Dados para atualização.
     * @return DTO do ticket atualizado.
     */
    @PutMapping("/{id}")
    public TicketResponseDTO updateTicket(
            @PathVariable Long id,
            @RequestBody UpdateTicketRequestDTO requestDTO
    ) {
        return ticketService.updateTicket(id, requestDTO);
    }

    /**
     * Deleta um ticket pelo ID.
     *
     * @param id ID do ticket a ser deletado.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}
