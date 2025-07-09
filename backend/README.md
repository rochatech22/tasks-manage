# Task Manager Backend

Este é o backend do sistema de gerenciamento de tarefas desenvolvido com Spring Boot, Java 17 e SQL Server/Azure.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (com JWT)
- **Spring Data JPA**
- **SQL Server** (Azure SQL Database)
- **H2 Database** (para desenvolvimento local)
- **Maven** (gerenciamento de dependências)

## Funcionalidades

### Autenticação e Autorização
- Registro de novos usuários
- Login com JWT
- Validação de tokens
- Proteção de rotas

### Gerenciamento de Usuários
- CRUD completo de usuários
- Atualização de perfil
- Alteração de senha
- Busca por nome

### Gerenciamento de Tarefas
- CRUD completo de tarefas
- Filtros por data, status, prioridade e categoria
- Busca por título
- Visualização por dia/semana/mês
- Estatísticas de tarefas

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/taskmanager/backend/
│   │   ├── config/          # Configurações (Security)
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositórios
│   │   ├── security/       # Classes de segurança (JWT, UserDetails)
│   │   ├── service/        # Serviços de negócio
│   │   └── TaskManagerBackendApplication.java
│   └── resources/
│       ├── application.properties           # Configuração principal
│       ├── application-dev.properties       # Configuração para desenvolvimento
│       ├── application-azure.properties     # Configuração para Azure
│       └── data.sql                        # Dados iniciais
└── test/                   # Testes unitários
```

## Configuração do Ambiente

### Desenvolvimento Local (H2)

1. Clone o repositório
2. Execute com o perfil de desenvolvimento:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. Acesse o console H2: http://localhost:8080/h2-console
   - URL: `jdbc:h2:mem:taskmanager`
   - Username: `sa`
   - Password: (vazio)

### Produção (Azure SQL Database)

1. Configure as variáveis de ambiente:
```bash
export AZURE_SQL_SERVER=your-server-name
export AZURE_SQL_DATABASE=taskmanager
export AZURE_SQL_USERNAME=your-username
export AZURE_SQL_PASSWORD=your-password
export JWT_SECRET=your-jwt-secret-key
```

2. Execute com o perfil Azure:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=azure
```

## API Endpoints

### Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login
- `GET /api/auth/validate` - Validar token
- `POST /api/auth/logout` - Fazer logout

### Usuários
- `GET /api/users` - Listar todos os usuários
- `GET /api/users/{id}` - Buscar usuário por ID
- `GET /api/users/profile` - Buscar perfil do usuário autenticado
- `PUT /api/users/{id}` - Atualizar usuário
- `PUT /api/users/{id}/password` - Alterar senha
- `DELETE /api/users/{id}` - Deletar usuário
- `GET /api/users/search?name={name}` - Buscar por nome
- `GET /api/users/count` - Contar total de usuários

### Tarefas
- `GET /api/tasks` - Listar todas as tarefas do usuário
- `GET /api/tasks/{id}` - Buscar tarefa por ID
- `POST /api/tasks` - Criar nova tarefa
- `PUT /api/tasks/{id}` - Atualizar tarefa
- `PATCH /api/tasks/{id}/toggle` - Alternar status de conclusão
- `DELETE /api/tasks/{id}` - Deletar tarefa
- `GET /api/tasks/date/{date}` - Buscar por data específica
- `GET /api/tasks/period?startDate={start}&endDate={end}` - Buscar por período
- `GET /api/tasks/week` - Tarefas da semana atual
- `GET /api/tasks/month` - Tarefas do mês atual
- `GET /api/tasks/status/{completed}` - Buscar por status
- `GET /api/tasks/priority/{priority}` - Buscar por prioridade
- `GET /api/tasks/category/{category}` - Buscar por categoria
- `GET /api/tasks/search?title={title}` - Buscar por título
- `GET /api/tasks/stats` - Estatísticas das tarefas

## Modelos de Dados

### User
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@teste.com",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Task
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Revisar conceitos para entrevista",
  "taskDate": "2024-01-01",
  "completed": false,
  "priority": "HIGH",
  "category": "STUDY",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "completedAt": null,
  "userId": 1,
  "userName": "João Silva"
}
```

### Enums

**Priority**: LOW, MEDIUM, HIGH, URGENT
**Category**: PERSONAL, WORK, STUDY, HEALTH, FINANCE, OTHER

## Segurança

- Autenticação baseada em JWT
- Senhas criptografadas com BCrypt
- CORS configurado para permitir requisições do frontend
- Validação de entrada em todos os endpoints
- Autorização baseada em propriedade (usuários só podem acessar suas próprias tarefas)

## Testes

Execute os testes com:
```bash
mvn test
```

## Build e Deploy

### Build local
```bash
mvn clean package
```

### Executar JAR
```bash
java -jar target/task-manager-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=azure
```

## Configuração do Azure SQL Database

1. Crie um Azure SQL Database
2. Configure as regras de firewall
3. Execute os scripts de criação das tabelas (DDL será criado automaticamente pelo Hibernate)
4. Configure as variáveis de ambiente com as credenciais

## Monitoramento e Logs

- Logs configurados para diferentes níveis por ambiente
- Métricas de performance disponíveis via Spring Actuator (se habilitado)
- Logs de SQL habilitados em desenvolvimento

## Próximos Passos

- Implementar cache com Redis
- Adicionar testes de integração
- Implementar notificações
- Adicionar métricas e monitoramento
- Implementar backup automático

