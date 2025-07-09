# Relatório de Implementação - Sistema Gerenciador de Tarefas

## Sumário Executivo

Este relatório documenta a implementação completa de um sistema de gerenciamento de tarefas desenvolvido com tecnologias modernas. O projeto foi construído seguindo as melhores práticas de desenvolvimento, utilizando Java Spring Boot para o backend, Angular para o frontend, e SQL Server com Azure para persistência de dados.

O sistema implementa funcionalidades completas de CRUD (Create, Read, Update, Delete) para usuários e tarefas, incluindo autenticação JWT, filtros avançados, e uma interface responsiva e moderna. A arquitetura foi projetada para ser escalável, mantível e seguir os padrões da indústria.




## 1. Tecnologias Utilizadas

### 1.1 Backend - Java Spring Boot

O backend foi desenvolvido utilizando o ecossistema Spring Boot, que oferece uma base robusta e produtiva para desenvolvimento de aplicações Java empresariais. A escolha do Spring Boot se justifica pela sua maturidade, ampla adoção no mercado, e pelo conjunto abrangente de funcionalidades que oferece.

**Spring Boot 3.2.0** foi selecionado como framework principal, trazendo suporte nativo ao Java 24 e incluindo melhorias significativas em performance e segurança. Esta versão incorpora o Spring Framework 6.x, que oferece suporte aprimorado para programação reativa e melhor integração com tecnologias de containerização.

**Spring Security** foi implementado para gerenciar autenticação e autorização, utilizando tokens JWT (JSON Web Tokens) para manter o estado de sessão de forma stateless. Esta abordagem é particularmente adequada para aplicações modernas que precisam escalar horizontalmente e integrar com diferentes tipos de clientes.

**Spring Data JPA** foi escolhido para a camada de persistência, oferecendo uma abstração elegante sobre o Hibernate ORM. Esta combinação permite o desenvolvimento rápido de repositórios com consultas complexas, mantendo a flexibilidade para otimizações quando necessário.

**Maven** foi utilizado como ferramenta de build e gerenciamento de dependências, proporcionando um ciclo de vida de build bem definido e facilitando a integração com pipelines de CI/CD.

### 1.2 Frontend - Angular

O frontend foi desenvolvido com **Angular 18**, a versão mais recente do framework na época do desenvolvimento. Angular foi escolhido por sua arquitetura robusta, sistema de tipos forte através do TypeScript, e ecossistema maduro de ferramentas e bibliotecas.

**TypeScript** oferece tipagem estática que melhora significativamente a qualidade do código e a experiência de desenvolvimento, especialmente em projetos de médio e grande porte. A detecção de erros em tempo de compilação reduz bugs em produção e facilita a manutenção do código.

**SCSS (Sass)** foi utilizado para estilização, permitindo o uso de variáveis, mixins e outras funcionalidades avançadas que tornam o CSS mais maintível e organizável. A estrutura modular do SCSS facilita a criação de sistemas de design consistentes.

**RxJS** é utilizado extensivamente para programação reativa, especialmente no gerenciamento de chamadas HTTP e estado da aplicação. Os observables do RxJS oferecem uma forma elegante de lidar com operações assíncronas e fluxos de dados complexos.

### 1.3 Banco de Dados - SQL Server e Azure

**Microsoft SQL Server** foi escolhido como sistema de gerenciamento de banco de dados relacional, oferecendo robustez, performance e recursos avançados para aplicações empresariais. O SQL Server é amplamente utilizado no mercado corporativo e oferece excelente integração com o ecossistema Microsoft.

**Azure SQL Database** foi configurado como opção de deploy em nuvem, proporcionando escalabilidade automática, backups gerenciados e alta disponibilidade. A integração com Azure oferece recursos como monitoramento avançado, otimização automática de performance e segurança aprimorada.

Para desenvolvimento local, foi configurado o **H2 Database** como alternativa em memória, permitindo desenvolvimento e testes sem dependência de infraestrutura externa. Esta configuração dual facilita o desenvolvimento local e simplifica a configuração de ambientes de teste.

### 1.4 Ferramentas de Desenvolvimento

**Git** foi utilizado para controle de versão, seguindo práticas de branching e commit messages padronizadas. A estrutura do repositório foi organizada para facilitar a colaboração e manutenção do código.

**Angular CLI** proporcionou ferramentas de scaffolding, build e teste para o frontend, automatizando tarefas repetitivas e garantindo consistência na estrutura do projeto.

**Maven** no backend ofereceu gerenciamento de dependências robusto e ciclo de vida de build bem definido, facilitando a integração com ferramentas de CI/CD e deploy automatizado.


## 2. Arquitetura do Sistema

### 2.1 Visão Geral da Arquitetura

O sistema foi projetado seguindo uma arquitetura de três camadas (3-tier architecture), separando claramente as responsabilidades entre apresentação, lógica de negócio e persistência de dados. Esta separação promove a manutenibilidade, testabilidade e escalabilidade da aplicação.

A comunicação entre frontend e backend é realizada através de APIs RESTful, seguindo os princípios REST e utilizando JSON como formato de troca de dados. Esta abordagem garante interoperabilidade e facilita a integração com outros sistemas ou clientes móveis no futuro.

### 2.2 Arquitetura do Backend

O backend segue o padrão arquitetural MVC (Model-View-Controller) adaptado para APIs REST, onde os Controllers atuam como endpoints REST, os Services contêm a lógica de negócio, e os Models representam as entidades de domínio.

**Camada de Apresentação (Controllers):** Os controllers REST são responsáveis por receber requisições HTTP, validar dados de entrada, chamar os serviços apropriados e retornar respostas formatadas. Cada controller é focado em um domínio específico (AuthController, UserController, TaskController), promovendo a coesão e facilitando a manutenção.

**Camada de Negócio (Services):** Os services encapsulam a lógica de negócio da aplicação, implementando regras de validação, transformações de dados e orquestração de operações complexas. Esta camada é independente de detalhes de persistência e apresentação, facilitando testes unitários e reutilização de código.

