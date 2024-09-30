package com.tecflux.controller;

import com.tecflux.dto.ApiResponse;
import com.tecflux.dto.company.CompanyResponseDTO;
import com.tecflux.dto.company.CreateCompanyRequestDTO;
import com.tecflux.dto.company.UpdateComapnyRequestDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping({"/cnpj", "/cnpj/"})
    public ResponseEntity<?> findByCnpj(@RequestParam(value = "cnpj") String cnpj) {
        try {
            CompanyResponseDTO company = companyService.findByCnpj(cnpj);

            if (company.id() != null) {
                // Empresa encontrada no banco de dados
                return ResponseEntity.ok(new ApiResponse("Empresa já cadastrada"));
            } else {
                // Empresa não cadastrada, mas CNPJ existe na Receita Federal
                return ResponseEntity.ok(company);
            }
        } catch (ResponseStatusException e) {
            // Trata as exceções lançadas pelo serviço
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse(e.getReason()));
        } catch (Exception e) {
            // Trata quaisquer outras exceções não previstas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Erro interno no servidor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> deleteCompany(@PathVariable(value = "id") Long id) {
        CompanyResponseDTO responseDTO = companyService.deleteCompany(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{companyId}/departments")
    public Page<DepartmentResponseDTO> listDepartmentsByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return companyService.listDepartmentsByCompanyId(companyId, page, size);
    }

    @GetMapping("/{companyId}/users")
    public Page<UserResponseDTO> listUsersByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return companyService.listUsersByCompanyId(companyId, page, size);
    }
}
