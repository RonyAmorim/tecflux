package com.tecflux.controller;

import com.tecflux.dto.user.UpdateUserRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var users = userService.listUsers(page, pageSize);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable(value = "id") Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable(value = "id") Long id,
                                           @RequestBody UpdateUserRequestDTO requestDTO) {
        userService.updateUser(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByDepartment(@PathVariable Long departmentId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDTO> users = userService.listUsersByDepartment(departmentId, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByCompany(@PathVariable Long companyId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDTO> users = userService.listUsersByCompany(companyId, page, size);
        return ResponseEntity.ok(users);
    }
}
