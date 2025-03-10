package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.AuthResponseDto;
import com.example.task_management_system.dto.LoginDto;
import com.example.task_management_system.dto.RegisterDto;
import com.example.task_management_system.models.Role;
import com.example.task_management_system.models.UserEntity;
import com.example.task_management_system.repository.RoleRepository;
import com.example.task_management_system.repository.UserRepository;
import com.example.task_management_system.security.JwtGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "API для аутентификации")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @Operation(summary = "Регистрация пользователя",
            description = "Метод для регистрации нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован успешно"),
            @ApiResponse(responseCode = "400", description = "Аккаунт с таким email уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Account with this email already exists.", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        if (registerDto.isAdmin()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            user.setRole(adminRole);
        } else {
            Role userRole = roleRepository.findByName("USER").orElseThrow();
            user.setRole(userRole);
        }

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @Operation(summary = "Вход в систему",
            description = "Метод для входа в систему и получения токена доступа.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponseDto(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
