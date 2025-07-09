package com.taskmanager.backend.service;

import com.taskmanager.backend.dto.RegisterRequest;
import com.taskmanager.backend.dto.UserDTO;
import com.taskmanager.backend.entity.User;
import com.taskmanager.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Busca todos os usuários
     * @return Lista de UserDTO
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca usuário por ID
     * @param id ID do usuário
     * @return Optional contendo UserDTO se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    /**
     * Busca usuário por email
     * @param email Email do usuário
     * @return Optional contendo User se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Busca usuários por nome (busca parcial)
     * @param name Nome ou parte do nome
     * @return Lista de UserDTO
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Cria um novo usuário
     * @param registerRequest Dados do usuário a ser criado
     * @return UserDTO do usuário criado
     * @throws RuntimeException se email já existe ou senhas não coincidem
     */
    public UserDTO createUser(RegisterRequest registerRequest) {
        // Validar se as senhas coincidem
        if (!registerRequest.isPasswordMatching()) {
            throw new RuntimeException("As senhas não coincidem");
        }
        
        // Verificar se email já existe
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        // Criar novo usuário
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    /**
     * Atualiza um usuário existente
     * @param id ID do usuário
     * @param userDTO Dados atualizados do usuário
     * @return UserDTO do usuário atualizado
     * @throws RuntimeException se usuário não encontrado ou email já existe
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar se o novo email já existe (se foi alterado)
        if (!user.getEmail().equals(userDTO.getEmail()) && 
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    /**
     * Atualiza a senha de um usuário
     * @param id ID do usuário
     * @param newPassword Nova senha
     * @return UserDTO do usuário atualizado
     * @throws RuntimeException se usuário não encontrado
     */
    public UserDTO updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    /**
     * Deleta um usuário
     * @param id ID do usuário
     * @throws RuntimeException se usuário não encontrado
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
    
    /**
     * Verifica se um usuário existe
     * @param id ID do usuário
     * @return true se existe, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    /**
     * Conta o número total de usuários
     * @return Número total de usuários
     */
    @Transactional(readOnly = true)
    public long countTotalUsers() {
        return userRepository.countTotalUsers();
    }
    
    /**
     * Converte User para UserDTO
     * @param user Entidade User
     * @return UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    
    /**
     * Converte UserDTO para User (sem senha)
     * @param userDTO DTO do usuário
     * @return User
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        return user;
    }
}

