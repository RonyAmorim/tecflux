package com.tecflux.service;

import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.category.CreateCategoryRequestDTO;
import com.tecflux.dto.category.UpdateCategoryRequestDTO;
import com.tecflux.entity.Category;
import com.tecflux.entity.Department;
import com.tecflux.repository.CategoryRepository;
import com.tecflux.repository.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    private static final String CATEGORY_NOT_FOUND = "Categoria não encontrada.";
    private static final String DEPARTMENT_NOT_FOUND = "Departamento não encontrado.";

    public CategoryService(CategoryRepository categoryRepository, DepartmentRepository departmentRepository) {
        this.categoryRepository = categoryRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Cria uma nova categoria associada a um departamento.
     *
     * @param requestDTO Dados para criação da categoria.
     * @return DTO da categoria criada.
     */
    @Transactional
    public CategoryResponseDTO createCategory(CreateCategoryRequestDTO requestDTO) {
        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DEPARTMENT_NOT_FOUND));

        Category category = new Category();
        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());
        category.setDepartment(department);

        categoryRepository.save(category);

        return CategoryResponseDTO.fromEntity(category);
    }

    /**
     * Obtém uma categoria por seu ID.
     *
     * @param id ID da categoria.
     * @return DTO da categoria.
     */
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));
        return CategoryResponseDTO.fromEntity(category);
    }

    /**
     * Lista todas as categorias com paginação.
     *
     * @param page Número da página.
     * @param size Tamanho da página.
     * @return Página de DTOs de categorias.
     */
    public Page<CategoryResponseDTO> listCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable).map(CategoryResponseDTO::fromEntity);
    }

    /**
     * Atualiza uma categoria existente.
     *
     * @param id         ID da categoria a ser atualizada.
     * @param requestDTO Dados para atualização.
     * @return DTO da categoria atualizada.
     */
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, UpdateCategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));

        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());

        categoryRepository.save(category);

        return CategoryResponseDTO.fromEntity(category);
    }

    /**
     * Deleta uma categoria por seu ID.
     *
     * @param id ID da categoria a ser deletada.
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }
}