**Camada de Persistência (Repositories):** Os repositories abstraem o acesso aos dados, utilizando Spring Data JPA para gerar implementações automáticas de operações CRUD básicas e permitindo a definição de consultas customizadas através de métodos nomeados ou anotações @Query.

**Camada de Segurança:** Implementada de forma transversal através de filtros e interceptors, a segurança é aplicada automaticamente a todas as requisições. O sistema utiliza JWT para autenticação stateless e Spring Security para autorização baseada em roles.

### 2.3 Arquitetura do Frontend

O frontend Angular segue uma arquitetura baseada em componentes, onde cada componente encapsula sua própria lógica, template e estilos. Esta abordagem promove a reutilização de código e facilita a manutenção.

**Componentes:** Cada tela ou funcionalidade é implementada como um componente Angular, seguindo o princípio de responsabilidade única. Os componentes são organizados hierarquicamente, com componentes pais orquestrando componentes filhos através de inputs e outputs.

**Serviços:** Os serviços Angular encapsulam a lógica de comunicação com o backend, gerenciamento de estado e operações utilitárias. Utilizando injeção de dependência, os serviços são facilmente testáveis e reutilizáveis entre diferentes componentes.

**Guards e Interceptors:** Guards protegem rotas que requerem autenticação, enquanto interceptors adicionam automaticamente tokens de autenticação às requisições HTTP. Esta abordagem centraliza a lógica de segurança e reduz a duplicação de código.

**Roteamento:** O sistema de roteamento do Angular gerencia a navegação entre diferentes telas, implementando lazy loading quando apropriado para otimizar o tempo de carregamento inicial.

### 2.4 Comunicação Entre Camadas

A comunicação entre frontend e backend é realizada exclusivamente através de APIs REST, utilizando o protocolo HTTP/HTTPS. Todas as requisições incluem headers apropriados para autenticação (Authorization: Bearer token) e content-type (application/json).

O sistema implementa tratamento de erros robusto, com códigos de status HTTP apropriados e mensagens de erro estruturadas. Erros de validação retornam status 400 com detalhes específicos, erros de autenticação retornam 401, e erros de autorização retornam 403.

Para otimização de performance, o sistema utiliza paginação em endpoints que retornam listas grandes de dados, e implementa caching apropriado através de headers HTTP quando aplicável.

### 2.5 Segurança da Arquitetura

A segurança foi implementada em múltiplas camadas, seguindo o princípio de defesa em profundidade. No backend, todas as rotas são protegidas por padrão, exceto endpoints públicos explicitamente marcados. A validação de entrada é realizada tanto no frontend quanto no backend, garantindo que dados maliciosos não sejam processados.

O sistema utiliza HTTPS para todas as comunicações em produção, garantindo que dados sensíveis como tokens de autenticação e informações pessoais sejam transmitidos de forma segura. Senhas são sempre hasheadas utilizando BCrypt antes de serem armazenadas no banco de dados.

CORS (Cross-Origin Resource Sharing) foi configurado adequadamente para permitir requisições apenas de origens autorizadas, prevenindo ataques de cross-site scripting em cenários de produção.


## 3. Configuração do Ambiente de Desenvolvimento

### 3.1 Pré-requisitos do Sistema

Para executar o projeto completo, é necessário ter instalado no ambiente de desenvolvimento um conjunto específico de ferramentas e tecnologias. A configuração adequada destes pré-requisitos é fundamental para garantir que o sistema funcione corretamente em diferentes ambientes.

**Java Development Kit (JDK) 17** é obrigatório para o backend Spring Boot. Esta versão LTS (Long Term Support) oferece estabilidade e recursos modernos da linguagem Java, incluindo records, pattern matching e melhorias significativas na JVM. A instalação pode ser realizada através do OpenJDK ou Oracle JDK, sendo recomendado o OpenJDK para ambientes de desenvolvimento.

**Node.js 18+** é necessário para o desenvolvimento e build do frontend Angular. Esta versão inclui melhorias importantes no motor V8 e suporte aprimorado para módulos ES. O Node.js também instala o npm (Node Package Manager), que será utilizado para gerenciar as dependências do projeto Angular.

**Maven 3.6+** é utilizado para build e gerenciamento de dependências do projeto Java. O Maven deve estar configurado corretamente no PATH do sistema para permitir execução de comandos mvn de qualquer diretório.

**Angular CLI** deve ser instalado globalmente através do npm para facilitar a criação, desenvolvimento e build do projeto Angular. A instalação é realizada através do comando `npm install -g @angular/cli`.

### 3.2 Configuração do Backend

A configuração do backend envolve múltiplos aspectos, desde a estrutura de diretórios até a configuração de diferentes perfis de execução para desenvolvimento, teste e produção.

**Estrutura de Diretórios:** O projeto segue a estrutura padrão do Maven, com código fonte em `src/main/java`, recursos em `src/main/resources`, e testes em `src/test/java`. Dentro do pacote principal, os códigos são organizados por responsabilidade: controllers, services, repositories, entities, DTOs, security e config.

**Configuração de Perfis:** Foram criados três perfis de configuração distintos para atender diferentes necessidades de ambiente. O perfil padrão utiliza configurações genéricas adequadas para desenvolvimento local com banco H2. O perfil 'dev' é otimizado para desenvolvimento local com configurações de debug habilitadas. O perfil 'azure' é configurado especificamente para deploy em produção com Azure SQL Database.

**Configuração de Banco de Dados:** Para desenvolvimento local, o sistema está configurado para utilizar H2 Database em memória, eliminando a necessidade de instalação e configuração de banco de dados externo. Esta configuração permite que desenvolvedores iniciem o projeto rapidamente sem dependências externas complexas.

Para produção, a configuração aponta para Azure SQL Database, utilizando variáveis de ambiente para credenciais sensíveis. Esta abordagem garante que informações confidenciais não sejam expostas no código fonte e permite configuração flexível entre diferentes ambientes.

**Configuração de Segurança:** As chaves JWT são configuradas através de variáveis de ambiente, com valores padrão adequados para desenvolvimento local. Em produção, estas chaves devem ser geradas de forma segura e mantidas confidenciais.

### 3.3 Configuração do Frontend

