package com.tecflux.service;

import com.tecflux.dto.company.CompanyResponseDTO;
import com.tecflux.dto.company.CreateCompanyRequestDTO;
import com.tecflux.dto.company.UpdateComapnyRequestDTO;
import com.tecflux.entity.Company;
import com.tecflux.exception.CnpjAlreadyExistsException;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.util.CryptoUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    private final String companyNotFound = "Empresa não encontrada.";

    public CompanyService(CompanyRepository companyRepository, DepartmentRepository departmentRepository) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
    }

    public CompanyResponseDTO createCompany(CreateCompanyRequestDTO requestDTO) {
        if (requestDTO.cnpj() == null || requestDTO.cnpj().isEmpty()) {
            throw new IllegalArgumentException("CNPJ cannot be null or empty");
        }

        String hashedCnpj = CryptoUtil.hash(requestDTO.cnpj());
        if (companyRepository.existsByHashedCnpj(hashedCnpj)) {
            throw new CnpjAlreadyExistsException("CNPJ já cadastrado: " + requestDTO.cnpj());
        }

        var company = new Company();
        company.setName(requestDTO.name());
        company.setRawCnpj(requestDTO.cnpj());
        company.setHashedCnpj(hashedCnpj);
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);

        return CompanyResponseDTO.fromEntity(company);
    }

    public Page<CompanyResponseDTO> listCompanies(int page, int size) {
        var companies = companyRepository.findAll(PageRequest.of(page, size));
        return companies.map(CompanyResponseDTO::fromEntity);
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
    }

    public CompanyResponseDTO getCompanyResponseDTO(Long id) {
        Company company = findById(id); // Recupera a entidade Company
        return CompanyResponseDTO.fromEntity(company); // Converte a entidade em DTO
    }



    public CompanyResponseDTO findByCnpj(String cnpj) {
        String hashedCnpj = CryptoUtil.hash(cnpj);
        var company = companyRepository.findByHashedCnpj(hashedCnpj)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));
        return CompanyResponseDTO.fromEntity(company);
    }

    public CompanyResponseDTO updateCompany(Long id, UpdateComapnyRequestDTO requestDTO) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        company.setName(requestDTO.name());
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);

        return CompanyResponseDTO.fromEntity(company);
    }

    public CompanyResponseDTO deleteCompany(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        companyRepository.deleteById(company.getId());

        return CompanyResponseDTO.fromEntity(company);
    }
}
