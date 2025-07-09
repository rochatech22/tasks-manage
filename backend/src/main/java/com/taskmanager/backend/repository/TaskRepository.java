package com.taskmanager.backend.repository;

import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Busca todas as tarefas de um usuário
     * @param user Usuário
     * @return Lista de tarefas do usuário
     */
    List<Task> findByUser(User user);
    
    /**
     * Busca todas as tarefas de um usuário ordenadas por data
     * @param user Usuário
     * @return Lista de tarefas ordenadas por data
     */
    List<Task> findByUserOrderByTaskDateAsc(User user);
    
    /**
     * Busca tarefas de um usuário por data específica
     * @param user Usuário
     * @param taskDate Data da tarefa
     * @return Lista de tarefas na data especificada
     */
    List<Task> findByUserAndTaskDate(User user, LocalDate taskDate);
    
    /**
     * Busca tarefas de um usuário em um período
     * @param user Usuário
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de tarefas no período
     */
    List<Task> findByUserAndTaskDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca tarefas de um usuário por status de conclusão
     * @param user Usuário
     * @param completed Status de conclusão
     * @return Lista de tarefas com o status especificado
     */
    List<Task> findByUserAndCompleted(User user, Boolean completed);
    
    /**
     * Busca tarefas de um usuário por prioridade
     * @param user Usuário
     * @param priority Prioridade
     * @return Lista de tarefas com a prioridade especificada
     */
    List<Task> findByUserAndPriority(User user, Task.Priority priority);
    
    /**
     * Busca tarefas de um usuário por categoria
     * @param user Usuário
     * @param category Categoria
     * @return Lista de tarefas da categoria especificada
     */
    List<Task> findByUserAndCategory(User user, Task.Category category);
    
    /**
     * Busca tarefas por título (busca parcial, case insensitive)
     * @param user Usuário
     * @param title Título ou parte do título
     * @return Lista de tarefas encontradas
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Task> findByUserAndTitleContainingIgnoreCase(@Param("user") User user, @Param("title") String title);
    
    /**
     * Conta tarefas concluídas de um usuário
     * @param user Usuário
     * @return Número de tarefas concluídas
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.completed = true")
    long countCompletedTasksByUser(@Param("user") User user);
    
    /**
     * Conta tarefas pendentes de um usuário
     * @param user Usuário
     * @return Número de tarefas pendentes
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.completed = false")
    long countPendingTasksByUser(@Param("user") User user);
    
    /**
     * Busca tarefas da semana atual de um usuário
     * @param user Usuário
     * @param startOfWeek Início da semana
     * @param endOfWeek Fim da semana
     * @return Lista de tarefas da semana
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.taskDate BETWEEN :startOfWeek AND :endOfWeek ORDER BY t.taskDate ASC")
    List<Task> findTasksForWeek(@Param("user") User user, @Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);
    
    /**
     * Busca tarefas do mês atual de um usuário
     * @param user Usuário
     * @param startOfMonth Início do mês
     * @param endOfMonth Fim do mês
     * @return Lista de tarefas do mês
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.taskDate BETWEEN :startOfMonth AND :endOfMonth ORDER BY t.taskDate ASC")
    List<Task> findTasksForMonth(@Param("user") User user, @Param("startOfMonth") LocalDate startOfMonth, @Param("endOfMonth") LocalDate endOfMonth);
}

