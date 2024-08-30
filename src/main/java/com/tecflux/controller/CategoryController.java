package com.tecflux.controller;

import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.category.CreateCategoryRequestDTO;
import com.tecflux.dto.category.UpdateCategoryRequestDTO;
import com.tecflux.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CreateCategoryRequestDTO requestDTO) {
        return categoryService.createCategory(requestDTO);
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public Page<CategoryResponseDTO> listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.listCategories(page, size);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequestDTO requestDTO) {
        return categoryService.updateCategory(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
