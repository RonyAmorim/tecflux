package com.tecflux.service;

import com.tecflux.dto.ticket.CreateTicketRequestDTO;
import com.tecflux.dto.ticket.TicketResponseDTO;
import com.tecflux.dto.ticket.UpdateTicketRequestDTO;
import com.tecflux.entity.Category;
import com.tecflux.entity.Priority;
import com.tecflux.entity.Status;
import com.tecflux.entity.Ticket;
import com.tecflux.entity.User;
import com.tecflux.repository.CategoryRepository;
import com.tecflux.repository.PriorityRepository;
import com.tecflux.repository.StatusRepository;
import com.tecflux.repository.TicketRepository;
import com.tecflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         CategoryRepository categoryRepository,
                         UserRepository userRepository,
                         PriorityRepository priorityRepository,
                         StatusRepository statusRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.priorityRepository = priorityRepository;
        this.statusRepository = statusRepository;
    }

    /**
     * Cria um novo ticket.
     *
     * @param requestDTO Dados para criação do ticket.
     * @return DTO do ticket criado.
     */
    public TicketResponseDTO createTicket(CreateTicketRequestDTO requestDTO) {
        Category category = categoryRepository.findById(requestDTO.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Priority priority = priorityRepository.findById(requestDTO.priorityId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada"));

        User userAssigned = null;
        if (requestDTO.userAssignedId() != null) {
            userAssigned = userRepository.findById(requestDTO.userAssignedId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário atribuído não encontrado"));
        }

        Status status = statusRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado"));


        Ticket ticket = new Ticket();
        ticket.setTitle(requestDTO.title());
        ticket.setDescription(requestDTO.description());
        ticket.setCategory(category);
        ticket.setUser(user);
        ticket.setPriority(priority);
        ticket.setStatus(status);
        ticket.setUserAssigned(userAssigned);

        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketResponseDTO.fromEntity(savedTicket);
    }

    /**
     * Obtém um ticket por seu ID.
     *
     * @param id ID do ticket.
     * @return DTO do ticket.
     */
    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));
        return TicketResponseDTO.fromEntity(ticket);
    }

    /**
     * Lista todos os tickets com suporte a paginação.
     *
     * @param pageable Informações de paginação.
     * @return Página de DTOs de tickets.
     */
    public Page<TicketResponseDTO> listTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).map(TicketResponseDTO::fromEntity);
    }

    /**
     * Atualiza um ticket existente.
     *
     * @param id          ID do ticket a ser atualizado.
     * @param requestDTO Dados para atualização do ticket.
     * @return DTO do ticket atualizado.
     */
    public TicketResponseDTO updateTicket(Long id, UpdateTicketRequestDTO requestDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        if (requestDTO.title() != null && !requestDTO.title().isEmpty()) {
            ticket.setTitle(requestDTO.title());
        }

        if (requestDTO.description() != null) {
            ticket.setDescription(requestDTO.description());
        }

        if (requestDTO.categoryId() != null) {
            Category category = categoryRepository.findById(requestDTO.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
            ticket.setCategory(category);
        }

        if (requestDTO.userId() != null) {
            User user = userRepository.findById(requestDTO.userId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
            ticket.setUser(user);
        }

        if (requestDTO.userAssignedId() != null) {
            User userAssigned = userRepository.findById(requestDTO.userAssignedId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário atribuído não encontrado"));
            ticket.setUserAssigned(userAssigned);
        }

        if (requestDTO.priorityId() != null) {
            Priority priority = priorityRepository.findById(requestDTO.priorityId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada"));
            ticket.setPriority(priority);
        }

        if (requestDTO.statusId() != null) {
            Status status = statusRepository.findById(requestDTO.statusId()) //todo criar o status como entidade
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado"));
            ticket.setStatus(status);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return TicketResponseDTO.fromEntity(updatedTicket);
    }

    /**
     * Deleta um ticket por seu ID.
     *
     * @param id ID do ticket a ser deletado.
     */
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado");
        }
        ticketRepository.deleteById(id);
    }
}
