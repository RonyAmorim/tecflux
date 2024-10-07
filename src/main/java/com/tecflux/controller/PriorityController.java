package com.tecflux.controller;

import com.tecflux.dto.priority.PriorityResponseDTO;
import com.tecflux.entity.Priority;
import com.tecflux.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/priorities")
public class PriorityController {

    @Autowired
    private PriorityService priorityService;

    @GetMapping
    public ResponseEntity<List<PriorityResponseDTO>> getAllPriorities() {
        List<PriorityResponseDTO> priorities = priorityService.listAllPriorities();
        return ResponseEntity.ok(priorities);
    }
}
