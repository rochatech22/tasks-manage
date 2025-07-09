import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { TaskService } from '../../services/task.service';
import { TaskListComponent } from '../task-list/task-list';
import { TaskFormComponent } from '../task-form/task-form';
import { User } from '../../models/user.model';
import { Task, TaskStats } from '../../models/task.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TaskListComponent, TaskFormComponent],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  tasks: Task[] = [];
  stats: TaskStats = { totalTasks: 0, completedTasks: 0, pendingTasks: 0 };
  showTaskForm = false;
  selectedTask: Task | null = null;
  currentView = 'all'; // all, today, week, month
  loading = false;

  constructor(
    private authService: AuthService,
    private taskService: TaskService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.loadTasks();
        this.loadStats();
      }
    });
  }

  loadTasks() {
    this.loading = true;
    
    let taskObservable;
    
    switch (this.currentView) {
      case 'today':
        const today = new Date().toISOString().split('T')[0];
        taskObservable = this.taskService.getTasksByDate(today);
        break;
      case 'week':
        taskObservable = this.taskService.getTasksForCurrentWeek();
        break;
      case 'month':
        taskObservable = this.taskService.getTasksForCurrentMonth();
        break;
      default:
        taskObservable = this.taskService.getAllTasks();
    }

    taskObservable.subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar tarefas:', error);
        this.loading = false;
      }
    });
  }

  loadStats() {
    this.taskService.getTaskStats().subscribe({
      next: (stats) => {
        this.stats = stats;
      },
      error: (error) => {
        console.error('Erro ao carregar estatísticas:', error);
      }
    });
  }

  onViewChange(view: string) {
    this.currentView = view;
    this.loadTasks();
  }

  onNewTask() {
    this.selectedTask = null;
    this.showTaskForm = true;
  }

  onEditTask(task: Task) {
    this.selectedTask = task;
    this.showTaskForm = true;
  }

  onDeleteTask(taskId: number) {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.taskService.deleteTask(taskId).subscribe({
        next: () => {
          this.loadTasks();
          this.loadStats();
        },
        error: (error) => {
          console.error('Erro ao excluir tarefa:', error);
        }
      });
    }
  }

  onToggleTask(task: Task) {
    this.taskService.toggleTaskCompletion(task.id!, !task.completed).subscribe({
      next: () => {
        this.loadTasks();
        this.loadStats();
      },
      error: (error) => {
        console.error('Erro ao alterar status da tarefa:', error);
      }
    });
  }

  onTaskSaved() {
    this.showTaskForm = false;
    this.selectedTask = null;
    this.loadTasks();
    this.loadStats();
  }

  onTaskFormCanceled() {
    this.showTaskForm = false;
    this.selectedTask = null;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  getCompletionPercentage(): number {
    if (this.stats.totalTasks === 0) return 0;
    return Math.round((this.stats.completedTasks / this.stats.totalTasks) * 100);
  }
}

