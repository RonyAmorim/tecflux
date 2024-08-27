package com.tecflux.controller;

import com.tecflux.dto.company.CompanyResponseDTO;
import com.tecflux.dto.company.CreateCompanyRequestDTO;
import com.tecflux.dto.company.UpdateComapnyRequestDTO;
import com.tecflux.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(@RequestBody CreateCompanyRequestDTO requestDTO) {
        CompanyResponseDTO responseDTO = companyService.createCompany(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@PathVariable(value = "id") Long id,
                                                            @RequestBody UpdateComapnyRequestDTO requestDTO) {
        CompanyResponseDTO responseDTO = companyService.updateCompany(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDTO>> listCompanies(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var companies = companyService.listCompanies(page, pageSize);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> findById(@PathVariable(value = "id") Long id) {
        var company = companyService.findById(id);
        return ResponseEntity.ok(CompanyResponseDTO.fromEntity(company));
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<CompanyResponseDTO> findByCnpj(@PathVariable(value = "cnpj") String cnpj) {
        var company = companyService.findByCnpj(cnpj);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> deleteCompany(@PathVariable(value = "id") Long id) {
        CompanyResponseDTO responseDTO = companyService.deleteCompany(id);
        return ResponseEntity.ok(responseDTO);
    }
}
