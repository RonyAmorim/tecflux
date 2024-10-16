package com.tecflux.service;

import com.tecflux.dto.ticket.CreateTicketRequestDTO;
import com.tecflux.dto.ticket.TicketResponseDTO;
import com.tecflux.dto.ticket.UpdateTicketRequestDTO;
import com.tecflux.entity.*;
import com.tecflux.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         CategoryRepository categoryRepository,
                         UserRepository userRepository,
                         PriorityRepository priorityRepository,
                         StatusRepository statusRepository,
                         DepartmentRepository departmentRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.priorityRepository = priorityRepository;
        this.statusRepository = statusRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Cria um novo ticket no sistema.
     *
     * @param requestDTO Dados necessários para criar o ticket.
     * @return DTO contendo os dados do ticket criado.
     */
    @Transactional
    public TicketResponseDTO createTicket(CreateTicketRequestDTO requestDTO) {
        // Define o status como "Aberto" (ID = 1)
        Status abertoStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status 'Aberto' não encontrado"));

        // Busca a categoria, se fornecida
        Category category = null;
        if (requestDTO.categoryId() != null) {
            category = categoryRepository.findById(requestDTO.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        }

        // Busca o usuário que está criando o ticket
        User user = userRepository.findById(requestDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Busca o usuário atribuído, se fornecido
        User userAssigned = null;
        if (requestDTO.userAssignedId() != null) {
            userAssigned = userRepository.findById(requestDTO.userAssignedId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário atribuído não encontrado"));
        }

        // Busca a prioridade
        Priority priority = priorityRepository.findById(requestDTO.priorityId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada"));

        // Busca o departamento
        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));

        // Cria a entidade Ticket
        Ticket ticket = new Ticket();
        ticket.setTitle(requestDTO.title());
        ticket.setDescription(requestDTO.description());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(abertoStatus);
        ticket.setPriority(priority);
        ticket.setUser(user);
        ticket.setUserAssigned(userAssigned);
        ticket.setDepartment(department);

        // Associa a categoria se não for nula
        if (category != null) {
            ticket.setCategory(category);
        } else {
            // Se a categoria for nula, o ticket é direcionado ao departamento
            ticket.setCategory(null);
        }

        // Salva o ticket no banco de dados
        Ticket savedTicket = ticketRepository.save(ticket);

        // Retorna o DTO do ticket criado
        return TicketResponseDTO.fromEntity(savedTicket);
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
     * @return Lista de DTOs dos tickets que correspondem aos filtros.
     */
    public List<TicketResponseDTO> getTickets(
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            Long categoryId,
            Long userId,
            Long userAssignedId,
            Long priorityId,
            Long statusId,
            LocalDateTime dueDateStart,
            LocalDateTime dueDateEnd
    ) {
        Specification<Ticket> spec = Specification.where(null);

        if (createdAtStart != null && createdAtEnd != null) {
            spec = spec.and(createdAtBetween(createdAtStart, createdAtEnd));
        }

        if (categoryId != null) {
            spec = spec.and(categoryIs(categoryId));
        }

        if (userId != null) {
            spec = spec.and(userIs(userId));
        }

        if (userAssignedId != null) {
            spec = spec.and(userAssignedIs(userAssignedId));
        }

        if (priorityId != null) {
            spec = spec.and(priorityIs(priorityId));
        }

        if (statusId != null) {
            spec = spec.and(statusIs(statusId));
        }

        if (dueDateStart != null && dueDateEnd != null) {
            spec = spec.and(dueDateBetween(dueDateStart, dueDateEnd));
        }

        List<Ticket> tickets = ticketRepository.findAll(spec);

        return tickets.stream()
                .map(TicketResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Especificação para filtrar tickets pelo intervalo de data de criação.
     */
    private Specification<Ticket> createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), start, end);
    }

    /**
     * Especificação para filtrar tickets por categoria.
     */
    private Specification<Ticket> categoryIs(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    /**
     * Especificação para filtrar tickets por usuário que criou.
     */
    private Specification<Ticket> userIs(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    /**
     * Especificação para filtrar tickets por usuário atribuído.
     */
    private Specification<Ticket> userAssignedIs(Long userAssignedId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userAssigned").get("id"), userAssignedId);
    }

    /**
     * Especificação para filtrar tickets por prioridade.
     */
    private Specification<Ticket> priorityIs(Long priorityId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority").get("id"), priorityId);
    }

    /**
     * Especificação para filtrar tickets por status.
     */
    private Specification<Ticket> statusIs(Long statusId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status").get("id"), statusId);
    }

    /**
     * Especificação para filtrar tickets pelo intervalo de data de vencimento.
     */
    private Specification<Ticket> dueDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dueDate"), start, end);
    }

    /**
     * Busca um ticket pelo ID.
     *
     * @param id ID do ticket.
     * @return DTO do ticket encontrado.
     */
    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        return TicketResponseDTO.fromEntity(ticket);
    }

    /**
     * Atualiza um ticket existente.
     *
     * @param id        ID do ticket a ser atualizado.
     * @param requestDTO Dados para atualização.
     * @return DTO do ticket atualizado.
     */
    @Transactional
    public TicketResponseDTO updateTicket(Long id, UpdateTicketRequestDTO requestDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        // Atualiza os campos permitidos
        if (requestDTO.title() != null) {
            ticket.setTitle(requestDTO.title());
        }

        if (requestDTO.description() != null) {
            ticket.setDescription(requestDTO.description());
        }

        if (requestDTO.dueDate() != null) {
            ticket.setDueDate(requestDTO.dueDate());
        }

        // Atualiza a categoria, se fornecida
        if (requestDTO.categoryId() != null) {
            Category category = categoryRepository.findById(requestDTO.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
            ticket.setCategory(category);
        } else {
            ticket.setCategory(null);
        }

        // Atualiza o usuário atribuído, se fornecido
        if (requestDTO.userAssignedId() != null) {
            User userAssigned = userRepository.findById(requestDTO.userAssignedId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário atribuído não encontrado"));
            ticket.setUserAssigned(userAssigned);
        } else {
            ticket.setUserAssigned(null);
        }

        // Atualiza a prioridade, se fornecida
        if (requestDTO.priorityId() != null) {
            Priority priority = priorityRepository.findById(requestDTO.priorityId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada"));
            ticket.setPriority(priority);
        }

        // Atualiza o status, se fornecido
        if (requestDTO.statusId() != null) {
            Status status = statusRepository.findById(requestDTO.statusId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado"));
            ticket.setStatus(status);
        }

        // Atualiza o departamento, se fornecido
        if (requestDTO.departmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.departmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));
            ticket.setDepartment(department);
        }

        // Atualiza a data de atualização
        ticket.setUpdatedAt(LocalDateTime.now());

        // Salva as alterações
        Ticket updatedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromEntity(updatedTicket);
    }

    /**
     * Deleta um ticket pelo ID.
     *
     * @param id ID do ticket a ser deletado.
     */
    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        ticketRepository.delete(ticket);
    }
}
