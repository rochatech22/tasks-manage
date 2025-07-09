package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.TaskDTO;
import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.security.JwtUtil;
import com.taskmanager.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Extrai o ID do usuário do token JWT
     * @param authorizationHeader Header Authorization
     * @return ID do usuário
     */
    private Long extractUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        return jwtUtil.extractUserId(token);
    }
    
    /**
     * Busca todas as tarefas do usuário autenticado
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas
     */
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findAllByUser(userId);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefa por ID
     * @param id ID da tarefa
     * @param authorizationHeader Header Authorization com token
     * @return Tarefa encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id,
                                        @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            Optional<TaskDTO> task = taskService.findById(id, userId);
            
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Tarefa não encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar tarefa");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Busca tarefas por data específica
     * @param date Data da tarefa
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas na data especificada
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<TaskDTO>> getTasksByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findByUserAndDate(userId, date);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas em um período
     * @param startDate Data inicial
     * @param endDate Data final
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas no período
     */
    @GetMapping("/period")
    public ResponseEntity<List<TaskDTO>> getTasksByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findByUserAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas da semana atual
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas da semana
     */
    @GetMapping("/week")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentWeek(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findTasksForCurrentWeek(userId);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas do mês atual
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas do mês
     */
    @GetMapping("/month")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentMonth(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findTasksForCurrentMonth(userId);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas por status de conclusão
     * @param completed Status de conclusão
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas com o status especificado
     */
    @GetMapping("/status/{completed}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(
            @PathVariable Boolean completed,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findByUserAndCompleted(userId, completed);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas por prioridade
     * @param priority Prioridade
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas com a prioridade especificada
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskDTO>> getTasksByPriority(
            @PathVariable Task.Priority priority,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findByUserAndPriority(userId, priority);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas por categoria
     * @param category Categoria
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas da categoria especificada
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TaskDTO>> getTasksByCategory(
            @PathVariable Task.Category category,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findByUserAndCategory(userId, category);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Busca tarefas por título
     * @param title Título ou parte do título
     * @param authorizationHeader Header Authorization com token
     * @return Lista de tarefas encontradas
     */
    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> getTasksByTitle(
            @RequestParam String title,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        List<TaskDTO> tasks = taskService.findByUserAndTitle(userId, title);
        return ResponseEntity.ok(tasks);
    }
    
    /**
     * Cria uma nova tarefa
     * @param taskDTO Dados da tarefa
     * @param authorizationHeader Header Authorization com token
     * @return Tarefa criada
     */
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTO taskDTO,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            TaskDTO createdTask = taskService.createTask(taskDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
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
     * Atualiza uma tarefa existente
     * @param id ID da tarefa
     * @param taskDTO Dados atualizados da tarefa
     * @param authorizationHeader Header Authorization com token
     * @return Tarefa atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                       @Valid @RequestBody TaskDTO taskDTO,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            TaskDTO updatedTask = taskService.updateTask(id, taskDTO, userId);
            return ResponseEntity.ok(updatedTask);
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
     * Alterna o status de conclusão de uma tarefa
     * @param id ID da tarefa
     * @param statusData Dados do status
     * @param authorizationHeader Header Authorization com token
     * @return Tarefa atualizada
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTaskCompletion(@PathVariable Long id,
                                                  @RequestBody Map<String, Boolean> statusData,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            Boolean completed = statusData.get("completed");
            TaskDTO updatedTask = taskService.toggleTaskCompletion(id, completed, userId);
            return ResponseEntity.ok(updatedTask);
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
     * Deleta uma tarefa
     * @param id ID da tarefa
     * @param authorizationHeader Header Authorization com token
     * @return Confirmação de deleção
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id,
                                       @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            taskService.deleteTask(id, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Tarefa deletada com sucesso");
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
     * Obtém estatísticas das tarefas do usuário
     * @param authorizationHeader Header Authorization com token
     * @return Estatísticas das tarefas
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getTaskStats(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = extractUserIdFromToken(authorizationHeader);
        
        long completedTasks = taskService.countCompletedTasks(userId);
        long pendingTasks = taskService.countPendingTasks(userId);
        long totalTasks = completedTasks + pendingTasks;
        
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalTasks", totalTasks);
        stats.put("completedTasks", completedTasks);
        stats.put("pendingTasks", pendingTasks);
        
        return ResponseEntity.ok(stats);
    }
}

