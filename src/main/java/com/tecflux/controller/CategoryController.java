package com.tecflux.controller;

import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.category.CreateCategoryRequestDTO;
import com.tecflux.dto.category.UpdateCategoryRequestDTO;
import com.tecflux.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Cria uma nova categoria.
     *
     * @param requestDTO Dados para criação da categoria.
     * @return DTO da categoria criada com status 201.
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CreateCategoryRequestDTO requestDTO) {
        CategoryResponseDTO responseDTO = categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Obtém uma categoria por seu ID.
     *
     * @param id ID da categoria.
     * @return DTO da categoria com status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Lista todas as categorias com paginação.
     *
     * @param page Número da página.
     * @param size Tamanho da página.
     * @return Página de DTOs de categorias com status 200.
     */
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> listCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CategoryResponseDTO> categories = categoryService.listCategories(page, size);
        return ResponseEntity.ok(categories);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id         ID da categoria a ser atualizada.
     * @param requestDTO Dados para atualização.
     * @return DTO da categoria atualizada com status 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDTO requestDTO) {
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Deleta uma categoria por seu ID.
     *
     * @param id ID da categoria a ser deletada.
     * @return Resposta sem conteúdo com status 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
