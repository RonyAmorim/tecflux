package com.tecflux.controller;


import com.tecflux.dto.user.RegisterUserRequestDTO;
import com.tecflux.dto.user.UpdatePasswordDTO;
import com.tecflux.dto.user.UpdateUserRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO registerUser(@Valid @RequestBody RegisterUserRequestDTO requestDTO) {
        return userService.registerUser(requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/update-password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        userService.updatePassword(id,updatePasswordDTO);
    }

    // Buscar usuários por empresa com paginação
    @GetMapping("/company/{companyId}")
    public Page<UserResponseDTO> getUsersByCompany(@PathVariable Long companyId, Pageable pageable) {
        return userService.getUsersByCompany(companyId, pageable);
    }

    // Buscar usuários por departamento com paginação
    @GetMapping("/department/{departmentId}")
    public Page<UserResponseDTO> getUsersByDepartment(@PathVariable Long departmentId, Pageable pageable) {
        return userService.getUsersByDepartment(departmentId, pageable);
    }

    // Buscar usuários por role com paginação
    @GetMapping("/role/{roleName}")
    public Page<UserResponseDTO> getUsersByRole(@PathVariable String roleName, Pageable pageable) {
        return userService.getUsersByRole(roleName, pageable);
    }

    @GetMapping("{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO userUpdateDTO) {
        return userService.updateUser(id, userUpdateDTO);
    }
}