O frontend Angular requer configuração específica para desenvolvimento local e build de produção, incluindo configuração de proxy para comunicação com o backend durante desenvolvimento.

**Instalação de Dependências:** Após clonar o projeto, é necessário executar `npm install` no diretório do frontend para baixar todas as dependências especificadas no package.json. Este processo pode levar alguns minutos dependendo da velocidade da conexão com a internet.

**Configuração de Proxy:** Para desenvolvimento local, é recomendado configurar um proxy no Angular CLI para redirecionar chamadas de API para o backend local. Isto evita problemas de CORS durante desenvolvimento e simula o comportamento de produção onde frontend e backend são servidos do mesmo domínio.

**Configuração de Ambiente:** O Angular utiliza arquivos de configuração de ambiente para definir URLs de API e outras configurações específicas de cada ambiente. O arquivo `environment.ts` contém configurações para desenvolvimento, enquanto `environment.prod.ts` contém configurações otimizadas para produção.

### 3.4 Configuração do Banco de Dados

A configuração do banco de dados varia significativamente entre ambientes de desenvolvimento e produção, requerendo atenção especial para garantir compatibilidade e performance adequada.

**Desenvolvimento Local com H2:** O banco H2 é configurado para executar em memória, criando e populando tabelas automaticamente a cada inicialização da aplicação. Esta configuração inclui dados de exemplo para facilitar testes e desenvolvimento. O console web do H2 é habilitado para permitir inspeção e manipulação manual dos dados durante desenvolvimento.

**Produção com Azure SQL Database:** A configuração de produção utiliza Azure SQL Database, que oferece escalabilidade automática, backups gerenciados e alta disponibilidade. A string de conexão é configurada para utilizar SSL/TLS obrigatório, garantindo que todas as comunicações com o banco sejam criptografadas.

**Migrações de Schema:** O Hibernate é configurado para gerenciar automaticamente o schema do banco de dados através da propriedade `ddl-auto`. Em desenvolvimento, utiliza-se `create-drop` para recriar o schema a cada execução. Em produção, utiliza-se `validate` para garantir que o schema esteja correto sem modificações automáticas.

### 3.5 Scripts de Inicialização

Para facilitar a configuração e execução do projeto, foram criados scripts e documentação detalhada para cada componente do sistema.

**Backend:** O backend pode ser iniciado através do comando `mvn spring-boot:run` com perfis específicos. Para desenvolvimento local, recomenda-se utilizar `mvn spring-boot:run -Dspring-boot.run.profiles=dev` para habilitar configurações de debug e logging detalhado.

**Frontend:** O frontend é iniciado através do comando `ng serve`, que inicia um servidor de desenvolvimento com hot reload. Para build de produção, utiliza-se `ng build --configuration production`, que gera arquivos otimizados na pasta `dist/`.

**Verificação de Funcionamento:** Após inicializar ambos os componentes, o sistema pode ser testado acessando `http://localhost:4200` para o frontend e verificando que as chamadas de API para `http://localhost:8080` estão funcionando corretamente. O console do navegador e logs do backend devem ser monitorados para identificar possíveis problemas de configuração.


## 4. Implementação do Backend

### 4.1 Estrutura e Organização do Código

A implementação do backend segue princípios de Clean Architecture e Domain-Driven Design, organizando o código em camadas bem definidas que promovem separação de responsabilidades e facilidade de manutenção. A estrutura de pacotes foi cuidadosamente planejada para refletir a arquitetura da aplicação e facilitar a navegação no código.

O pacote `entity` contém as classes de domínio que representam as entidades principais do sistema: User e Task. Estas classes utilizam anotações JPA para mapeamento objeto-relacional e incluem validações de negócio através de Bean Validation. As entidades são projetadas para serem ricas em comportamento, encapsulando regras de negócio relevantes e mantendo consistência de estado.

O pacote `repository` define interfaces que estendem JpaRepository, aproveitando a capacidade do Spring Data de gerar implementações automáticas para operações CRUD básicas. Além disso, métodos customizados são definidos utilizando convenções de nomenclatura do Spring Data ou anotações @Query para consultas mais complexas. Esta abordagem reduz significativamente a quantidade de código boilerplate necessário para operações de persistência.

O pacote `service` implementa a lógica de negócio da aplicação, orquestrando operações entre diferentes repositórios e aplicando regras de validação complexas. Os serviços são projetados para serem transacionais, utilizando anotações @Transactional para garantir consistência de dados em operações que envolvem múltiplas entidades.

O pacote `controller` expõe endpoints REST que servem como interface entre o frontend e a lógica de negócio. Os controllers são responsáveis apenas por receber requisições, validar dados de entrada, chamar serviços apropriados e formatar respostas. Esta separação clara de responsabilidades facilita testes unitários e manutenção do código.

### 4.2 Implementação das Entidades

A entidade User representa os usuários do sistema e inclui campos essenciais como nome, email e senha, além de metadados de auditoria como timestamps de criação e atualização. A senha é sempre armazenada de forma hasheada utilizando BCrypt, garantindo que mesmo em caso de comprometimento do banco de dados, as senhas originais permaneçam protegidas.

A validação da entidade User é implementada através de anotações Bean Validation, incluindo validação de formato de email, tamanho mínimo e máximo de campos, e obrigatoriedade de campos essenciais. Estas validações são aplicadas automaticamente pelo Spring Boot antes da persistência, garantindo integridade dos dados.

A entidade Task representa as tarefas do sistema e inclui campos como título, descrição, data da tarefa, status de conclusão, prioridade e categoria. A relação com User é implementada através de anotação @ManyToOne, estabelecendo que cada tarefa pertence a um usuário específico. Esta relação é fundamental para garantir isolamento de dados entre usuários.

Enums são utilizados para representar prioridade (LOW, MEDIUM, HIGH, URGENT) e categoria (PERSONAL, WORK, STUDY, HEALTH, FINANCE, OTHER), garantindo consistência de dados e facilitando futuras extensões. Estes enums são mapeados como strings no banco de dados para facilitar legibilidade e manutenção.

### 4.3 Implementação dos Repositórios

