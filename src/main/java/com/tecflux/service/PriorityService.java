package com.tecflux.service;

import com.tecflux.dto.priority.PriorityResponseDTO;
import com.tecflux.entity.Priority;
import com.tecflux.repository.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriorityService {

    private PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<PriorityResponseDTO> listAllPriorities() {
        try {
            return priorityRepository.findAll().stream()
                    .map(PriorityResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar prioridades", e);
        }
    }
}
