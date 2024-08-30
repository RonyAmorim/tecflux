package com.tecflux.service;

import com.tecflux.dto.category.CategoryResponseDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.department.CreateDepartmentRequestDTO;
import com.tecflux.dto.department.UpdateDepartmentRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.Category;
import com.tecflux.entity.Department;
import com.tecflux.entity.Company;
import com.tecflux.entity.User;
import com.tecflux.repository.CategoryRepository;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;

    private final String departmentNotFound = "Departamento não encontrado.";

    public DepartmentService(DepartmentRepository departmentRepository, CompanyRepository companyRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public DepartmentResponseDTO createDepartment(CreateDepartmentRequestDTO requestDTO) {
        Company company = companyRepository.findById(requestDTO.companyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        var department = new Department();
        department.setName(requestDTO.name());
        department.setDescription(requestDTO.description());
        department.setCompany(company);

        departmentRepository.save(department);

        return DepartmentResponseDTO.fromEntity(department);
    }

    public Page<DepartmentResponseDTO> listDepartments(int page, int size) {
        var departments = departmentRepository.findAll(PageRequest.of(page, size));
        return departments.map(DepartmentResponseDTO::fromEntity);
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));
    }

    public DepartmentResponseDTO getDepartmentResponseDTO(Long id) {
        Department department = findById(id);
        return DepartmentResponseDTO.fromEntity(department);
    }

    public DepartmentResponseDTO updateDepartment(Long id, UpdateDepartmentRequestDTO requestDTO) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        department.setName(requestDTO.name());
        department.setDescription(requestDTO.description());

        departmentRepository.save(department);

        return DepartmentResponseDTO.fromEntity(department);
    }

    public DepartmentResponseDTO deleteDepartment(Long id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        departmentRepository.deleteById(department.getId());

        return DepartmentResponseDTO.fromEntity(department);
    }

    public Page<CategoryResponseDTO> listCategoriesByDepartmentId(Long departmentId, int page, int size) {
        Department department = findById(departmentId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findByDepartmentId(departmentId, pageable);

        return categoryPage.map(CategoryResponseDTO::fromEntity);
    }

    public Page<UserResponseDTO> listUsersByDepartmentId(Long departmentId, int page, int size) {
        Department department = findById(departmentId);

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByDepartmentId(departmentId, pageable);

        return userPage.map(UserResponseDTO::fromEntity);
    }
}