Os repositórios foram implementados seguindo o padrão Repository do Domain-Driven Design, encapsulando toda a lógica de acesso a dados e fornecendo uma interface limpa para os serviços. Cada entidade possui seu próprio repositório, promovendo coesão e facilitando manutenção.

O UserRepository inclui métodos para busca por email (essencial para autenticação), verificação de existência de email (para evitar duplicatas), e busca por nome com suporte a busca parcial case-insensitive. Estes métodos são implementados utilizando convenções de nomenclatura do Spring Data, que gera automaticamente as consultas SQL apropriadas.

O TaskRepository é mais complexo, incluindo métodos para filtrar tarefas por usuário, data, status de conclusão, prioridade e categoria. Métodos especializados foram implementados para buscar tarefas da semana atual e do mês atual, utilizando consultas JPQL customizadas que calculam dinamicamente os períodos baseados na data atual.

Consultas de agregação foram implementadas para fornecer estatísticas como contagem de tarefas concluídas e pendentes por usuário. Estas consultas utilizam funções SQL nativas quando necessário para otimizar performance, especialmente em cenários com grandes volumes de dados.

### 4.4 Implementação dos Serviços

A camada de serviços implementa toda a lógica de negócio da aplicação, incluindo validações complexas, transformações de dados e orquestração de operações que envolvem múltiplas entidades. Os serviços são projetados para serem stateless e thread-safe, permitindo execução concorrente segura.

O UserService gerencia operações relacionadas a usuários, incluindo criação, atualização, busca e exclusão. A criação de usuários inclui validação de unicidade de email e hash da senha antes da persistência. A atualização permite modificação de dados pessoais mas requer validação especial para alteração de email, garantindo que não haja conflitos com usuários existentes.

O TaskService implementa toda a lógica relacionada ao gerenciamento de tarefas, incluindo validações de propriedade (garantindo que usuários só possam acessar suas próprias tarefas), aplicação de regras de negócio para marcação de conclusão, e implementação de filtros complexos para busca e visualização de tarefas.

Ambos os serviços utilizam DTOs (Data Transfer Objects) para comunicação com a camada de apresentação, evitando exposição direta das entidades de domínio. Esta abordagem permite evolução independente das APIs e das estruturas internas de dados, além de facilitar versionamento de APIs.

### 4.5 Implementação da Segurança

A segurança foi implementada utilizando Spring Security com autenticação baseada em JWT (JSON Web Tokens), proporcionando uma solução stateless adequada para aplicações modernas que precisam escalar horizontalmente.

O JwtUtil é uma classe utilitária que encapsula toda a lógica de geração, validação e extração de informações de tokens JWT. Esta classe utiliza a biblioteca JJWT para operações criptográficas seguras e inclui validação de expiração, assinatura e estrutura do token.

O CustomUserDetailsService implementa a interface UserDetailsService do Spring Security, integrando o sistema de autenticação com as entidades de usuário da aplicação. Este serviço é responsável por carregar informações do usuário durante o processo de autenticação e fornecer dados necessários para autorização.

O JwtAuthenticationFilter é um filtro customizado que intercepta todas as requisições HTTP, extrai tokens JWT dos headers Authorization, valida os tokens e estabelece o contexto de segurança para a requisição. Este filtro é executado antes dos controllers, garantindo que apenas requisições autenticadas tenham acesso aos recursos protegidos.

A configuração de segurança define quais endpoints são públicos (como login e registro) e quais requerem autenticação. CORS é configurado adequadamente para permitir requisições do frontend durante desenvolvimento e produção, com configurações específicas para cada ambiente.

### 4.6 Implementação dos Controllers

Os controllers REST foram implementados seguindo as melhores práticas de design de APIs, incluindo uso apropriado de códigos de status HTTP, estruturação consistente de respostas, e tratamento robusto de erros.

O AuthController gerencia operações de autenticação, incluindo login, registro e validação de tokens. O endpoint de login aceita credenciais, valida através do AuthenticationManager do Spring Security, e retorna um token JWT junto com informações básicas do usuário. O endpoint de registro cria novos usuários após validação de dados e retorna automaticamente um token para login imediato.

O UserController expõe operações CRUD para gerenciamento de usuários, incluindo busca por ID, atualização de perfil, alteração de senha e exclusão de conta. Todas as operações incluem validação de autorização para garantir que usuários só possam modificar seus próprios dados.

O TaskController é o mais complexo, expondo uma ampla gama de endpoints para gerenciamento de tarefas. Inclui operações CRUD básicas, endpoints especializados para filtros (por data, status, prioridade, categoria), busca textual, e endpoints para estatísticas. Todos os endpoints incluem validação de propriedade para garantir isolamento de dados entre usuários.

Tratamento de erros é implementado de forma consistente em todos os controllers, retornando códigos de status HTTP apropriados e mensagens de erro estruturadas em formato JSON. Validações de entrada utilizam Bean Validation com mensagens customizadas em português para melhor experiência do usuário.


## 5. Implementação do Frontend

### 5.1 Estrutura e Organização do Projeto Angular

O frontend foi desenvolvido seguindo as melhores práticas de arquitetura Angular, organizando o código em uma estrutura modular que promove reutilização, manutenibilidade e escalabilidade. A aplicação utiliza Angular 18 com standalone components, uma abordagem moderna que simplifica a configuração e melhora a performance através de tree-shaking mais eficiente.

A estrutura de diretórios foi organizada por funcionalidade, com cada feature encapsulada em seu próprio módulo conceitual. O diretório `components` contém todos os componentes da aplicação, organizados por responsabilidade: login, register, dashboard, task-list e task-form. Esta organização facilita a localização de código e promove coesão funcional.

O diretório `services` centraliza toda a lógica de comunicação com o backend e gerenciamento de estado da aplicação. Os serviços são implementados como singletons através do sistema de injeção de dependência do Angular, garantindo consistência de estado e eficiência de memória.

O diretório `models` define interfaces TypeScript que representam as estruturas de dados utilizadas na aplicação. Estas interfaces garantem type safety em tempo de compilação e servem como documentação viva da estrutura de dados esperada pelas APIs.

