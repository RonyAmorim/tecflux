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
    public ResponseEntity<Void> createCompany (@RequestBody CreateCompanyRequestDTO requestDTO) {
        companyService.createCompany(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompany(@PathVariable (value = "id") Long id,
                                              @RequestBody UpdateComapnyRequestDTO requestDTO) {
        companyService.updateCompany(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDTO>> listCompanies(@RequestParam(name = "page", defaultValue = "0")Integer page,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10")Integer pageSize) {
        var companies = companyService.listCompanies(page, pageSize);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> findById(@PathVariable (value = "id") Long id) {
        var company = companyService.findById(id);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable (value = "id") Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
