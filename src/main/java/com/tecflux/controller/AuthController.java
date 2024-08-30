package com.tecflux.controller;

import com.tecflux.dto.ApiResponse;
import com.tecflux.dto.user.CreateUserRequestDTO;
import com.tecflux.dto.user.LoginRequestDTO;
import com.tecflux.dto.user.LoginResponseDTO;
import com.tecflux.dto.user.UserResponseDTO;
import com.tecflux.entity.User;
import com.tecflux.repository.UserRepository;
import com.tecflux.service.UserService;
import com.tecflux.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;

    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {

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

        user.setLastLogin(Instant.now());
        userRepository.save(user);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequestDTO signUpRequest) {
        // Verifica se o email já está em uso
        if (userService.findByEmail(signUpRequest.email()).isPresent()) {
            return new ResponseEntity<>(new ApiResponse("Esse email já está em uso!"), HttpStatus.BAD_REQUEST);
        }

        UserResponseDTO responseDTO = userService.saveUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