Guards e interceptors são organizados em diretórios específicos, encapsulando lógica transversal de segurança e comunicação HTTP. Esta separação facilita testes unitários e reutilização de código entre diferentes partes da aplicação.

### 5.2 Implementação dos Componentes

O componente de login implementa um formulário reativo com validação em tempo real, utilizando Angular Forms para gerenciar estado e validação de campos. O formulário inclui validações para formato de email e tamanho mínimo de senha, fornecendo feedback visual imediato ao usuário através de classes CSS condicionais e mensagens de erro específicas.

A integração com o AuthService permite autenticação assíncrona com tratamento robusto de erros. Estados de loading são gerenciados através de variáveis booleanas que controlam a habilitação de botões e exibição de indicadores visuais, melhorando a experiência do usuário durante operações de rede.

O componente de registro estende a funcionalidade do login com validações adicionais, incluindo confirmação de senha e validação de unicidade de email. A validação de confirmação de senha é implementada através de um validator customizado que compara os valores dos campos em tempo real.

O dashboard é o componente mais complexo, servindo como hub central da aplicação. Implementa um layout responsivo com cards de estatísticas, controles de filtro e integração com componentes filhos para listagem e edição de tarefas. O gerenciamento de estado é realizado através de observables RxJS, permitindo atualizações reativas quando dados são modificados.

O componente task-list implementa uma visualização rica de tarefas com indicadores visuais para diferentes estados (concluída, atrasada, hoje). Utiliza pipes customizados para formatação de datas e cores dinâmicas baseadas em prioridade e categoria. A interação com tarefas é implementada através de eventos que são propagados para o componente pai.

O componente task-form é um formulário modal reutilizável que serve tanto para criação quanto edição de tarefas. Utiliza formulários reativos com validações complexas e suporte a diferentes tipos de input (text, textarea, date, select). O componente detecta automaticamente se está em modo de criação ou edição baseado na presença de dados de entrada.

### 5.3 Implementação dos Serviços

O AuthService é responsável por todo o gerenciamento de autenticação da aplicação, incluindo login, registro, logout e manutenção de estado de autenticação. Utiliza BehaviorSubject do RxJS para manter estado reativo do usuário atual, permitindo que componentes se inscrevam para receber atualizações automáticas quando o estado de autenticação muda.

O serviço implementa persistência de token no localStorage, com validação automática de expiração através de decodificação do payload JWT. Esta abordagem permite que a aplicação mantenha estado de autenticação entre sessões do navegador, melhorando a experiência do usuário.

Métodos de validação de token são implementados para verificar se o usuário ainda está autenticado, incluindo verificação de expiração e integridade do token. Estes métodos são utilizados pelos guards de rota para proteger páginas que requerem autenticação.

O TaskService encapsula toda a comunicação com a API de tarefas, fornecendo métodos para operações CRUD básicas e filtros avançados. Cada método retorna observables que podem ser facilmente integrados com componentes Angular através de async pipes ou subscrições manuais.

O serviço inclui métodos utilitários para formatação e apresentação de dados, como conversão de enums para labels em português, geração de cores para prioridades e categorias, e formatação de datas. Estes utilitários centralizam lógica de apresentação e garantem consistência visual em toda a aplicação.

Tratamento de erros é implementado de forma consistente em todos os métodos, com transformação de erros HTTP em mensagens amigáveis ao usuário. O serviço utiliza operadores RxJS como catchError e map para processar respostas e erros de forma declarativa.

### 5.4 Implementação de Roteamento e Navegação

O sistema de roteamento foi configurado para suportar navegação fluida entre diferentes telas da aplicação, incluindo proteção de rotas através de guards de autenticação. A configuração de rotas utiliza lazy loading quando apropriado para otimizar o tempo de carregamento inicial da aplicação.

O AuthGuard implementa a interface CanActivate para proteger rotas que requerem autenticação. O guard verifica o estado de autenticação através do AuthService e redireciona usuários não autenticados para a página de login. Esta proteção é aplicada automaticamente a todas as rotas marcadas como protegidas.

Redirecionamentos automáticos são implementados para melhorar a experiência do usuário, incluindo redirecionamento para dashboard após login bem-sucedido e redirecionamento para login quando tokens expiram. Estes redirecionamentos são implementados de forma não intrusiva, preservando o contexto quando possível.

A navegação programática é utilizada em componentes para redirecionar usuários após operações bem-sucedidas, como criação de conta ou logout. Esta abordagem garante que o estado da aplicação permaneça consistente e que usuários sejam direcionados para telas apropriadas baseado em suas ações.

### 5.5 Implementação de Interceptors e Guards

O AuthInterceptor implementa a interface HttpInterceptor para adicionar automaticamente tokens de autenticação a todas as requisições HTTP. O interceptor verifica se existe um token válido no AuthService e adiciona o header Authorization com o formato Bearer token quando apropriado.

Esta implementação centraliza a lógica de autenticação HTTP, eliminando a necessidade de adicionar headers manualmente em cada chamada de API. O interceptor é registrado globalmente na configuração da aplicação, garantindo que todas as requisições sejam processadas consistentemente.

Tratamento de erros de autenticação é implementado no interceptor para detectar respostas 401 (Unauthorized) e automaticamente redirecionar usuários para a página de login. Esta funcionalidade garante que usuários sejam notificados imediatamente quando suas sessões expiram.

O AuthGuard complementa o interceptor fornecendo proteção no nível de rota, verificando autenticação antes mesmo de carregar componentes protegidos. Esta abordagem em camadas garante que recursos protegidos nunca sejam expostos a usuários não autenticados.

### 5.6 Implementação de Estilos e Responsividade

O sistema de estilos foi implementado utilizando SCSS com uma arquitetura modular que promove reutilização e consistência visual. Variáveis SCSS são utilizadas para definir cores, tamanhos e outros valores de design, facilitando manutenção e permitindo mudanças globais de tema.

Cada componente possui seu próprio arquivo de estilos encapsulado, evitando conflitos de CSS e promovendo modularidade. Estilos globais são definidos em `styles.scss` e incluem reset CSS, classes utilitárias e estilos base para elementos HTML.

