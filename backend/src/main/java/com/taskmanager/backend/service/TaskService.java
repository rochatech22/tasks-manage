package com.taskmanager.backend.service;

import com.taskmanager.backend.dto.TaskDTO;
import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.entity.User;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Busca todas as tarefas de um usuário
     * @param userId ID do usuário
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findAllByUser(Long userId) {
        User user = getUserById(userId);
        return taskRepository.findByUserOrderByTaskDateAsc(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefa por ID
     * @param id ID da tarefa
     * @param userId ID do usuário (para verificar propriedade)
     * @return Optional contendo TaskDTO se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findById(Long id, Long userId) {
        return taskRepository.findById(id)
                .filter(task -> task.getUser().getId().equals(userId))
                .map(this::convertToDTO);
    }
    
    /**
     * Busca tarefas de um usuário por data específica
     * @param userId ID do usuário
     * @param date Data da tarefa
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByUserAndDate(Long userId, LocalDate date) {
        User user = getUserById(userId);
        return taskRepository.findByUserAndTaskDate(user, date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas de um usuário em um período
     * @param userId ID do usuário
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = getUserById(userId);
        return taskRepository.findByUserAndTaskDateBetween(user, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas da semana atual de um usuário
     * @param userId ID do usuário
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findTasksForCurrentWeek(Long userId) {
        User user = getUserById(userId);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        
        return taskRepository.findTasksForWeek(user, startOfWeek, endOfWeek).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas do mês atual de um usuário
     * @param userId ID do usuário
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findTasksForCurrentMonth(Long userId) {
        User user = getUserById(userId);
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        
        return taskRepository.findTasksForMonth(user, startOfMonth, endOfMonth).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas por status de conclusão
     * @param userId ID do usuário
     * @param completed Status de conclusão
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByUserAndCompleted(Long userId, Boolean completed) {
        User user = getUserById(userId);
        return taskRepository.findByUserAndCompleted(user, completed).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas por prioridade
     * @param userId ID do usuário
     * @param priority Prioridade
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByUserAndPriority(Long userId, Task.Priority priority) {
        User user = getUserById(userId);
        return taskRepository.findByUserAndPriority(user, priority).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas por categoria
     * @param userId ID do usuário
     * @param category Categoria
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByUserAndCategory(Long userId, Task.Category category) {
        User user = getUserById(userId);
        return taskRepository.findByUserAndCategory(user, category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca tarefas por título
     * @param userId ID do usuário
     * @param title Título ou parte do título
     * @return Lista de TaskDTO
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByUserAndTitle(Long userId, String title) {
        User user = getUserById(userId);
        return taskRepository.findByUserAndTitleContainingIgnoreCase(user, title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Cria uma nova tarefa
     * @param taskDTO Dados da tarefa
     * @param userId ID do usuário
     * @return TaskDTO da tarefa criada
     */
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        User user = getUserById(userId);
        
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setTaskDate(taskDTO.getTaskDate());
        task.setCompleted(taskDTO.getCompleted() != null ? taskDTO.getCompleted() : false);
        task.setPriority(taskDTO.getPriority() != null ? taskDTO.getPriority() : Task.Priority.MEDIUM);
        task.setCategory(taskDTO.getCategory() != null ? taskDTO.getCategory() : Task.Category.PERSONAL);
        task.setUser(user);
        
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
    
    /**
     * Atualiza uma tarefa existente
     * @param id ID da tarefa
     * @param taskDTO Dados atualizados da tarefa
     * @param userId ID do usuário (para verificar propriedade)
     * @return TaskDTO da tarefa atualizada
     */
    public TaskDTO updateTask(Long id, TaskDTO taskDTO, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        // Verificar se a tarefa pertence ao usuário
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado: tarefa não pertence ao usuário");
        }
        
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setTaskDate(taskDTO.getTaskDate());
        task.setCompleted(taskDTO.getCompleted());
        task.setPriority(taskDTO.getPriority());
        task.setCategory(taskDTO.getCategory());
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
    
    /**
     * Marca uma tarefa como concluída ou não concluída
     * @param id ID da tarefa
     * @param completed Status de conclusão
     * @param userId ID do usuário (para verificar propriedade)
     * @return TaskDTO da tarefa atualizada
     */
    public TaskDTO toggleTaskCompletion(Long id, Boolean completed, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        // Verificar se a tarefa pertence ao usuário
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado: tarefa não pertence ao usuário");
        }
        
        task.setCompleted(completed);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
    
    /**
     * Deleta uma tarefa
     * @param id ID da tarefa
     * @param userId ID do usuário (para verificar propriedade)
     */
    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        // Verificar se a tarefa pertence ao usuário
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado: tarefa não pertence ao usuário");
        }
        
        taskRepository.delete(task);
    }
    
    /**
     * Conta tarefas concluídas de um usuário
     * @param userId ID do usuário
     * @return Número de tarefas concluídas
     */
    @Transactional(readOnly = true)
    public long countCompletedTasks(Long userId) {
        User user = getUserById(userId);
        return taskRepository.countCompletedTasksByUser(user);
    }
    
    /**
     * Conta tarefas pendentes de um usuário
     * @param userId ID do usuário
     * @return Número de tarefas pendentes
     */
    @Transactional(readOnly = true)
    public long countPendingTasks(Long userId) {
        User user = getUserById(userId);
        return taskRepository.countPendingTasksByUser(user);
    }
    
    /**
     * Busca usuário por ID
     * @param userId ID do usuário
     * @return User
     * @throws RuntimeException se usuário não encontrado
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
    
    /**
     * Converte Task para TaskDTO
     * @param task Entidade Task
     * @return TaskDTO
     */
    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskDate(),
                task.getCompleted(),
                task.getPriority(),
                task.getCategory(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getCompletedAt(),
                task.getUser().getId(),
                task.getUser().getName()
        );
    }
}

