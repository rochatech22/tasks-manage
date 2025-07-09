import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task, TaskStats, Priority, Category } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  // CRUD básico
  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  getTaskById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  updateTask(id: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, task);
  }

  deleteTask(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  toggleTaskCompletion(id: number, completed: boolean): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${id}/toggle`, { completed });
  }

  // Filtros e buscas
  getTasksByDate(date: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/date/${date}`);
  }

  getTasksByPeriod(startDate: string, endDate: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/period?startDate=${startDate}&endDate=${endDate}`);
  }

  getTasksForCurrentWeek(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/week`);
  }

  getTasksForCurrentMonth(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/month`);
  }

  getTasksByStatus(completed: boolean): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/status/${completed}`);
  }

  getTasksByPriority(priority: Priority): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/priority/${priority}`);
  }

  getTasksByCategory(category: Category): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/category/${category}`);
  }

  searchTasksByTitle(title: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/search?title=${title}`);
  }

  // Estatísticas
  getTaskStats(): Observable<TaskStats> {
    return this.http.get<TaskStats>(`${this.apiUrl}/stats`);
  }

  // Métodos utilitários
  getPriorityLabel(priority: Priority): string {
    const labels = {
      [Priority.LOW]: 'Baixa',
      [Priority.MEDIUM]: 'Média',
      [Priority.HIGH]: 'Alta',
      [Priority.URGENT]: 'Urgente'
    };
    return labels[priority];
  }

  getCategoryLabel(category: Category): string {
    const labels = {
      [Category.PERSONAL]: 'Pessoal',
      [Category.WORK]: 'Trabalho',
      [Category.STUDY]: 'Estudos',
      [Category.HEALTH]: 'Saúde',
      [Category.FINANCE]: 'Finanças',
      [Category.OTHER]: 'Outros'
    };
    return labels[category];
  }

  getPriorityColor(priority: Priority): string {
    const colors = {
      [Priority.LOW]: '#4CAF50',
      [Priority.MEDIUM]: '#FF9800',
      [Priority.HIGH]: '#F44336',
      [Priority.URGENT]: '#9C27B0'
    };
    return colors[priority];
  }

  getCategoryColor(category: Category): string {
    const colors = {
      [Category.PERSONAL]: '#2196F3',
      [Category.WORK]: '#FF5722',
      [Category.STUDY]: '#9C27B0',
      [Category.HEALTH]: '#4CAF50',
      [Category.FINANCE]: '#FF9800',
      [Category.OTHER]: '#607D8B'
    };
    return colors[category];
  }
}

