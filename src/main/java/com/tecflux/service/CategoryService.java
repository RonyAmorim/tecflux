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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    public CategoryService(CategoryRepository categoryRepository, DepartmentRepository departmentRepository) {
        this.categoryRepository = categoryRepository;
        this.departmentRepository = departmentRepository;
    }

    public CategoryResponseDTO createCategory(CreateCategoryRequestDTO requestDTO) {
        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Departamento não encontrado"));

        Category category = new Category();
        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());
        category.setDepartment(department);

        categoryRepository.save(category);

        return CategoryResponseDTO.fromEntity(category);
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        return CategoryResponseDTO.fromEntity(category);
    }

    public Page<CategoryResponseDTO> listCategories(int page, int size) {
        var pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable).map(CategoryResponseDTO::fromEntity);
    }

    public CategoryResponseDTO updateCategory(Long id, UpdateCategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());

        categoryRepository.save(category);

        return CategoryResponseDTO.fromEntity(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        categoryRepository.delete(category);
    }
}
