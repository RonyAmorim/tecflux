package com.tecflux.service;

import com.tecflux.dto.company.CompanyResponseDTO;
import com.tecflux.dto.company.CreateCompanyRequestDTO;
import com.tecflux.dto.company.UpdateComapnyRequestDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.entity.Company;
import com.tecflux.exception.InvalidCnpjException;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.validator.CnpjValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    private String companyNotFound = "Empresa não encontrada.";

    public CompanyService(CompanyRepository companyRepository, DepartmentRepository departmentRepository) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
    }

    public void createCompany(CreateCompanyRequestDTO requestDTO) {
        validateCnpj(requestDTO.cnpj());

        var company = new Company();
        company.setName(requestDTO.name());
        company.setRawCnpj(requestDTO.cnpj());
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);
    }

    public Page<CompanyResponseDTO> listCompanies(int page, int size) {
        var companies = companyRepository.findAll(PageRequest.of(page, size));
        return companies.map(CompanyResponseDTO::fromEntity);
    }

    private void validateCnpj(String cnpj) {
        if (!CnpjValidator.isValidCnpj(cnpj)) {
            throw new InvalidCnpjException("CNPJ: "+ cnpj +" Invalido.");
        }
    }

    public CompanyResponseDTO findById(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));
        return CompanyResponseDTO.fromEntity(company);
    }


    public void updateCompany(Long id, UpdateComapnyRequestDTO requestDTO) {
        var company = companyRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        company.setName(requestDTO.name());
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        companyRepository.deleteById(company.getId());
    }

    public Page<DepartmentResponseDTO> listDepartmentsByCompany(Long companyId, int page, int size) {
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        var departments = departmentRepository.findByCompany(company, PageRequest.of(page, size));
        return departments.map(DepartmentResponseDTO::fromEntity);
    }
}
