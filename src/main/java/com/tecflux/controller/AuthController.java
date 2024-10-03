package com.tecflux.controller;

import com.tecflux.dto.ApiResponse;
import com.tecflux.dto.user.CreateUserRequestDTO;
import com.tecflux.dto.user.LoginRequestDTO;
import com.tecflux.dto.user.LoginResponseDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.User;
import com.tecflux.service.UserService;
import com.tecflux.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();

        LoginResponseDTO loginResponse = LoginResponseDTO.fromEntity(user, jwt);

        // Atualizar o lastLogin através do UserService
        userService.updateLastLogin(user.getId());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequestDTO signUpRequest) {
        // Verifica se o email já está em uso
        if (userService.findByEmail(signUpRequest.email()).isPresent()) {
            return new ResponseEntity<>(new ApiResponse("Esse email já está em uso!"), HttpStatus.BAD_REQUEST);
        }

        UserResponseDTO responseDTO = userService.saveUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
