package com.tecflux.service;

import com.tecflux.dto.department.CreateDepartmentRequestDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.department.UpdateDepartmentRequestDTO;
import com.tecflux.entity.Department;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.DepartmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    private String departmentNotFound = "Departamento não encontrado.";
    private String companyNotFound = "Empresa não encontrada.";

    public DepartmentService(DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
    }

    public void createDepartment(CreateDepartmentRequestDTO requestDTO) {
        var company = companyRepository.findById(requestDTO.companyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        var department = new Department();
        department.setName(requestDTO.name());
        department.setDescription(requestDTO.description());
        department.setCompany(company);

        departmentRepository.save(department);
    }

    public DepartmentResponseDTO findById(Long id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));
        return DepartmentResponseDTO.fromEntity(department);
    }

    public void updateDepartment(Long id, UpdateDepartmentRequestDTO requestDTO) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        department.setName(requestDTO.name());
        department.setDescription(requestDTO.description());

        departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        departmentRepository.deleteById(department.getId());
    }
}
