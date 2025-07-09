import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Task, Priority, Category } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-form.html',
  styleUrls: ['./task-form.scss']
})
export class TaskFormComponent implements OnInit {
  @Input() task: Task | null = null;
  @Output() taskSaved = new EventEmitter<void>();
  @Output() taskCanceled = new EventEmitter<void>();

  formData: Task = {
    title: '',
    description: '',
    taskDate: '',
    completed: false,
    priority: Priority.MEDIUM,
    category: Category.PERSONAL
  };

  priorities = [
    { value: Priority.LOW, label: 'Baixa' },
    { value: Priority.MEDIUM, label: 'Média' },
    { value: Priority.HIGH, label: 'Alta' },
    { value: Priority.URGENT, label: 'Urgente' }
  ];

  categories = [
    { value: Category.PERSONAL, label: 'Pessoal' },
    { value: Category.WORK, label: 'Trabalho' },
    { value: Category.STUDY, label: 'Estudos' },
    { value: Category.HEALTH, label: 'Saúde' },
    { value: Category.FINANCE, label: 'Finanças' },
    { value: Category.OTHER, label: 'Outros' }
  ];

  loading = false;
  error = '';

  constructor(private taskService: TaskService) {}

  ngOnInit() {
    if (this.task) {
      // Editando tarefa existente
      this.formData = { ...this.task };
    } else {
      // Nova tarefa - definir data padrão como hoje
      const today = new Date();
      this.formData.taskDate = today.toISOString().split('T')[0];
    }
  }

  onSubmit() {
    if (this.isFormValid()) {
      this.loading = true;
      this.error = '';

      const operation = this.task 
        ? this.taskService.updateTask(this.task.id!, this.formData)
        : this.taskService.createTask(this.formData);

      operation.subscribe({
        next: () => {
          this.loading = false;
          this.taskSaved.emit();
        },
        error: (error) => {
          this.loading = false;
          this.error = error.error?.message || 'Erro ao salvar tarefa';
        }
      });
    }
  }

  onCancel() {
    this.taskCanceled.emit();
  }

  isFormValid(): boolean {
    return this.formData.title.trim().length > 0 && 
           this.formData.taskDate.length > 0;
  }

  getFormTitle(): string {
    return this.task ? 'Editar Tarefa' : 'Nova Tarefa';
  }

  getSubmitButtonText(): string {
    if (this.loading) {
      return this.task ? 'Atualizando...' : 'Criando...';
    }
    return this.task ? 'Atualizar' : 'Criar';
  }
}

