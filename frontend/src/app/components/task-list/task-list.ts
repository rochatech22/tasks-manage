import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task, Priority, Category } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-list.html',
  styleUrls: ['./task-list.scss']
})
export class TaskListComponent {
  @Input() tasks: Task[] = [];
  @Output() editTask = new EventEmitter<Task>();
  @Output() deleteTask = new EventEmitter<number>();
  @Output() toggleTask = new EventEmitter<Task>();

  constructor(private taskService: TaskService) {}

  onEditTask(task: Task) {
    this.editTask.emit(task);
  }

  onDeleteTask(taskId: number) {
    this.deleteTask.emit(taskId);
  }

  onToggleTask(task: Task) {
    this.toggleTask.emit(task);
  }

  getPriorityLabel(priority: Priority): string {
    return this.taskService.getPriorityLabel(priority);
  }

  getCategoryLabel(category: Category): string {
    return this.taskService.getCategoryLabel(category);
  }

  getPriorityColor(priority: Priority): string {
    return this.taskService.getPriorityColor(priority);
  }

  getCategoryColor(category: Category): string {
    return this.taskService.getCategoryColor(category);
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  }

  isOverdue(task: Task): boolean {
    if (task.completed) return false;
    const today = new Date();
    const taskDate = new Date(task.taskDate);
    today.setHours(0, 0, 0, 0);
    taskDate.setHours(0, 0, 0, 0);
    return taskDate < today;
  }

  isToday(task: Task): boolean {
    const today = new Date();
    const taskDate = new Date(task.taskDate);
    return today.toDateString() === taskDate.toDateString();
  }

  sortedTasks(): Task[] {
    return [...this.tasks].sort((a, b) => {
      // Primeiro por status (pendentes primeiro)
      if (a.completed !== b.completed) {
        return a.completed ? 1 : -1;
      }
      
      // Depois por data
      const dateA = new Date(a.taskDate);
      const dateB = new Date(b.taskDate);
      if (dateA.getTime() !== dateB.getTime()) {
        return dateA.getTime() - dateB.getTime();
      }
      
      // Por último por prioridade
      const priorityOrder = { URGENT: 0, HIGH: 1, MEDIUM: 2, LOW: 3 };
      return priorityOrder[a.priority] - priorityOrder[b.priority];
    });
  }
}

