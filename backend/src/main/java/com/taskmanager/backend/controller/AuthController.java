package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.*;
import com.taskmanager.backend.entity.User;
import com.taskmanager.backend.security.CustomUserDetailsService;
import com.taskmanager.backend.security.JwtUtil;
import com.taskmanager.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Endpoint para login de usuário
     * @param loginRequest Dados de login
     * @return Token JWT e dados do usuário
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar usuário
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );
            
            // Carregar detalhes do usuário
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            User user = userDetailsService.loadUserEntityByEmail(loginRequest.getEmail());
            
            // Gerar token JWT
            String jwt = jwtUtil.generateToken(userDetails, user.getId(), user.getName());
            
            // Criar DTO do usuário
            UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );
            
            // Retornar resposta de autenticação
            AuthResponse authResponse = new AuthResponse(jwt, userDTO, "Login realizado com sucesso");
            
            return ResponseEntity.ok(authResponse);
            
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas");
            error.put("message", "Email ou senha incorretos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            error.put("message", "Erro ao processar login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Endpoint para registro de novo usuário
     * @param registerRequest Dados de registro
     * @return Token JWT e dados do usuário criado
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Criar usuário
            UserDTO userDTO = userService.createUser(registerRequest);
            
            // Autenticar o usuário recém-criado
            UserDetails userDetails = userDetailsService.loadUserByUsername(registerRequest.getEmail());
            User user = userDetailsService.loadUserEntityByEmail(registerRequest.getEmail());
            
            // Gerar token JWT
            String jwt = jwtUtil.generateToken(userDetails, user.getId(), user.getName());
            
            // Retornar resposta de autenticação
            AuthResponse authResponse = new AuthResponse(jwt, userDTO, "Usuário registrado com sucesso");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de validação");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            error.put("message", "Erro ao processar registro");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Endpoint para validar token JWT
     * @param authorizationHeader Header Authorization com token
     * @return Status de validação do token
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                
                if (jwtUtil.isTokenValid(token)) {
                    String email = jwtUtil.extractUsername(token);
                    Long userId = jwtUtil.extractUserId(token);
                    String name = jwtUtil.extractName(token);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", true);
                    response.put("email", email);
                    response.put("userId", userId);
                    response.put("name", name);
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Token inválido ou expirado");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Erro ao validar token");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para logout (invalidação do token no lado cliente)
     * @return Mensagem de logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout realizado com sucesso");
        return ResponseEntity.ok(response);
    }
}

