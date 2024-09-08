package com.tecflux.controller;


import com.tecflux.dto.user.RegisterUserRequestDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.service.UserService;
import com.tecflux.util.EmailUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailUtil emailUtil;

    public UserController(UserService userService, EmailUtil emailUtil) {
        this.userService = userService;
        this.emailUtil = emailUtil;
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

}
