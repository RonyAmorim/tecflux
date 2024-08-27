package com.tecflux.service;

import com.tecflux.dto.user.CreateUserRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.User;
import com.tecflux.repository.UserRepository;
import com.tecflux.util.CryptoUtil;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyService companyService, DepartmentService departmentService, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.departmentService = departmentService;
        this.roleService = roleService;
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
        user.setUsername(requestDTO.username());
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
