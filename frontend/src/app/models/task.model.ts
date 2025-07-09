export interface Task {
  id?: number;
  title: string;
  description?: string;
  taskDate: string;
  completed: boolean;
  priority: Priority;
  category: Category;
  createdAt?: string;
  updatedAt?: string;
  completedAt?: string;
  userId?: number;
  userName?: string;
}

export enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  URGENT = 'URGENT'
}

export enum Category {
  PERSONAL = 'PERSONAL',
  WORK = 'WORK',
  STUDY = 'STUDY',
  HEALTH = 'HEALTH',
  FINANCE = 'FINANCE',
  OTHER = 'OTHER'
}

export interface TaskStats {
  totalTasks: number;
  completedTasks: number;
  pendingTasks: number;
}

