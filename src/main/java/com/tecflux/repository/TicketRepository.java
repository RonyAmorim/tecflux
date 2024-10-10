package com.tecflux.repository;

import com.tecflux.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Buscar tickets por usuário
    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    // Buscar tickets por usuário atribuído
    Page<Ticket> findByUserAssignedId(Long userAssignedId, Pageable pageable);

    // Buscar tickets por categoria
    Page<Ticket> findByCategoryId(Long categoryId, Pageable pageable);

    // Buscar tickets por prioridade
    Page<Ticket> findByPriorityId(Long priorityId, Pageable pageable);

    // Buscar tickets por status
    Page<Ticket> findByStatusId(Long statusId, Pageable pageable);

    // Outros métodos personalizados conforme necessário
}
