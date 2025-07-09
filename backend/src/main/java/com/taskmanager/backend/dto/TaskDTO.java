package com.taskmanager.backend.dto;

import com.taskmanager.backend.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskDTO {
    
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    private String title;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String description;
    
    @NotNull(message = "Data da tarefa é obrigatória")
    private LocalDate taskDate;
    
    private Boolean completed = false;
    private Task.Priority priority = Task.Priority.MEDIUM;
    private Task.Category category = Task.Category.PERSONAL;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    
    private Long userId;
    private String userName;
    
    // Constructors
    public TaskDTO() {}
    
    public TaskDTO(Long id, String title, String description, LocalDate taskDate, 
                   Boolean completed, Task.Priority priority, Task.Category category,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime completedAt,
                   Long userId, String userName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.completed = completed;
        this.priority = priority;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
        this.userId = userId;
        this.userName = userName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getTaskDate() {
        return taskDate;
    }
    
    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    public Task.Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Task.Priority priority) {
        this.priority = priority;
    }
    
    public Task.Category getCategory() {
        return category;
    }
    
    public void setCategory(Task.Category category) {
        this.category = category;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
}

