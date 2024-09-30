package com.tecflux.service;

import com.tecflux.dto.user.*;
import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.User;
import com.tecflux.repository.UserRepository;
import com.tecflux.util.CryptoUtil;
import com.tecflux.util.EmailUtil;
import com.tecflux.util.PasswordGeneratorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final EmailUtil emailUtil;

    public UserService(
            UserRepository userRepository,
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

        System.out.println(generatedPassword);

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

    public void updatePassword(Long id, UpdatePasswordDTO requestDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(requestDTO.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Senha antiga incorreta");
        }

        user.setPassword(passwordEncoder.encode(requestDTO.newPassword()));

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        String emailHash = CryptoUtil.hash(email);
        return userRepository.findByEmailHash(emailHash);
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return UserResponseDTO.fromEntity(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Método para buscar usuários por empresa com paginação
    public Page<UserResponseDTO> getUsersByCompany(Long companyId, Pageable pageable) {
        Page<User> users = userRepository.findByCompany_Id(companyId, pageable);
        return users.map(UserResponseDTO::fromEntity);
    }

    // Método para buscar usuários por departamento com paginação
    public Page<UserResponseDTO> getUsersByDepartment(Long departmentId, Pageable pageable) {
        Page<User> users = userRepository.findByDepartment_Id(departmentId, pageable);
        return users.map(UserResponseDTO::fromEntity);
    }

    // Método para buscar usuários por role com paginação
    public Page<UserResponseDTO> getUsersByRole(String roleName, Pageable pageable) {
        Page<User> users = userRepository.findByRoles_Name(roleName, pageable);
        return users.map(UserResponseDTO::fromEntity);
    }

    // Método para atualizar os dados do usuário
        public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userUpdateDTO) {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isEmpty()) {
                throw new IllegalArgumentException("Usuário não encontrado.");
            }

            User user = optionalUser.get();

            // Atualiza nome se estiver presente
            if (userUpdateDTO.name() != null && !userUpdateDTO.name().isEmpty()) {
                user.setName(userUpdateDTO.name());
            }

            // Atualiza e-mail se estiver presente
            if (userUpdateDTO.email() != null && !userUpdateDTO.email().isEmpty()) {
                user.setRawEmail(userUpdateDTO.email());
            }

            // Atualiza senha se estiver presente
            if (userUpdateDTO.password() != null && !userUpdateDTO.password().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));
            }

            // Atualiza telefone se estiver presente
            if (userUpdateDTO.phone() != null && !userUpdateDTO.phone().isEmpty()) {
                user.setRawPhone(userUpdateDTO.phone());
            }

            // Atualiza departamento se estiver presente
            if (userUpdateDTO.departmentId() != null) {
                Department department = departmentService.findById(userUpdateDTO.departmentId());
                user.setDepartment(department);
            }

            // Atualiza roles se estiverem presentes
            if (userUpdateDTO.roles() != null && !userUpdateDTO.roles().isEmpty()) {
                Set<String> roleNames = userUpdateDTO.roles();
                user.setRoles(roleNames.stream()
                        .map(roleName -> roleService.findByName(roleName)
                                .orElseThrow(() -> new IllegalArgumentException("Role " + roleName + " não encontrada")))
                        .collect(Collectors.toSet()));
            }

            // Salva as alterações no banco de dados
            userRepository.save(user);

            // Retorna os dados atualizados do usuário
            return UserResponseDTO.fromEntity(user);
        }
}
