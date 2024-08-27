package com.tecflux.service;

import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.department.CreateDepartmentRequestDTO;
import com.tecflux.dto.department.UpdateDepartmentRequestDTO;
import com.tecflux.entity.Department;
import com.tecflux.entity.Company;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    private final String departmentNotFound = "Departamento não encontrado.";

    public DepartmentService(DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
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
}
