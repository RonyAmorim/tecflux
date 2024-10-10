package com.tecflux.service;

import com.tecflux.dto.status.StatusResponseDTO;
import com.tecflux.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<StatusResponseDTO> listAllStatus() {
        try {
            return statusRepository.findAll().stream()
                    .map(StatusResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar status", e);
        }
    }

    public StatusResponseDTO findStatusById(Long id) {
        return statusRepository.findById(id)
                .map(StatusResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Status não encontrado"));
    }
}
