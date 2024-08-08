package com.tecflux.service;

import com.tecflux.dto.category.CreateCategoryRequestDTO;
import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.category.UpdateCategoryRequestDTO;
import com.tecflux.entity.Category;
import com.tecflux.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    private String categoryNotFound = "Categoria não encontrada.";
    private String departmentNotFound = "Departamento não encontrado.";

    public CategoryService(CategoryRepository categoryRepository, DepartmentRepository departmentRepository) {
        this.categoryRepository = categoryRepository;
        this.departmentRepository = departmentRepository;
    }

    public void createCategory(CreateCategoryRequestDTO requestDTO) {
        var department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        var category = new Category();
        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());

        category.setDepartments(List.of(department));
        department.getCategories().add(category);

        categoryRepository.save(category);
        departmentRepository.save(department);
    }

    public Page<CategoryResponseDTO> listCategories(int page, int size) {
        var categories = categoryRepository.findAll(PageRequest.of(page, size));
        return categories.map(CategoryResponseDTO::fromEntity);
    }

    public CategoryResponseDTO findById(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryNotFound));
        return CategoryResponseDTO.fromEntity(category);
    }

    public void updateCategory(Long id, UpdateCategoryRequestDTO requestDTO) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryNotFound));

        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());

        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryNotFound));

        // Remove category references from departments
        for (var department : category.getDepartments()) {
            department.getCategories().remove(category);
        }
        category.getDepartments().clear();

        // Delete the category
        categoryRepository.delete(category);
    }

    public List<CategoryResponseDTO> listCategoriesByDepartment(Long departmentId) {
        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        var categories = categoryRepository.findByDepartmentsContaining(department);
        return categories.stream().map(CategoryResponseDTO::fromEntity).collect(Collectors.toList());
    }
}
