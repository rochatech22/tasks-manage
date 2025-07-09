package com.taskmanager.backend.repository;

import com.taskmanager.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca um usuário pelo email
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica se existe um usuário com o email especificado
     * @param email Email a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários pelo nome (busca parcial, case insensitive)
     * @param name Nome ou parte do nome
     * @return Lista de usuários encontrados
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    java.util.List<User> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Conta o número total de usuários cadastrados
     * @return Número total de usuários
     */
    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();
}

