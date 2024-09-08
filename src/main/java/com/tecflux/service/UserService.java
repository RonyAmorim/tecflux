package com.tecflux.service;

import com.tecflux.dto.user.CreateUserRequestDTO;
import com.tecflux.dto.user.RegisterUserRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.User;
import com.tecflux.repository.UserRepository;
import com.tecflux.util.CryptoUtil;
import com.tecflux.util.EmailUtil;
import com.tecflux.util.PasswordGeneratorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final EmailUtil emailUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       CompanyService companyService,
                       DepartmentService departmentService,
                       RoleService roleService,
                       EmailUtil emailUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.emailUtil = emailUtil;
    }

    public UserResponseDTO saveUser(CreateUserRequestDTO requestDTO) {
        Company company = null;
        Department department = null;

        if (requestDTO.companyId() != null){
            company = companyService.findById(requestDTO.companyId());
        }

        if(requestDTO.departmentId() != null){
            department = departmentService.findById(requestDTO.departmentId());
        }

        String emailHash = CryptoUtil.hash(requestDTO.email());

        User user = new User();
        user.setName(requestDTO.name());
        user.setRawEmail(requestDTO.email());
        user.setEmailHash(emailHash);

        user.setPassword(passwordEncoder.encode(requestDTO.password()));

        user.setRawPhone(requestDTO.phone());
        user.setCompany(company);
        user.setDepartment(department);
        user.setRoles(requestDTO.roles().stream()
                .map(roleName -> roleService.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + roleName + " não encontrada")))
                .collect(Collectors.toSet()));

        userRepository.save(user);

        UserResponseDTO responseDTO = UserResponseDTO.fromEntity(userRepository.save(user));

        return responseDTO;
    }

    public UserResponseDTO registerUser(RegisterUserRequestDTO requestDTO) {
        Company company = null;
        Department department = null;

        if (requestDTO.companyId() != null) {
            company = companyService.findById(requestDTO.companyId());
        }

        if (requestDTO.departmentId() != null) {
            department = departmentService.findById(requestDTO.departmentId());
        }

        String emailHash = CryptoUtil.hash(requestDTO.email());

        // Geração da senha do usuário
        String generatedPassword = PasswordGeneratorUtil.generateRandomPassword();

        // Criação do usuário
        User user = new User();
        user.setName(requestDTO.name());  // Use o nome real da requisição
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setRawEmail(requestDTO.email());
        user.setRawPhone(requestDTO.phone());
        user.setEmailHash(emailHash);
        user.setCompany(company);
        user.setDepartment(department);
        user.setRoles(requestDTO.roles().stream()
                .map(roleName -> roleService.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + roleName + " não encontrada")))
                .collect(Collectors.toSet()));

        // Salvando o usuário
        userRepository.save(user);

        // Envio do e-mail de boas-vindas
        String nomeUsuario = requestDTO.name();
        String emailUsuario = requestDTO.email();
        String linkPlataforma = "http://localhost:4200/login";

        // Chamando o método de envio de e-mail
        emailUtil.sendWelcomeEmail(
                emailUsuario,
                nomeUsuario,
                emailUsuario,
                generatedPassword,
                linkPlataforma
        );

        // Retorna o DTO com as informações do usuário criado
        return UserResponseDTO.fromEntity(user);
    }


    public Optional<User> findByEmail(String email) {
        String emailHash = CryptoUtil.hash(email);
        return userRepository.findByEmailHash(emailHash);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
