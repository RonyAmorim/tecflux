package com.tecflux.controller;

import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.department.CreateDepartmentRequestDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.department.UpdateDepartmentRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody CreateDepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO responseDTO = departmentService.createDepartment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> listDepartments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<DepartmentResponseDTO> departments = departmentService.listDepartments(page, size);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO department = departmentService.getDepartmentResponseDTO(id);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @RequestBody UpdateDepartmentRequestDTO requestDTO) {
        DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> deleteDepartment(@PathVariable Long id) {
        DepartmentResponseDTO deletedDepartment = departmentService.deleteDepartment(id);
        return ResponseEntity.ok(deletedDepartment);
    }

    @GetMapping("/{departmentId}/categories")
    public Page<CategoryResponseDTO> listCategoriesByDepartmentId(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return departmentService.listCategoriesByDepartmentId(departmentId, page, size);
    }

    @GetMapping("/'{departmentId}/users")
    public Page<UserResponseDTO> listUsersByDepartmentId(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return departmentService.listUsersByDepartmentId(departmentId, page, size);
    }
}
