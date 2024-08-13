package com.tecflux.service;

import com.tecflux.dto.user.CreateUserRequestDTO;
import com.tecflux.dto.user.UpdateUserRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.Role;
import com.tecflux.entity.User;
import com.tecflux.repository.CompanyRepository;
import com.tecflux.repository.DepartmentRepository;
import com.tecflux.repository.RoleRepository;
import com.tecflux.repository.UserRepository;
import com.tecflux.util.CryptoUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    private final String userNotFound = "Usuário não encontrado.";
    private final String roleNotFound = "Role não encontrada.";
    private final String departmentNotFound = "Departamento não encontrado.";
    private final String companyNotFound = "Empresa não encontrada.";

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
    }

    public void createUser(CreateUserRequestDTO requestDTO) {
        User user = new User();
        user.setUsername(requestDTO.username());
        user.setRawEmail(requestDTO.email());
        user.setRawPassword(requestDTO.password());
        user.setRawPhone(requestDTO.phone());

        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));
        user.setDepartment(department);

        Company company = companyRepository.findById(requestDTO.companyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));
        user.setCompany(company);

        Set<Role> roles = requestDTO.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, roleNotFound)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        userRepository.save(user);
    }

    public Page<UserResponseDTO> listUsers(int page, int size) {
        var users = userRepository.findAll(PageRequest.of(page, size));
        return users.map(UserResponseDTO::fromEntity);
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, userNotFound));
        return UserResponseDTO.fromEntity(user);
    }

    public void updateUser(Long id, UpdateUserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, userNotFound));

        user.setUsername(requestDTO.username());
        user.setRawEmail(requestDTO.email());
        user.setRawPassword(requestDTO.password());
        user.setRawPhone(requestDTO.phone());

        Department department = departmentRepository.findById(requestDTO.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));
        user.setDepartment(department);

        Set<Role> roles = requestDTO.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, roleNotFound)))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, userNotFound));

        for (Role role : user.getRoles()) {
            role.getUsers().remove(user);
        }
        user.getRoles().clear();

        userRepository.delete(user);
    }

    public boolean validateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        String decryptedPassword = CryptoUtil.decrypt(user.getPassword());
        if(decryptedPassword.equals(password)){
            user.setLastLogin(Instant.now());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public Page<UserResponseDTO> listUsersByDepartment(Long departmentId, int page, int size) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, departmentNotFound));

        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByDepartment(department, pageable)
                .map(UserResponseDTO::fromEntity);
    }

    public Page<UserResponseDTO> listUsersByCompany(Long companyId, int page, int size) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, companyNotFound));

        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByCompany(company, pageable)
                .map(UserResponseDTO::fromEntity);
    }
}
