package com.tecflux.service;

import com.tecflux.dto.priority.CreatePriorityRequestDTO;
import com.tecflux.dto.priority.PriorityResponseDTO;
import com.tecflux.dto.priority.UpdatePriorityRequestDTO;
import com.tecflux.entity.Priority;
import com.tecflux.repository.PriorityRepository;
import com.tecflux.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriorityService {

    private final PriorityRepository priorityRepository;
    private final DepartmentRepository departmentRepository;

    private String priorityNotFound = "Prioridade não encontrada.";
    private String departmentNotFound = "Departamento não encontrado.";

    public PriorityService(PriorityRepository priorityRepository, DepartmentRepository departmentRepository) {
        this.priorityRepository = priorityRepository;
        this.departmentRepository = departmentRepository;
    }

    public void createPriority(CreatePriorityRequestDTO requestDTO) {
        var department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        var priority = new Priority();
        priority.setLevel(requestDTO.level());
        priority.setDescription(requestDTO.description());

        priority.setDepartments(List.of(department));
        department.getPriorities().add(priority);

        priorityRepository.save(priority);
        departmentRepository.save(department);
    }

    public Page<PriorityResponseDTO> listPriorities(int page, int size) {
        var priorities = priorityRepository.findAll(PageRequest.of(page, size));
        return priorities.map(PriorityResponseDTO::fromEntity);
    }

    public PriorityResponseDTO findById(Long id) {
        var priority = priorityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, priorityNotFound));
        return PriorityResponseDTO.fromEntity(priority);
    }

    public void updatePriority(Long id, UpdatePriorityRequestDTO requestDTO) {
        var priority = priorityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, priorityNotFound));

        priority.setLevel(requestDTO.level());
        priority.setDescription(requestDTO.description());

        priorityRepository.save(priority);
    }

    @Transactional
    public void deletePriority(Long id) {
        var priority = priorityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, priorityNotFound));

        // Remove priority references from departments
        for (var department : priority.getDepartments()) {
            department.getPriorities().remove(priority);
        }
        priority.getDepartments().clear();

        // Delete the priority
        priorityRepository.delete(priority);
    }

    public List<PriorityResponseDTO> listPrioritiesByDepartment(Long departmentId) {
        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        var priorities = priorityRepository.findByDepartmentsContaining(department);
        return priorities.stream().map(PriorityResponseDTO::fromEntity).collect(Collectors.toList());
    }

    public void updatePriorityForDepartment(Long departmentId, List<Long> priorityIds) {
        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        var priorities = priorityRepository.findAllById(priorityIds);

        if (priorities.size() != priorityIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, priorityNotFound);
        }

        department.setPriorities(priorities);
        departmentRepository.save(department);
    }

    public void removePriorityFromDepartment(Long departmentId, Long priorityId) {
        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        var priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, priorityNotFound));

        department.getPriorities().remove(priority);
        departmentRepository.save(department);
    }
}
