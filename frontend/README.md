# Task Manager Frontend

Este é o frontend do sistema de gerenciamento de tarefas desenvolvido com Angular 18.

## Tecnologias Utilizadas

- **Angular 18**
- **TypeScript**
- **SCSS** (Sass)
- **RxJS** (para programação reativa)
- **Angular Router** (para navegação)
- **Angular Forms** (para formulários)

## Funcionalidades

### Autenticação
- Login de usuários
- Registro de novos usuários
- Proteção de rotas com guards
- Interceptor para adicionar token JWT automaticamente

### Dashboard
- Visão geral das tarefas
- Estatísticas (total, concluídas, pendentes, progresso)
- Filtros por período (todas, hoje, semana, mês)
- Interface responsiva e moderna

### Gerenciamento de Tarefas
- Listagem de tarefas com filtros visuais
- Criação de novas tarefas
- Edição de tarefas existentes
- Marcação de tarefas como concluídas
- Exclusão de tarefas
- Categorização por prioridade e categoria
- Indicadores visuais para tarefas atrasadas e do dia

## Estrutura do Projeto

```
src/
├── app/
│   ├── components/          # Componentes da aplicação
│   │   ├── dashboard/       # Dashboard principal
│   │   ├── login/          # Tela de login
│   │   ├── register/       # Tela de registro
│   │   ├── task-list/      # Lista de tarefas
│   │   └── task-form/      # Formulário de tarefas
│   ├── guards/             # Guards de autenticação
│   ├── interceptors/       # Interceptors HTTP
│   ├── models/            # Interfaces e modelos
│   ├── services/          # Serviços
│   ├── app.config.ts      # Configuração da aplicação
│   ├── app.routes.ts      # Configuração de rotas
│   └── app.ts            # Componente raiz
├── styles.scss           # Estilos globais
└── index.html           # Página principal
```

## Configuração e Execução

### Pré-requisitos
- Node.js 18+ 
- npm ou yarn
- Angular CLI

### Instalação
```bash
# Instalar dependências
npm install

# Instalar Angular CLI globalmente (se necessário)
npm install -g @angular/cli
```

### Desenvolvimento
```bash
# Executar em modo de desenvolvimento
ng serve

# Ou especificar porta
ng serve --port 4200
```

A aplicação estará disponível em `http://localhost:4200`

### Build para Produção
```bash
# Build otimizado para produção
ng build --configuration production

# Os arquivos serão gerados na pasta dist/
```

## Configuração da API

O frontend está configurado para se comunicar com o backend em `http://localhost:8080`.

Para alterar a URL da API, edite os arquivos de serviço em `src/app/services/`:
- `auth.service.ts`
- `task.service.ts`

## Componentes Principais

### AuthService
- Gerencia autenticação de usuários
- Armazena token JWT no localStorage
- Fornece observables para estado de autenticação

### TaskService
- Gerencia operações CRUD de tarefas
- Fornece métodos para filtros e buscas
- Utilitários para formatação e cores

### AuthGuard
- Protege rotas que requerem autenticação
- Redireciona para login se não autenticado

### AuthInterceptor
- Adiciona automaticamente token JWT nas requisições
- Intercepta todas as chamadas HTTP

## Funcionalidades por Tela

### Login (`/login`)
- Formulário de login com validação
- Redirecionamento automático após login
- Link para registro

### Registro (`/register`)
- Formulário de registro com validação
- Confirmação de senha
- Criação automática de conta

### Dashboard (`/dashboard`)
- Cards de estatísticas
- Filtros de visualização
- Lista de tarefas interativa
- Modal para criar/editar tarefas

## Modelos de Dados

### User
```typescript
interface User {
  id: number;
  name: string;
  email: string;
  createdAt: string;
  updatedAt?: string;
}
```

### Task
```typescript
interface Task {
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
```

## Estilos e Design

### Sistema de Cores
- Primária: `#667eea` (azul)
- Sucesso: `#28a745` (verde)
- Perigo: `#dc3545` (vermelho)
- Aviso: `#ffc107` (amarelo)
- Secundária: `#6c757d` (cinza)

### Responsividade
- Design mobile-first
- Breakpoints para tablet e desktop
- Grid system flexível
- Componentes adaptativos

### Componentes Visuais
- Cards com sombras e hover effects
- Botões com estados e transições
- Formulários com validação visual
- Badges e tags coloridas
- Modal overlay para formulários

## Validações

### Formulários
- Validação em tempo real
- Mensagens de erro específicas
- Estados visuais (válido/inválido)
- Prevenção de submissão com dados inválidos

### Campos Obrigatórios
- Nome (mín. 2 caracteres)
- Email (formato válido)
- Senha (mín. 6 caracteres)
- Título da tarefa
- Data da tarefa

## Funcionalidades Avançadas

### Filtros de Tarefas
- Por período (hoje, semana, mês)
- Por status (concluídas/pendentes)
- Por prioridade
- Por categoria
- Busca por título

### Indicadores Visuais
- Tarefas atrasadas (borda vermelha)
- Tarefas de hoje (borda amarela)
- Tarefas concluídas (opacidade reduzida)
- Cores por prioridade e categoria

### Experiência do Usuário
- Loading states
- Mensagens de erro amigáveis
- Confirmações para ações destrutivas
- Feedback visual para ações

## Testes

```bash
# Executar testes unitários
ng test

# Executar testes e2e
ng e2e

# Executar com coverage
ng test --code-coverage
```

## Deploy

### Netlify/Vercel
```bash
# Build para produção
ng build --configuration production

# Deploy da pasta dist/
```

### Servidor Web
```bash
# Servir arquivos estáticos da pasta dist/
# Configurar fallback para index.html (SPA)
```

## Próximos Passos

- Implementar PWA (Progressive Web App)
- Adicionar notificações push
- Implementar modo offline
- Adicionar temas (claro/escuro)
- Implementar drag & drop para tarefas
- Adicionar calendário visual
- Implementar compartilhamento de tarefas

## Troubleshooting

### Problemas Comuns

1. **CORS Error**: Verificar se o backend está configurado para aceitar requisições do frontend
2. **Token Expirado**: Implementar refresh token ou logout automático
3. **Rota não encontrada**: Verificar configuração do servidor para SPAs

### Debug

```bash
# Modo debug
ng serve --verbose

# Análise do bundle
ng build --stats-json
npx webpack-bundle-analyzer dist/stats.json
```

