-- Script de dados iniciais para desenvolvimento
-- Este script será executado automaticamente no perfil de desenvolvimento

-- Inserir usuário de teste (senha: 123456)
INSERT INTO users (name, email, password, created_at) VALUES 
('João Silva', 'joao@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lFkwRzQtRDrxbNfLG', CURRENT_TIMESTAMP),
('Maria Santos', 'maria@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lFkwRzQtRDrxbNfLG', CURRENT_TIMESTAMP);

-- Inserir tarefas de exemplo
INSERT INTO tasks (title, description, task_date, completed, priority, category, created_at, user_id) VALUES 
('Estudar Spring Boot', 'Revisar conceitos de Spring Boot para a entrevista', CURRENT_DATE, false, 'HIGH', 'STUDY', CURRENT_TIMESTAMP, 1),
('Fazer exercícios', 'Ir à academia pela manhã', CURRENT_DATE, false, 'MEDIUM', 'HEALTH', CURRENT_TIMESTAMP, 1),
('Reunião de trabalho', 'Participar da reunião de planejamento', CURRENT_DATE + 1, false, 'HIGH', 'WORK', CURRENT_TIMESTAMP, 1),
('Comprar mantimentos', 'Ir ao supermercado comprar comida para a semana', CURRENT_DATE + 2, false, 'LOW', 'PERSONAL', CURRENT_TIMESTAMP, 1),
('Revisar Angular', 'Estudar componentes e serviços do Angular', CURRENT_DATE, false, 'HIGH', 'STUDY', CURRENT_TIMESTAMP, 2),
('Pagar contas', 'Pagar conta de luz e água', CURRENT_DATE + 1, false, 'MEDIUM', 'FINANCE', CURRENT_TIMESTAMP, 2);