Responsividade é implementada através de media queries e layout flexível, garantindo que a aplicação funcione adequadamente em dispositivos móveis, tablets e desktops. O design mobile-first foi adotado, com estilos base otimizados para telas pequenas e melhorias progressivas para telas maiores.

Componentes visuais como cards, botões e formulários utilizam transições CSS para melhorar a experiência do usuário. Hover effects, animações de loading e feedback visual são implementados de forma sutil mas eficaz, proporcionando uma interface moderna e responsiva.

Sistema de cores consistente é aplicado em toda a aplicação, com cores específicas para diferentes tipos de conteúdo (prioridades, categorias, estados). Esta consistência visual ajuda usuários a navegar e compreender a interface mais rapidamente.


## 6. Configuração do Banco de Dados

### 6.1 Estratégia Multi-Ambiente

A configuração do banco de dados foi projetada para suportar múltiplos ambientes de execução, desde desenvolvimento local até produção em nuvem. Esta estratégia permite que desenvolvedores trabalhem de forma independente sem necessidade de infraestrutura complexa, enquanto garante que o sistema funcione adequadamente em ambiente de produção.

Para desenvolvimento local, foi configurado o H2 Database, um banco de dados em memória que oferece compatibilidade SQL completa sem necessidade de instalação ou configuração adicional. Esta escolha elimina barreiras de entrada para novos desenvolvedores e simplifica significativamente o processo de configuração inicial do ambiente.

O H2 é configurado para executar em modo embedded, criando automaticamente o schema do banco de dados baseado nas anotações JPA das entidades. Esta abordagem garante que o schema esteja sempre sincronizado com o modelo de dados da aplicação, eliminando problemas de incompatibilidade entre código e banco de dados.

Para produção, a configuração aponta para Azure SQL Database, oferecendo escalabilidade, alta disponibilidade e recursos avançados de monitoramento e backup. A migração entre ambientes é transparente graças ao uso de JPA e Hibernate, que abstraem diferenças específicas entre diferentes sistemas de banco de dados.

### 6.2 Modelagem de Dados

O modelo de dados foi projetado seguindo princípios de normalização e boas práticas de design de banco de dados relacional. A estrutura é simples mas extensível, permitindo futuras expansões sem necessidade de reestruturação significativa.

A tabela Users armazena informações básicas dos usuários, incluindo campos de auditoria para rastreamento de criação e modificação. O campo email é único e indexado para otimizar operações de autenticação. Senhas são armazenadas hasheadas utilizando BCrypt com salt automático, garantindo segurança mesmo em caso de comprometimento dos dados.

A tabela Tasks mantém todas as informações relacionadas às tarefas dos usuários, com chave estrangeira para Users estabelecendo a relação de propriedade. Campos de prioridade e categoria utilizam enums para garantir consistência de dados e facilitar futuras extensões. Timestamps de criação, atualização e conclusão permitem auditoria completa do ciclo de vida das tarefas.

Índices foram criados estrategicamente para otimizar consultas frequentes, incluindo índices compostos para combinações de usuário e data, que são utilizadas extensivamente pelos filtros da aplicação. Esta otimização garante performance adequada mesmo com grandes volumes de dados.

### 6.3 Configuração de Conexão e Pool

A configuração de conexão com o banco de dados utiliza HikariCP, um pool de conexões de alta performance que é padrão no Spring Boot. As configurações foram otimizadas para balancear performance e utilização de recursos, com tamanhos de pool apropriados para diferentes ambientes.

Para desenvolvimento local, o pool é configurado com tamanho mínimo para reduzir overhead, enquanto em produção utiliza configurações otimizadas para alta concorrência. Timeouts são configurados adequadamente para evitar travamentos em caso de problemas de rede ou sobrecarga do banco.

Configurações de SSL/TLS são obrigatórias em produção, garantindo que todas as comunicações com o banco sejam criptografadas. Certificados são validados automaticamente para prevenir ataques man-in-the-middle e garantir integridade das conexões.

Monitoramento de conexões é habilitado através de métricas do Spring Boot Actuator, permitindo observabilidade em tempo real do estado do pool de conexões e identificação proativa de problemas de performance.

### 6.4 Migrações e Versionamento

O sistema utiliza Hibernate para gerenciamento automático de schema, com configurações diferentes para cada ambiente. Em desenvolvimento, o schema é recriado a cada inicialização para garantir consistência com o modelo de dados atual. Em produção, validações são realizadas para garantir compatibilidade sem modificações automáticas.

Scripts de dados iniciais são fornecidos para popular o banco com dados de exemplo durante desenvolvimento. Estes scripts incluem usuários de teste e tarefas de exemplo que facilitam desenvolvimento e demonstração das funcionalidades do sistema.

Para futuras evoluções, está prevista a implementação de Flyway ou Liquibase para controle de versão de schema mais robusto. Esta migração permitirá atualizações controladas em produção e rastreamento completo de mudanças no banco de dados.

Backup e recuperação são gerenciados automaticamente pelo Azure SQL Database em produção, com políticas de retenção configuradas para atender requisitos de negócio. Para desenvolvimento, backups não são necessários devido à natureza temporária do banco H2.

## 7. Testes e Validação

### 7.1 Estratégia de Testes

A estratégia de testes foi desenvolvida para garantir qualidade e confiabilidade do sistema através de múltiplas camadas de validação. Embora testes automatizados completos não tenham sido implementados nesta versão inicial, a arquitetura foi projetada para facilitar a adição de testes unitários, de integração e end-to-end.

Testes manuais foram realizados extensivamente durante o desenvolvimento, cobrindo todos os fluxos principais da aplicação. Estes testes incluem criação de usuários, autenticação, gerenciamento de tarefas, filtros e funcionalidades de busca. Cenários de erro também foram testados para garantir que a aplicação se comporte adequadamente em situações excepcionais.

A separação clara entre camadas facilita a implementação de testes unitários, especialmente para serviços que contêm lógica de negócio. Mocks podem ser facilmente criados para repositórios e dependências externas, permitindo testes isolados e rápidos.

