package com.tecflux.controller;

import com.tecflux.dto.department.CreateDepartmentRequestDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.department.UpdateDepartmentRequestDTO;
import com.tecflux.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Void> createDepartment(@RequestBody CreateDepartmentRequestDTO requestDTO) {
        departmentService.createDepartment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDepartment(@PathVariable(value = "id") Long id,
                                                 @RequestBody UpdateDepartmentRequestDTO requestDTO) {
        departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> findById(@PathVariable(value = "id") Long id) {
        var department = departmentService.findById(id);
        return ResponseEntity.ok(department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable(value = "id") Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
