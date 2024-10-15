package com.tecflux.service;

import com.tecflux.dto.company.*;
import com.tecflux.dto.department.DepartmentResponseDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.Role;
import com.tecflux.entity.User;
import com.tecflux.exception.CnpjAlreadyExistsException;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.repository.UserRepository;
import com.tecflux.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
public class CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final RoleService roleService;
    private static final String BRASIL_API_URL = System.getenv("BRASIL_API_URL");

    private static final String companyNotFound = "Empresa não encontrada.";

    public CompanyService(CompanyRepository companyRepository,
                          DepartmentRepository departmentRepository,
                          UserRepository userRepository,
                          RestTemplate restTemplate,
                          PasswordEncoder passwordEncoder,
                          RoleService roleService) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public CompanyResponseDTO createCompany(CreateCompanyWithDepartmentAndUserRequestDTO requestDTO) {
        validateCnpj(requestDTO.cnpj());

        String hashedCnpj = CryptoUtil.hash(requestDTO.cnpj());
        if (companyRepository.findByHashedCnpj(hashedCnpj).isPresent()) {
            throw new CnpjAlreadyExistsException("CNPJ já cadastrado: " + requestDTO.cnpj());
        }

        Company company = new Company();
        company.setName(requestDTO.companyName());
        company.setRawCnpj(requestDTO.cnpj());
        company.setHashedCnpj(hashedCnpj);
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());
        companyRepository.save(company);

        Department department = new Department();
        department.setName("Geral");
        department.setDescription("Departamento geral da empresa");
        department.setCompany(company);
        departmentRepository.save(department);

        String emailHash = CryptoUtil.hash(requestDTO.userEmail());
        Role masterRole = roleService.findByName("ROLE_MASTER")
                .orElseThrow(() -> new IllegalArgumentException("Role 'ROLE_MASTER' não encontrada"));

        User user =  new User();
        user.setName(requestDTO.userName());
        user.setRawEmail(requestDTO.userEmail());
        user.setEmailHash(emailHash);
        user.setPassword(passwordEncoder.encode(requestDTO.userPassword()));
        user.setPhone(requestDTO.userPhone());
        user.setPosition(requestDTO.userPosition());
        user.setCompany(company);
        user.setDepartment(department);
        user.setRoles(Set.of(masterRole));
        userRepository.save(user);


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
        return CompanyResponseDTO.fromEntity(findById(id));
    }

    public CompanyResponseDTO findByCnpj(String cnpj) {
        validateCnpj(cnpj);

        String hashedCnpj = CryptoUtil.hash(cnpj);
        Optional<Company> companyOptional = companyRepository.findByHashedCnpj(hashedCnpj);

        if (companyOptional.isPresent()) {
            logger.info("Empresa encontrada no banco de dados para CNPJ: {}", cnpj);
            return CompanyResponseDTO.fromEntity(companyOptional.get());
        } else {
            logger.info("Empresa não encontrada no banco, verificando API externa para CNPJ: {}", cnpj);
            return checkExternalCnpj(cnpj);
        }
    }

    public CompanyResponseDTO updateCompany(Long id, UpdateComapnyRequestDTO requestDTO) {
        Company company = findById(id);

        company.setName(requestDTO.name());
        company.setRawAddress(requestDTO.address());
        company.setRawPhone(requestDTO.phone());

        companyRepository.save(company);

        return CompanyResponseDTO.fromEntity(company);
    }

    public CompanyResponseDTO deleteCompany(Long id) {
        Company company = findById(id);
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

    // Método utilitário para verificar o CNPJ na API externa
    private CompanyResponseDTO checkExternalCnpj(String cnpj) {
        try {
            String url = BRASIL_API_URL + cnpj;
            ResponseEntity<ExternalCnpjResponse> response = restTemplate.getForEntity(url, ExternalCnpjResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ExternalCnpjResponse externalData = response.getBody();
                return new CompanyResponseDTO(
                        null,
                        externalData.fantasia(),
                        cnpj,
                        formatAddress(externalData),
                        externalData.telefone(),
                        null
                );
            } else {
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

    private void validateCnpj(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ inválido");
        }
    }
}
