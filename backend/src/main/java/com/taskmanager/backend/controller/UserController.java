package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.UserDTO;
import com.taskmanager.backend.security.JwtUtil;
import com.taskmanager.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Busca todos os usuários
     * @return Lista de usuários
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Busca usuário por ID
     * @param id ID do usuário
     * @return Usuário encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Busca usuários por nome
     * @param name Nome ou parte do nome
     * @return Lista de usuários encontrados
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> getUsersByName(@RequestParam String name) {
        List<UserDTO> users = userService.findByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Busca o perfil do usuário autenticado
     * @param authorizationHeader Header Authorization com token
     * @return Dados do usuário autenticado
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            Long userId = jwtUtil.extractUserId(token);
            
            Optional<UserDTO> user = userService.findById(userId);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar perfil do usuário");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Atualiza dados do usuário
     * @param id ID do usuário
     * @param userDTO Dados atualizados
     * @param authorizationHeader Header Authorization com token
     * @return Usuário atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, 
                                       @Valid @RequestBody UserDTO userDTO,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            Long authenticatedUserId = jwtUtil.extractUserId(token);
            
            // Verificar se o usuário está tentando atualizar seus próprios dados
            if (!id.equals(authenticatedUserId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Acesso negado");
                error.put("message", "Você só pode atualizar seus próprios dados");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de validação");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Atualiza senha do usuário
     * @param id ID do usuário
     * @param passwordData Dados da nova senha
     * @param authorizationHeader Header Authorization com token
     * @return Confirmação de atualização
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id,
                                          @RequestBody Map<String, String> passwordData,
                                          @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            Long authenticatedUserId = jwtUtil.extractUserId(token);
            
            // Verificar se o usuário está tentando atualizar sua própria senha
            if (!id.equals(authenticatedUserId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Acesso negado");
                error.put("message", "Você só pode atualizar sua própria senha");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            
            String newPassword = passwordData.get("newPassword");
            if (newPassword == null || newPassword.length() < 6) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Senha inválida");
                error.put("message", "A senha deve ter pelo menos 6 caracteres");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            userService.updatePassword(id, newPassword);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Senha atualizada com sucesso");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de validação");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Deleta usuário
     * @param id ID do usuário
     * @param authorizationHeader Header Authorization com token
     * @return Confirmação de deleção
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            Long authenticatedUserId = jwtUtil.extractUserId(token);
            
            // Verificar se o usuário está tentando deletar sua própria conta
            if (!id.equals(authenticatedUserId)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Acesso negado");
                error.put("message", "Você só pode deletar sua própria conta");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            
            userService.deleteUser(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário deletado com sucesso");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de validação");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Conta o número total de usuários
     * @return Número total de usuários
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getTotalUsers() {
        long totalUsers = userService.countTotalUsers();
        Map<String, Long> response = new HashMap<>();
        response.put("totalUsers", totalUsers);
        return ResponseEntity.ok(response);
    }
}