Para testes de integração, a configuração com H2 Database permite criação de ambientes de teste limpos e reproduzíveis. Esta abordagem garante que testes não sejam afetados por dados residuais e possam ser executados de forma independente.

### 7.2 Validação de Funcionalidades

Todas as funcionalidades principais foram validadas através de testes manuais sistemáticos. O processo de registro de usuários foi testado com diferentes combinações de dados válidos e inválidos, verificando que validações funcionam corretamente e que mensagens de erro são apropriadas.

A funcionalidade de autenticação foi testada extensivamente, incluindo cenários de login bem-sucedido, credenciais inválidas, tokens expirados e tentativas de acesso não autorizado. Todos os cenários se comportaram conforme esperado, com redirecionamentos apropriados e mensagens de erro claras.

O gerenciamento de tarefas foi validado através de operações CRUD completas, incluindo criação, visualização, edição e exclusão de tarefas. Filtros por data, prioridade e categoria foram testados com diferentes combinações de dados, verificando que os resultados são precisos e consistentes.

Funcionalidades de busca e estatísticas foram validadas com diferentes volumes de dados, garantindo que performance seja adequada e que cálculos estejam corretos. Testes de concorrência básicos foram realizados para verificar que múltiplos usuários podem utilizar o sistema simultaneamente sem conflitos.

### 7.3 Validação de Segurança

A segurança do sistema foi validada através de testes específicos para verificar que controles de acesso funcionam adequadamente. Tentativas de acesso a recursos sem autenticação foram testadas, confirmando que redirecionamentos para login ocorrem corretamente.

Isolamento de dados entre usuários foi validado através de tentativas de acesso a tarefas de outros usuários, verificando que o sistema rejeita adequadamente estas operações. Tokens JWT foram testados para verificar que expiração é respeitada e que tokens inválidos são rejeitados.

Validações de entrada foram testadas com dados maliciosos e malformados, confirmando que o sistema rejeita adequadamente entradas inválidas e não é vulnerável a ataques básicos de injeção. Embora testes de segurança mais avançados não tenham sido realizados, a arquitetura implementa defesas adequadas contra vulnerabilidades comuns.

HTTPS é obrigatório em produção, e configurações de CORS foram validadas para garantir que apenas origens autorizadas possam acessar a API. Estas medidas proporcionam uma base sólida de segurança para o sistema.

## 8. Deploy e Produção

### 8.1 Preparação para Deploy

O sistema foi projetado para facilitar deploy em diferentes ambientes, incluindo servidores tradicionais, containers Docker e plataformas de nuvem. A configuração baseada em perfis permite adaptação automática a diferentes ambientes sem necessidade de modificação de código.

Para o backend, o build Maven gera um JAR executável que inclui todas as dependências necessárias. Este JAR pode ser executado em qualquer ambiente que tenha Java 17 instalado, simplificando significativamente o processo de deploy. Variáveis de ambiente são utilizadas para configurações sensíveis, permitindo que o mesmo artefato seja utilizado em diferentes ambientes.

O frontend Angular é compilado para arquivos estáticos que podem ser servidos por qualquer servidor web. O build de produção inclui otimizações como minificação, tree-shaking e compressão, resultando em arquivos pequenos e carregamento rápido. A configuração de roteamento do Angular requer configuração específica do servidor web para suportar URLs amigáveis.

Documentação detalhada foi criada para orientar o processo de deploy, incluindo configurações específicas para diferentes provedores de nuvem e servidores web. Esta documentação inclui exemplos de configuração para nginx, Apache e IIS.

### 8.2 Configuração de Produção

A configuração de produção inclui otimizações específicas para performance, segurança e monitoramento. Logging é configurado para capturar informações relevantes sem impactar performance, com níveis apropriados para diferentes tipos de eventos.

Configurações de cache são implementadas para reduzir latência e carga no banco de dados. Headers HTTP apropriados são configurados para cache de recursos estáticos no frontend, melhorando significativamente a experiência do usuário em visitas subsequentes.

Monitoramento é implementado através do Spring Boot Actuator, expondo métricas de saúde, performance e utilização de recursos. Estas métricas podem ser integradas com ferramentas de monitoramento como Prometheus, Grafana ou Azure Monitor.

Configurações de segurança são endurecidas para produção, incluindo desabilitação de endpoints de debug, configuração de headers de segurança HTTP e implementação de rate limiting para prevenir ataques de força bruta.

### 8.3 Escalabilidade e Performance

A arquitetura stateless do sistema facilita escalabilidade horizontal, permitindo que múltiplas instâncias da aplicação sejam executadas simultaneamente atrás de um load balancer. O uso de JWT para autenticação elimina a necessidade de sessões compartilhadas entre instâncias.

O banco de dados Azure SQL oferece escalabilidade automática baseada em demanda, garantindo que performance seja mantida mesmo com crescimento significativo de usuários. Índices foram criados estrategicamente para otimizar consultas frequentes e suportar crescimento de dados.

Cache pode ser implementado em múltiplas camadas, incluindo cache de aplicação para dados frequentemente acessados e CDN para recursos estáticos do frontend. Esta abordagem em camadas maximiza performance enquanto minimiza custos de infraestrutura.

Otimizações de performance foram implementadas no frontend através de lazy loading, OnPush change detection strategy e otimizações de bundle. Estas técnicas garantem que a aplicação permaneça responsiva mesmo com crescimento de funcionalidades.


## 9. Conclusões e Próximos Passos

### 9.1 Objetivos Alcançados

O projeto de gerenciador de tarefas foi implementado com sucesso, atendendo a todos os requisitos técnicos especificados para demonstração em entrevista de emprego. O sistema demonstra proficiência nas tecnologias solicitadas: Java Spring Boot, SQL Server com Azure, e Angular, implementando um conjunto completo de funcionalidades que showcaseiam conhecimentos técnicos abrangentes.

A arquitetura implementada segue padrões da indústria e boas práticas de desenvolvimento, demonstrando compreensão de conceitos fundamentais como separação de responsabilidades, segurança de aplicações web, e design de APIs RESTful. O código é bem estruturado, documentado e organizado de forma que facilita manutenção e extensão futuras.

