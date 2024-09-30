package com.tecflux.service;

import com.tecflux.dto.ApiResponse;
import com.tecflux.dto.company.CompanyResponseDTO;
import com.tecflux.dto.company.CreateCompanyRequestDTO;
import com.tecflux.dto.company.ExternalCnpjResponse;
import com.tecflux.dto.company.UpdateComapnyRequestDTO;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.User;
import com.tecflux.exception.CnpjAlreadyExistsException;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.repository.UserRepository;
import com.tecflux.util.CryptoUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final String BRASIL_API_URL = System.getenv("BRASIL_API_URL");

    private static final String companyNotFound = "Empresa não encontrada.";

    public CompanyService(CompanyRepository companyRepository,
                          DepartmentRepository departmentRepository,
                          UserRepository userRepository,
                          RestTemplate restTemplate) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public CompanyResponseDTO createCompany(CreateCompanyRequestDTO requestDTO) {
        if (requestDTO.cnpj() == null || requestDTO.cnpj().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo ou vazio");
        }

        // Validação do formato do CNPJ
        if (!isValidCnpj(requestDTO.cnpj())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ inválido");
        }

        String hashedCnpj = CryptoUtil.hash(requestDTO.cnpj());
        if (companyRepository.existsByHashedCnpj(hashedCnpj)) {
            throw new CnpjAlreadyExistsException("CNPJ já cadastrado: " + requestDTO.cnpj());
        }

        Company company = new Company();
        company.setName(requestDTO.name());
        company.setRawCnpj(requestDTO.cnpj());
        company.setHashedCnpj(hashedCnpj);
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);

        return CompanyResponseDTO.fromEntity(company);
    }

    public Page<CompanyResponseDTO> listCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companies = companyRepository.findAll(pageable);
        return companies.map(CompanyResponseDTO::fromEntity);
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));
    }

    public CompanyResponseDTO getCompanyResponseDTO(Long id) {
        Company company = findById(id);
        return CompanyResponseDTO.fromEntity(company);
    }

    @Cacheable("cnpjCache")
    public CompanyResponseDTO findByCnpj(String cnpj) {
        // Validação do formato do CNPJ
        if (!isValidCnpj(cnpj)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ inválido");
        }

        String hashedCnpj = CryptoUtil.hash(cnpj);
        Optional<Company> companyOptional = companyRepository.findByHashedCnpj(hashedCnpj);

        if (companyOptional.isPresent()) {
            // Empresa encontrada no banco de dados
            return CompanyResponseDTO.fromEntity(companyOptional.get());
        } else {
            // Empresa não encontrada, verificar CNPJ na API externa
            try {
                String url = BRASIL_API_URL + cnpj;
                ResponseEntity<ExternalCnpjResponse> response = restTemplate.getForEntity(url, ExternalCnpjResponse.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    // CNPJ existe na Receita Federal
                    ExternalCnpjResponse externalData = response.getBody();

                    // Criar CompanyResponseDTO usando o construtor do record
                    return new CompanyResponseDTO(
                            null, // id é null, pois a empresa não está no banco de dados
                            externalData.fantasia(), // name
                            cnpj, // cnpj
                            formatAddress(externalData), // address
                            externalData.telefone(), // phone
                            null // createdAt é null, pois não temos essa informação
                    );
                } else {
                    // CNPJ não encontrado na API externa
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CNPJ não encontrado na Receita Federal");
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CNPJ não encontrado na Receita Federal");
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao verificar CNPJ");
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao verificar CNPJ");
            }
        }
    }


    public CompanyResponseDTO updateCompany(Long id, UpdateComapnyRequestDTO requestDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        company.setName(requestDTO.name());
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);

        return CompanyResponseDTO.fromEntity(company);
    }

    public CompanyResponseDTO deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        companyRepository.deleteById(company.getId());

        return CompanyResponseDTO.fromEntity(company);
    }

    public Page<DepartmentResponseDTO> listDepartmentsByCompanyId(Long companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.findByCompanyId(companyId, pageable);

        return departmentPage.map(DepartmentResponseDTO::fromEntity);
    }

    public Page<UserResponseDTO> listUsersByCompanyId(Long companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByCompanyId(companyId, pageable);

        return userPage.map(UserResponseDTO::fromEntity);
    }

    // Método utilitário para validar o formato do CNPJ
    private boolean isValidCnpj(String cnpj) {
        // Implementação simplificada: verifica se tem 14 dígitos numéricos
        return cnpj != null && cnpj.matches("\\d{14}");
    }

    // Método utilitário para formatar o endereço
    private String formatAddress(ExternalCnpjResponse data) {
        StringBuilder address = new StringBuilder();
        address.append(data.logradouro())
                .append(", ")
                .append(data.numero());

        if (data.complemento() != null && !data.complemento().isEmpty()) {
            address.append(" - ").append(data.complemento());
        }

        address.append(", ")
                .append(data.bairro())
                .append(", ")
                .append(data.municipio())
                .append(" - ")
                .append(data.uf())
                .append(", CEP: ")
                .append(data.cep());

        return address.toString();
    }
}
