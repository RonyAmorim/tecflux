package com.tecflux.controller;

import com.tecflux.dto.priority.CreatePriorityRequestDTO;
import com.tecflux.dto.priority.PriorityResponseDTO;
import com.tecflux.dto.priority.UpdatePriorityRequestDTO;
import com.tecflux.service.PriorityService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priorities")
public class PriorityController {

    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping
    public ResponseEntity<Void> createPriority(@RequestBody CreatePriorityRequestDTO requestDTO) {
        priorityService.createPriority(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePriority(@PathVariable(value = "id") Long id,
                                               @RequestBody UpdatePriorityRequestDTO requestDTO) {
        priorityService.updatePriority(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PriorityResponseDTO>> listPriorities(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        var priorities = priorityService.listPriorities(page, size);
        return ResponseEntity.ok(priorities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriorityResponseDTO> findById(@PathVariable(value = "id") Long id) {
        var priority = priorityService.findById(id);
        return ResponseEntity.ok(priority);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriority(@PathVariable(value = "id") Long id) {
        priorityService.deletePriority(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<PriorityResponseDTO>> listPrioritiesByDepartment(@PathVariable(value = "departmentId") Long departmentId) {
        var priorities = priorityService.listPrioritiesByDepartment(departmentId);
        return ResponseEntity.ok(priorities);
    }
}