As funcionalidades implementadas cobrem um espectro completo de operações típicas em aplicações empresariais, incluindo autenticação e autorização, operações CRUD, filtros e buscas avançadas, e interface de usuário responsiva. Esta amplitude demonstra capacidade de desenvolver soluções completas end-to-end.

A documentação técnica produzida, incluindo este relatório de implementação e o relatório técnico complementar, demonstra habilidades de comunicação técnica e capacidade de explicar decisões arquiteturais e implementações complexas de forma clara e estruturada.

### 9.2 Lições Aprendidas

Durante o desenvolvimento do projeto, várias lições importantes foram aprendidas sobre integração de tecnologias modernas e implementação de sistemas completos. A importância de configuração adequada de CORS para comunicação entre frontend e backend foi evidenciada, especialmente em ambientes de desenvolvimento onde diferentes portas são utilizadas.

A configuração de múltiplos perfis de ambiente mostrou-se fundamental para facilitar desenvolvimento local enquanto prepara adequadamente para deploy em produção. Esta abordagem permite que desenvolvedores trabalhem de forma independente sem necessidade de infraestrutura complexa.

O uso de DTOs para comunicação entre camadas provou ser essencial para manter flexibilidade e evoluibilidade da API. Esta separação permite mudanças internas nas entidades sem impactar clientes da API, facilitando manutenção e versionamento.

A implementação de segurança baseada em JWT demonstrou a importância de compreender profundamente os mecanismos de autenticação modernos. A configuração adequada de filtros, interceptors e guards é crucial para garantir que a segurança seja aplicada consistentemente em toda a aplicação.

### 9.3 Melhorias Futuras

Várias melhorias podem ser implementadas para tornar o sistema ainda mais robusto e adequado para uso em produção. A implementação de testes automatizados abrangentes, incluindo testes unitários, de integração e end-to-end, seria a próxima prioridade para garantir qualidade e facilitar manutenção contínua.

A adição de funcionalidades avançadas como notificações push, sincronização offline, e colaboração entre usuários expandiria significativamente o valor do sistema. Estas funcionalidades demonstrariam conhecimento de tecnologias modernas como Service Workers, WebSockets e arquiteturas event-driven.

Implementação de observabilidade completa através de logging estruturado, métricas detalhadas e tracing distribuído melhoraria significativamente a capacidade de monitoramento e debug em produção. Integração com ferramentas como ELK Stack ou Azure Application Insights proporcionaria insights valiosos sobre performance e uso.

A implementação de CI/CD pipelines automatizados facilitaria deploy contínuo e garantiria qualidade através de validações automáticas. Esta automação é essencial para desenvolvimento ágil e entrega contínua de valor.

### 9.4 Considerações para Entrevista

Este projeto demonstra competências técnicas abrangentes que são altamente valorizadas no mercado de desenvolvimento de software. A capacidade de implementar soluções completas utilizando tecnologias modernas e seguindo boas práticas é fundamental para posições de desenvolvimento full-stack.

A documentação técnica produzida demonstra habilidades de comunicação que são essenciais para trabalho em equipe e transferência de conhecimento. A capacidade de explicar decisões técnicas e arquiteturais de forma clara é crucial para liderança técnica e mentoria de outros desenvolvedores.

O projeto showcaseia conhecimento de conceitos fundamentais como segurança de aplicações web, design de APIs, arquitetura de software e experiência do usuário. Estes conhecimentos são transferíveis para diferentes tecnologias e contextos, demonstrando adaptabilidade e capacidade de aprendizado.

A implementação completa do sistema, desde configuração de ambiente até deploy, demonstra capacidade de entregar soluções funcionais e não apenas código. Esta visão end-to-end é especialmente valiosa em organizações que valorizam desenvolvedores que compreendem todo o ciclo de vida de desenvolvimento de software.

### 9.5 Recursos Adicionais

Para aprofundar o conhecimento nas tecnologias utilizadas, recomenda-se estudo adicional em áreas específicas que são frequentemente abordadas em entrevistas técnicas. Spring Security é um tópico complexo que merece estudo aprofundado, especialmente conceitos como OAuth2, SAML e integração com provedores de identidade externos.

Angular é um framework em constante evolução, e manter-se atualizado com as últimas versões e funcionalidades é importante para demonstrar conhecimento atual. Conceitos como Signals, Standalone Components e Server-Side Rendering são tendências importantes no ecossistema Angular.

Conhecimento de DevOps e cloud computing é cada vez mais valorizado, especialmente experiência prática com Azure, AWS ou Google Cloud Platform. Certificações nestas plataformas podem complementar significativamente o conhecimento técnico demonstrado neste projeto.

Padrões de arquitetura como microservices, event sourcing e CQRS são tópicos avançados que podem diferenciar candidatos em posições sênior. Embora não implementados neste projeto, compreender quando e como aplicar estes padrões é valioso para discussões arquiteturais em entrevistas.

## 10. Referências e Documentação

### 10.1 Documentação Oficial

- [Spring Boot Documentation](https://spring.io/projects/spring-boot) - Documentação oficial do Spring Boot
- [Angular Documentation](https://angular.io/docs) - Documentação oficial do Angular
- [Microsoft SQL Server Documentation](https://docs.microsoft.com/en-us/sql/sql-server/) - Documentação do SQL Server
- [Azure SQL Database Documentation](https://docs.microsoft.com/en-us/azure/azure-sql/) - Documentação do Azure SQL Database

### 10.2 Recursos Técnicos

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/) - Guia completo de Spring Security
- [JWT.io](https://jwt.io/) - Recursos e ferramentas para JSON Web Tokens
- [Angular Style Guide](https://angular.io/guide/styleguide) - Guia de boas práticas para Angular
- [TypeScript Handbook](https://www.typescriptlang.org/docs/) - Documentação oficial do TypeScript

### 10.3 Ferramentas e Bibliotecas

- [Maven Central Repository](https://mvnrepository.com/) - Repositório de dependências Maven
- [npm Registry](https://www.npmjs.com/) - Repositório de pacotes Node.js
- [H2 Database](https://www.h2database.com/) - Banco de dados em memória para desenvolvimento
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Pool de conexões de alta performance

---


