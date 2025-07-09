# Sistema Gerenciador de Tarefas

Um projeto completo de gerenciamento de tarefas desenvolvido para demonstração técnica em entrevistas de emprego, utilizando as tecnologias Java Spring Boot, SQL Server/Azure e Angular.

## 📋 Visão Geral

Este sistema permite que usuários gerenciem suas tarefas diárias, organizando-as por data, prioridade e categoria. Inclui funcionalidades completas de autenticação, CRUD de usuários e tarefas, filtros avançados e interface responsiva.

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT** - Tokens de autenticação
- **SQL Server** - Banco de dados principal
- **H2 Database** - Banco para desenvolvimento local
- **Maven** - Gerenciamento de dependências

### Frontend
- **Angular 18** - Framework frontend
- **TypeScript** - Linguagem de programação
- **RxJS** - Programação reativa
- **Angular Material** - Componentes UI
- **SCSS** - Estilização
- **npm** - Gerenciamento de pacotes

### Cloud e DevOps
- **Azure SQL Database** - Banco de dados em nuvem
- **Azure App Service** - Hospedagem da aplicação
- **Docker** - Containerização
- **GitHub Actions** - CI/CD

## 📁 Estrutura do Projeto

```
task-manager/
├── backend/                 # Aplicação Spring Boot
│   ├── src/main/java/      # Código fonte Java
│   ├── src/main/resources/ # Recursos e configurações
│   ├── pom.xml            # Dependências Maven
│   └── README.md          # Documentação do backend
├── frontend/               # Aplicação Angular
│   ├── src/               # Código fonte TypeScript
│   ├── package.json       # Dependências npm
│   └── README.md          # Documentação do frontend
├── relatorio-implementacao.md    # Relatório de implementação
├── relatorio-tecnico-entrevista.md # Relatório técnico para entrevista
└── README.md              # Este arquivo
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Node.js 18+
- Maven 3.8+
- SQL Server (ou usar H2 para desenvolvimento)

### Backend (Spring Boot)

1. Navegue para o diretório do backend:
```bash
cd backend
```

2. Execute com perfil de desenvolvimento (H2):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. Ou execute com SQL Server:
```bash
# Configure as variáveis de ambiente
export DB_HOST=localhost
export DB_PORT=1433
export DB_NAME=TaskManager
export DB_USERNAME=sa
export DB_PASSWORD=YourPassword

mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### Frontend (Angular)

1. Navegue para o diretório do frontend:
```bash
cd frontend
```

2. Instale as dependências:
```bash
npm install
```

3. Execute o servidor de desenvolvimento:
```bash
npm start
```

A aplicação estará disponível em: `http://localhost:4200`

## 📊 Funcionalidades

### Autenticação
- ✅ Registro de usuários
- ✅ Login com JWT
- ✅ Proteção de rotas
- ✅ Logout automático

### Gerenciamento de Tarefas
- ✅ Criar tarefas
- ✅ Listar tarefas
- ✅ Editar tarefas
- ✅ Excluir tarefas
- ✅ Marcar como concluída
- ✅ Filtros por data, prioridade e categoria
- ✅ Busca por título

### Interface
- ✅ Design responsivo
- ✅ Dashboard com estatísticas
- ✅ Calendário de tarefas
- ✅ Notificações visuais

## 🔧 Configuração para Produção

### Azure SQL Database

1. Crie um SQL Server no Azure
2. Configure as variáveis de ambiente:
```bash
export AZURE_SQL_HOST=your-server.database.windows.net
export AZURE_SQL_DATABASE=TaskManager
export AZURE_SQL_USERNAME=your-username
export AZURE_SQL_PASSWORD=your-password
```

### Deploy no Azure App Service

1. Build da aplicação:
```bash
cd backend && mvn clean package
cd frontend && npm run build:prod
```

2. Deploy usando Azure CLI:
```bash
az webapp deploy --resource-group your-rg --name your-app --src-path target/app.jar
```

## 📚 Documentação

- **[Relatório de Implementação](relatorio-implementacao.md)** - Guia detalhado de como implementar o projeto
- **[Relatório Técnico para Entrevista](relatorio-tecnico-entrevista.md)** - Explicações técnicas e perguntas/respostas para entrevistas
- **[Backend README](backend/README.md)** - Documentação específica do backend
- **[Frontend README](frontend/README.md)** - Documentação específica do frontend

## 🧪 Testes

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
npm test
```

## 🐳 Docker

### Executar com Docker Compose
```bash
docker-compose up -d
```

### Build da imagem Docker
```bash
docker build -t task-manager .
docker run -p 8080:8080 task-manager
```

## 📈 Monitoramento

A aplicação inclui:
- Spring Boot Actuator para health checks
- Métricas customizadas com Micrometer
- Logs estruturados em JSON
- Integration com Application Insights (Azure)

Endpoints de monitoramento:
- `GET /actuator/health` - Status da aplicação
- `GET /actuator/metrics` - Métricas da aplicação
- `GET /actuator/info` - Informações da aplicação

## 🔒 Segurança

- Autenticação JWT stateless
- Senhas hasheadas com BCrypt
- Proteção CORS configurada
- Validação de entrada em todas as APIs
- Headers de segurança HTTP
- Isolamento de dados por usuário

## 🤝 Contribuição

Este projeto foi desenvolvido para fins de demonstração técnica. Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto é licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👨‍💻 Autor

Desenvolvido como projeto de demonstração técnica para entrevistas de emprego.

## 📞 Suporte

Para dúvidas sobre implementação ou conceitos técnicos, consulte:
- [Relatório de Implementação](relatorio-implementacao.md)
- [Relatório Técnico](relatorio-tecnico-entrevista.md)
- Documentação específica de cada módulo

---

**Nota:** Este projeto demonstra proficiência em desenvolvimento full-stack com tecnologias modernas, seguindo boas práticas de arquitetura, segurança e DevOps.

