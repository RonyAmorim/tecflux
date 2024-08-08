package com.tecflux.controller;

import com.tecflux.dto.category.CreateCategoryRequestDTO;
import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.category.UpdateCategoryRequestDTO;
import com.tecflux.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryRequestDTO requestDTO) {
        categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable(value = "id") Long id,
                                               @RequestBody UpdateCategoryRequestDTO requestDTO) {
        categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> listCategories(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var categories = categoryService.listCategories(page, pageSize);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable(value = "id") Long id) {
        var category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<CategoryResponseDTO>> listCategoriesByDepartment(@PathVariable Long departmentId) {
        var categories = categoryService.listCategoriesByDepartment(departmentId);
        return ResponseEntity.ok(categories);
    }
}
