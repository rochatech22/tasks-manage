# Relatório Técnico para Entrevista - Sistema Gerenciador de Tarefas

**Autor:** Manus AI  
**Data:** Janeiro 2025  
**Versão:** 1.0  
**Objetivo:** Preparação técnica para entrevistas de emprego

## Sumário Executivo

Este relatório técnico foi desenvolvido especificamente para preparação de entrevistas de emprego, fornecendo explicações detalhadas sobre conceitos, tecnologias e implementações utilizadas no projeto de gerenciamento de tarefas. O documento aborda tanto aspectos teóricos quanto práticos, oferecendo respostas estruturadas para perguntas técnicas comuns em entrevistas para posições de desenvolvimento Java/Angular.

O conteúdo está organizado por tecnologia e conceito, permitindo estudo direcionado baseado no foco da entrevista. Cada seção inclui explicações conceituais, justificativas para decisões técnicas, e exemplos práticos de implementação que demonstram proficiência técnica e compreensão de boas práticas da indústria.

## Índice

1. **Fundamentos Java e Spring Boot**
2. **Arquitetura e Padrões de Design**
3. **Segurança e Autenticação**
4. **Persistência de Dados e JPA**
5. **Frontend Angular e TypeScript**
6. **APIs REST e Comunicação HTTP**
7. **Banco de Dados e SQL Server**
8. **Azure e Cloud Computing**
9. **DevOps e Deployment**
10. **Perguntas e Respostas Técnicas**


## 1. Fundamentos Java e Spring Boot

### 1.1 Por que Spring Boot?

Spring Boot foi escolhido como framework principal para o backend devido à sua capacidade de simplificar significativamente o desenvolvimento de aplicações Java empresariais. O framework implementa o conceito de "convention over configuration", reduzindo drasticamente a quantidade de configuração manual necessária para criar aplicações robustas e prontas para produção.

Uma das principais vantagens do Spring Boot é o sistema de auto-configuração, que automaticamente configura componentes baseado nas dependências presentes no classpath. Por exemplo, ao incluir a dependência `spring-boot-starter-data-jpa`, o framework automaticamente configura um EntityManager, DataSource e TransactionManager, eliminando a necessidade de configuração XML complexa que era comum em versões anteriores do Spring Framework.

O sistema de starters do Spring Boot é outro diferencial importante. Cada starter é uma coleção curada de dependências que trabalham bem juntas para uma funcionalidade específica. O `spring-boot-starter-web` inclui Tomcat embedded, Spring MVC, Jackson para serialização JSON, e outras dependências necessárias para criar APIs REST. Esta abordagem elimina conflitos de versão e garante compatibilidade entre bibliotecas.

O Tomcat embedded é uma característica fundamental que transforma aplicações Spring Boot em JARs executáveis. Isto significa que a aplicação pode ser executada com um simples `java -jar`, sem necessidade de deploy em servidores de aplicação externos. Esta abordagem simplifica significativamente deployment e é ideal para arquiteturas de microservices e containerização.

### 1.2 Injeção de Dependência e IoC Container

O Spring Framework é fundamentado no padrão Inversion of Control (IoC), onde o controle de criação e gerenciamento de objetos é transferido do código da aplicação para o framework. O IoC Container do Spring é responsável por instanciar, configurar e gerenciar o ciclo de vida dos beans da aplicação.

A injeção de dependência é implementada através de três mecanismos principais: constructor injection, setter injection e field injection. No projeto, utilizamos predominantemente constructor injection, que é considerada a melhor prática por garantir que dependências obrigatórias sejam fornecidas na criação do objeto e facilitar testes unitários através de mocks.

```java
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
}
```

O container Spring utiliza reflection para analisar classes anotadas e criar um grafo de dependências. Anotações como `@Component`, `@Service`, `@Repository` e `@Controller` marcam classes para serem gerenciadas pelo container. O `@Autowired` pode ser usado para injeção automática, mas com constructor injection ele é opcional quando há apenas um construtor.

O ciclo de vida dos beans é gerenciado automaticamente pelo container, incluindo inicialização, configuração de dependências e destruição. Métodos anotados com `@PostConstruct` são executados após injeção de dependências, permitindo inicialização customizada. O escopo padrão dos beans é singleton, garantindo que apenas uma instância seja criada por container.

### 1.3 Spring MVC e Controllers REST

Spring MVC é o framework web do Spring que implementa o padrão Model-View-Controller para aplicações web. No contexto de APIs REST, os Controllers atuam como endpoints que recebem requisições HTTP, processam dados e retornam respostas estruturadas.

A anotação `@RestController` combina `@Controller` e `@ResponseBody`, indicando que todos os métodos do controller retornam dados que devem ser serializados diretamente para o corpo da resposta HTTP, tipicamente em formato JSON. Isto elimina a necessidade de views tradicionais e é ideal para APIs REST.

```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        // Implementação
    }
    
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        // Implementação
    }
}
```

O mapeamento de URLs é realizado através de anotações como `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc. Estas anotações suportam path variables (`@PathVariable`), query parameters (`@RequestParam`) e request body (`@RequestBody`). A validação automática é habilitada através da anotação `@Valid`, que aplica validações Bean Validation aos objetos de entrada.

O Spring MVC inclui um sistema robusto de resolução de content type e serialização automática. O Jackson é usado por padrão para serialização JSON, convertendo automaticamente objetos Java para JSON na resposta e vice-versa na requisição. Headers HTTP como `Content-Type` e `Accept` são processados automaticamente para determinar o formato apropriado.

### 1.4 Spring Data JPA e Repositories

Spring Data JPA é uma abstração sobre JPA (Java Persistence API) que simplifica significativamente o desenvolvimento da camada de persistência. O framework gera automaticamente implementações de repositórios baseado em interfaces, eliminando a necessidade de escrever código boilerplate para operações CRUD básicas.

A interface `JpaRepository` fornece métodos prontos para operações comuns como `save()`, `findById()`, `findAll()`, `delete()`, etc. Estes métodos são implementados automaticamente pelo Spring Data, utilizando EntityManager do JPA internamente. A implementação é gerada em tempo de execução através de proxies dinâmicos.

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserIdAndTaskDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Task> findByUserIdAndCompletedOrderByTaskDateAsc(Long userId, boolean completed);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.userId = :userId AND t.completed = :completed")
    long countByUserIdAndCompleted(@Param("userId") Long userId, @Param("completed") boolean completed);
}
```

Métodos de consulta podem ser definidos seguindo convenções de nomenclatura do Spring Data. O framework analisa o nome do método e gera automaticamente a consulta JPQL correspondente. Palavras-chave como `findBy`, `countBy`, `deleteBy` são reconhecidas, assim como operadores como `And`, `Or`, `Between`, `OrderBy`, etc.

Para consultas mais complexas, a anotação `@Query` permite definir JPQL ou SQL nativo. Parâmetros podem ser passados por posição ou nome usando `@Param`. O Spring Data também suporta paginação através da interface `Pageable` e especificações dinâmicas através da interface `Specification`.

### 1.5 Tratamento de Exceções e Validação

O Spring Boot fornece mecanismos robustos para tratamento de exceções e validação de dados, essenciais para criar APIs confiáveis e user-friendly. O tratamento global de exceções é implementado através de `@ControllerAdvice`, permitindo centralizar lógica de tratamento de erros.

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Processar erros de validação e retornar resposta estruturada
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        // Tratar entidades não encontradas
    }
}
```

Bean Validation (JSR-303) é integrado automaticamente ao Spring Boot, permitindo validação declarativa através de anotações como `@NotNull`, `@NotBlank`, `@Email`, `@Size`, etc. Estas validações são aplicadas automaticamente quando `@Valid` é usado em parâmetros de métodos de controller.

Validações customizadas podem ser criadas implementando `ConstraintValidator` e definindo anotações personalizadas. Isto permite encapsular regras de negócio complexas em validadores reutilizáveis que podem ser aplicados em diferentes contextos.

O Spring Boot configura automaticamente um `Validator` baseado em Hibernate Validator, que é a implementação de referência da especificação Bean Validation. Mensagens de erro podem ser customizadas através de arquivos de propriedades, suportando internacionalização quando necessário.


## 2. Arquitetura e Padrões de Design

### 2.1 Arquitetura em Camadas (Layered Architecture)

A arquitetura em camadas é um padrão fundamental utilizado no projeto para organizar código de forma que promova separação de responsabilidades, manutenibilidade e testabilidade. Cada camada tem uma responsabilidade específica e se comunica apenas com camadas adjacentes, criando um design modular e bem estruturado.

A camada de apresentação (Controllers) é responsável por receber requisições HTTP, validar dados de entrada, chamar serviços apropriados e formatar respostas. Esta camada não contém lógica de negócio, atuando apenas como uma interface entre o mundo externo e a aplicação. Controllers são stateless e focados em uma única responsabilidade, facilitando testes unitários e manutenção.

A camada de negócio (Services) encapsula toda a lógica de negócio da aplicação, incluindo validações complexas, transformações de dados e orquestração de operações que envolvem múltiplas entidades. Services são transacionais por natureza, garantindo consistência de dados através de anotações `@Transactional`. Esta camada é independente de detalhes de persistência e apresentação, permitindo reutilização e facilitando testes.

A camada de persistência (Repositories) abstrai o acesso aos dados, fornecendo uma interface limpa para operações de banco de dados. Utilizando Spring Data JPA, esta camada elimina código boilerplate e fornece implementações automáticas para operações CRUD básicas. Consultas customizadas são definidas de forma declarativa, mantendo o código limpo e focado.

A camada de domínio (Entities) representa o modelo de dados da aplicação, encapsulando tanto dados quanto comportamentos relevantes. Entities são ricas em comportamento quando apropriado, incluindo validações de negócio e métodos que mantêm consistência de estado. Esta abordagem segue princípios de Domain-Driven Design, onde o modelo de domínio é central para a aplicação.

### 2.2 Padrão Repository

O padrão Repository encapsula a lógica necessária para acessar fontes de dados, centralizando funcionalidades de acesso a dados e promovendo melhor manutenibilidade e testabilidade. No Spring Data JPA, este padrão é implementado através de interfaces que estendem `JpaRepository`, fornecendo uma abstração limpa sobre operações de persistência.

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndTaskDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND t.title LIKE %:title%")
    List<Task> findByUserIdAndTitleContaining(@Param("userId") Long userId, @Param("title") String title);
}
```

O padrão Repository oferece várias vantagens importantes. Primeiro, ele desacopla a lógica de negócio dos detalhes de acesso a dados, permitindo que mudanças na tecnologia de persistência não afetem outras camadas da aplicação. Segundo, facilita testes unitários através da possibilidade de criar implementações mock dos repositórios.

Spring Data JPA implementa o padrão Repository de forma elegante, gerando automaticamente implementações baseadas em convenções de nomenclatura. Métodos como `findByPropertyName` são automaticamente traduzidos para consultas JPQL apropriadas. Esta abordagem reduz significativamente a quantidade de código necessário para operações de persistência comuns.

Para consultas mais complexas, o padrão suporta definição de consultas customizadas através de anotações `@Query` ou implementações customizadas. Isto permite balancear a conveniência da geração automática com a flexibilidade necessária para casos de uso específicos.

### 2.3 Padrão DTO (Data Transfer Object)

O padrão DTO é utilizado para transferir dados entre diferentes camadas da aplicação, especialmente entre a camada de apresentação e a camada de negócio. DTOs servem como contratos de API, definindo exatamente quais dados são expostos para clientes externos e como estes dados são estruturados.

```java
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate taskDate;
    private boolean completed;
    private Priority priority;
    private Category category;
    
    // Construtores, getters e setters
}
```

A utilização de DTOs oferece várias vantagens importantes. Primeiro, eles desacoplam a estrutura interna das entidades da API pública, permitindo evolução independente do modelo de dados interno sem quebrar clientes existentes. Segundo, DTOs permitem controlar exatamente quais dados são expostos, evitando vazamento de informações sensíveis.

DTOs também facilitam versionamento de APIs, permitindo que diferentes versões da API utilizem diferentes DTOs enquanto mantêm o mesmo modelo de domínio interno. Esta flexibilidade é crucial para manter compatibilidade com clientes existentes enquanto evolui a funcionalidade da aplicação.

A conversão entre entidades e DTOs pode ser realizada manualmente ou através de bibliotecas como MapStruct ou ModelMapper. No projeto, utilizamos conversão manual para manter controle total sobre o processo e garantir que apenas dados apropriados sejam transferidos.

### 2.4 Padrão MVC (Model-View-Controller)

O padrão MVC é fundamental na arquitetura do sistema, separando responsabilidades entre modelo de dados (Model), apresentação (View) e lógica de controle (Controller). No contexto de APIs REST, este padrão é adaptado onde Controllers servem como endpoints REST, Models representam dados e estruturas, e Views são substituídas por serialização JSON.

Controllers no Spring MVC são responsáveis por receber requisições HTTP, extrair dados relevantes, chamar serviços apropriados e retornar respostas formatadas. Eles atuam como uma camada fina que traduz entre o protocolo HTTP e a lógica de negócio da aplicação. Esta separação permite que a lógica de negócio seja independente de detalhes de protocolo.

```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
}
```

O Model no contexto de APIs REST é representado por DTOs e entidades de domínio. DTOs servem como modelos para transferência de dados, enquanto entidades representam o modelo de domínio interno. Esta separação permite evolução independente da API e do modelo interno.

A "View" em APIs REST é implementada através de serialização automática para JSON, realizada pelo Jackson. Esta abordagem elimina a necessidade de templates de view tradicionais, mas mantém o princípio de separação entre dados e apresentação.

### 2.5 Dependency Injection e Inversão de Controle

Dependency Injection (DI) é um padrão de design que implementa Inversão de Controle (IoC), onde dependências de um objeto são fornecidas externamente ao invés de serem criadas internamente. Este padrão é fundamental no Spring Framework e promove baixo acoplamento, alta coesão e facilita testes unitários.

No projeto, DI é implementado principalmente através de constructor injection, considerada a melhor prática por garantir que dependências obrigatórias sejam fornecidas na criação do objeto e facilitar criação de objetos imutáveis. Constructor injection também facilita testes unitários, permitindo injeção de mocks através do construtor.

```java
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
}
```

O Spring IoC Container gerencia o ciclo de vida completo dos beans, incluindo criação, configuração de dependências, inicialização e destruição. O container utiliza reflection para analisar classes e suas dependências, criando um grafo de dependências que é resolvido automaticamente.

Anotações como `@Component`, `@Service`, `@Repository` e `@Controller` marcam classes para serem gerenciadas pelo container. Estas anotações são especializações de `@Component` que fornecem semântica adicional e podem ser utilizadas por ferramentas de análise e aspectos.

O padrão DI facilita significativamente testes unitários, permitindo injeção de implementações mock das dependências. Isto permite testar classes isoladamente, focando na lógica específica da classe sob teste sem dependências externas como bancos de dados ou serviços web.

### 2.6 Padrão Strategy para Enums

O uso de enums para representar prioridades e categorias de tarefas implementa implicitamente o padrão Strategy, onde diferentes estratégias (valores enum) podem ser utilizadas de forma intercambiável. Esta abordagem garante type safety e facilita extensão futura de valores possíveis.

```java
public enum Priority {
    LOW("Baixa"),
    MEDIUM("Média"), 
    HIGH("Alta"),
    URGENT("Urgente");
    
    private final String label;
    
    Priority(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
```

Enums em Java são type-safe e fornecem um conjunto fixo de constantes que podem ser utilizadas de forma consistente em toda a aplicação. Eles são automaticamente serializáveis e podem incluir métodos e propriedades adicionais, tornando-os mais poderosos que simples constantes.

O padrão Strategy através de enums permite que diferentes comportamentos sejam encapsulados em cada valor enum. Por exemplo, cada prioridade poderia ter métodos específicos para cálculo de urgência ou ordenação, mantendo a lógica relacionada encapsulada junto com os dados.

Esta abordagem também facilita validação e conversão automática em APIs REST, onde valores enum são automaticamente validados pelo Spring Boot e convertidos entre representações string e enum conforme necessário.


## 3. Segurança e Autenticação

### 3.1 JSON Web Tokens (JWT)

JWT é um padrão aberto (RFC 7519) para transmitir informações de forma segura entre partes como um objeto JSON. No projeto, JWT é utilizado para implementar autenticação stateless, onde o servidor não precisa manter estado de sessão, facilitando escalabilidade horizontal e arquiteturas distribuídas.

Um token JWT consiste em três partes separadas por pontos: header, payload e signature. O header contém metadados sobre o tipo de token e algoritmo de assinatura utilizado. O payload contém as claims (declarações) sobre o usuário e metadados adicionais como expiração. A signature garante que o token não foi alterado e autentica o emissor.

```java
public class JwtUtil {
    private final String secret = "mySecretKey";
    private final int jwtExpiration = 86400000; // 24 horas
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
```

A principal vantagem do JWT é que ele é self-contained, ou seja, contém todas as informações necessárias para validação sem necessidade de consultar um banco de dados ou cache. Isto reduz latência e elimina pontos únicos de falha associados a armazenamento de sessão centralizado.

JWT suporta diferentes algoritmos de assinatura, incluindo HMAC (symmetric) e RSA/ECDSA (asymmetric). No projeto, utilizamos HMAC SHA-512 por simplicidade, mas em ambientes de produção com múltiplos serviços, algoritmos assimétricos podem ser preferíveis para permitir verificação sem compartilhar chaves secretas.

A validação de JWT inclui verificação de assinatura, expiração e estrutura do token. Claims customizadas podem ser adicionadas para incluir informações específicas da aplicação, como roles do usuário ou identificadores de tenant em aplicações multi-tenant.

### 3.2 Spring Security Framework

Spring Security é um framework abrangente que fornece autenticação, autorização e proteção contra ataques comuns para aplicações Java. O framework é altamente customizável e integra-se perfeitamente com o ecossistema Spring, fornecendo segurança declarativa através de anotações e configuração.

A arquitetura do Spring Security é baseada em filtros que interceptam requisições HTTP antes que elas alcancem os controllers. Estes filtros formam uma cadeia (filter chain) onde cada filtro tem uma responsabilidade específica, como autenticação, autorização, proteção CSRF, etc.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

O AuthenticationManager é o componente central responsável por processar tentativas de autenticação. Ele delega para AuthenticationProviders específicos baseado no tipo de Authentication object. No projeto, utilizamos DaoAuthenticationProvider que autentica contra dados armazenados em banco de dados.

UserDetailsService é uma interface que carrega dados específicos do usuário durante autenticação. Nossa implementação customizada (CustomUserDetailsService) integra com o repositório de usuários para carregar informações necessárias para autenticação e autorização.

O SecurityContext mantém informações de segurança associadas à thread atual, incluindo detalhes do usuário autenticado. Este contexto é estabelecido após autenticação bem-sucedida e pode ser acessado em qualquer ponto da aplicação para obter informações do usuário atual.

### 3.3 Filtros de Autenticação Customizados

Filtros customizados são essenciais para implementar autenticação JWT no Spring Security. O JwtAuthenticationFilter intercepta todas as requisições, extrai tokens JWT dos headers Authorization, valida os tokens e estabelece o contexto de segurança quando apropriado.

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

O filtro estende OncePerRequestFilter para garantir que seja executado apenas uma vez por requisição, evitando processamento duplicado em cenários de forward ou include. Esta classe base também fornece métodos para pular o filtro em certas condições.

A validação do token inclui verificação de assinatura, expiração e estrutura. Se o token for válido, um UsernamePasswordAuthenticationToken é criado e adicionado ao SecurityContext, estabelecendo a identidade do usuário para a requisição atual.

O filtro é registrado na cadeia de filtros antes do UsernamePasswordAuthenticationFilter padrão, garantindo que autenticação JWT seja processada antes de outros mecanismos de autenticação. Esta ordem é crucial para o funcionamento correto do sistema de segurança.

### 3.4 Hashing de Senhas com BCrypt

BCrypt é um algoritmo de hashing de senhas baseado no cipher Blowfish, projetado especificamente para ser computacionalmente caro e resistente a ataques de força bruta. O algoritmo inclui salt automático e permite configuração do "cost factor" para ajustar a dificuldade computacional.

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Cost factor 12
    }
}
```

O salt é um valor aleatório adicionado à senha antes do hashing, garantindo que senhas idênticas resultem em hashes diferentes. BCrypt gera automaticamente um salt único para cada senha, eliminando a necessidade de gerenciamento manual de salts e prevenindo ataques de rainbow table.

O cost factor determina quantas iterações do algoritmo são executadas, dobrando o tempo de processamento a cada incremento. Um valor de 12 significa 2^12 = 4096 iterações, proporcionando boa segurança com performance aceitável. Este valor pode ser ajustado baseado na capacidade computacional disponível.

BCrypt é adaptativo, permitindo aumento do cost factor ao longo do tempo conforme hardware se torna mais poderoso. Senhas hasheadas com cost factors menores podem ser re-hasheadas com valores maiores durante login, mantendo segurança atualizada sem forçar reset de senhas.

A verificação de senhas é realizada através do método matches() do PasswordEncoder, que compara a senha em texto plano com o hash armazenado. Este processo extrai o salt e cost factor do hash existente, aplica o mesmo processo à senha fornecida e compara os resultados.

### 3.5 CORS (Cross-Origin Resource Sharing)

CORS é um mecanismo que permite que recursos de uma página web sejam acessados por outra página de um domínio diferente. No contexto de SPAs (Single Page Applications) que consomem APIs REST, CORS é essencial para permitir comunicação entre frontend e backend executando em portas ou domínios diferentes.

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

O navegador implementa CORS através de preflight requests para métodos HTTP não-simples (PUT, DELETE) ou quando headers customizados são utilizados. Estas requisições OPTIONS verificam se o servidor permite a operação antes de enviar a requisição real.

Configuração adequada de CORS é crucial para segurança. Em desenvolvimento, permitir todas as origens (*) é conveniente, mas em produção deve-se especificar exatamente quais domínios são autorizados. Isto previne que sites maliciosos façam requisições não autorizadas para a API.

Headers como Access-Control-Allow-Origin, Access-Control-Allow-Methods e Access-Control-Allow-Headers são utilizados pelo servidor para comunicar ao navegador quais operações são permitidas. O Spring Security integra-se com configurações CORS para aplicar políticas consistentemente.

### 3.6 Proteção contra Vulnerabilidades Comuns

O sistema implementa proteções contra vulnerabilidades web comuns identificadas no OWASP Top 10. Injeção SQL é prevenida através do uso de JPA e consultas parametrizadas, que automaticamente escapam valores de entrada e previnem manipulação de consultas.

Cross-Site Scripting (XSS) é mitigado através de validação rigorosa de entrada e encoding automático de saída. O Spring Boot configura automaticamente headers de segurança como X-Content-Type-Options e X-Frame-Options para prevenir ataques de clickjacking e MIME sniffing.

```java
@RestController
public class TaskController {
    
    @PostMapping("/api/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        // @Valid garante validação automática de entrada
        // JPA previne SQL injection através de consultas parametrizadas
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }
}
```

Autenticação quebrada é prevenida através de implementação robusta de JWT com expiração apropriada e validação rigorosa. Senhas são sempre hasheadas com BCrypt e nunca armazenadas em texto plano. Tokens são validados em cada requisição para garantir que não foram comprometidos.

Exposição de dados sensíveis é prevenida através do uso de DTOs que controlam exatamente quais dados são expostos nas APIs. Informações como senhas hasheadas e dados internos nunca são incluídos em respostas de API. HTTPS é obrigatório em produção para criptografar dados em trânsito.

Controle de acesso é implementado através de validação de propriedade, garantindo que usuários só possam acessar seus próprios dados. Cada operação verifica se o usuário autenticado tem permissão para acessar o recurso solicitado, prevenindo acesso não autorizado a dados de outros usuários.


## 4. Persistência de Dados e JPA

### 4.1 Java Persistence API (JPA) Fundamentals

JPA é uma especificação Java que define uma API padrão para mapeamento objeto-relacional (ORM), permitindo que desenvolvedores trabalhem com dados relacionais usando paradigma orientado a objetos. JPA abstrai diferenças entre diferentes sistemas de banco de dados, proporcionando portabilidade e reduzindo dependência de SQL específico de vendor.

Hibernate é a implementação JPA mais popular e é utilizada por padrão no Spring Boot. Hibernate fornece funcionalidades avançadas como lazy loading, caching de segundo nível, e otimizações automáticas de consultas. A integração com Spring Boot é transparente, com configuração automática baseada em propriedades de aplicação.

```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;
    
    @Column(nullable = false)
    private boolean completed = false;
    
    @Enumerated(EnumType.STRING)
    private Priority priority;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
```

O EntityManager é a interface principal para interagir com o contexto de persistência, gerenciando o ciclo de vida das entidades e executando operações de banco de dados. Spring Data JPA abstrai o EntityManager através de repositórios, mas compreender seu funcionamento é crucial para otimização de performance.

O contexto de persistência (Persistence Context) é um cache de primeiro nível que mantém entidades gerenciadas durante uma transação. Entidades no contexto são automaticamente sincronizadas com o banco de dados quando a transação é commitada, implementando o padrão Unit of Work.

### 4.2 Mapeamento Objeto-Relacional

O mapeamento objeto-relacional é o processo de converter dados entre sistemas incompatíveis (orientado a objetos e relacional). JPA utiliza anotações para definir como classes Java são mapeadas para tabelas de banco de dados, incluindo relacionamentos, constraints e estratégias de geração de chaves.

Anotações básicas como `@Entity`, `@Table`, `@Id` e `@Column` definem a estrutura básica do mapeamento. `@Entity` marca uma classe como entidade JPA, `@Table` especifica o nome da tabela (opcional se igual ao nome da classe), `@Id` marca a chave primária, e `@Column` customiza mapeamento de colunas.

```java
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

Estratégias de geração de chaves primárias são definidas através de `@GeneratedValue`. `GenerationType.IDENTITY` utiliza auto-increment do banco de dados, `GenerationType.SEQUENCE` usa sequências, `GenerationType.TABLE` usa uma tabela auxiliar, e `GenerationType.AUTO` deixa o provider escolher a estratégia mais apropriada.

Tipos de dados Java são automaticamente mapeados para tipos SQL apropriados, mas podem ser customizados através de `@Column`. Enums podem ser mapeados como strings (`EnumType.STRING`) ou ordinais (`EnumType.ORDINAL`), sendo strings preferíveis por serem mais legíveis e resistentes a reordenação.

### 4.3 Relacionamentos JPA

Relacionamentos entre entidades são fundamentais em aplicações que modelam domínios complexos. JPA suporta todos os tipos de relacionamentos relacionais: one-to-one, one-to-many, many-to-one e many-to-many. Cada tipo de relacionamento tem características específicas de performance e uso de memória.

O relacionamento `@ManyToOne` é o mais comum e eficiente, representando o lado "muitos" de um relacionamento. No projeto, cada Task pertence a um User, estabelecido através de uma foreign key. Este relacionamento é unidirecional por simplicidade, mas poderia ser bidirecional se necessário.

```java
@Entity
public class Task {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

// Se fosse bidirecional:
@Entity
public class User {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}
```

Estratégias de fetch (LAZY vs EAGER) controlam quando dados relacionados são carregados. `FetchType.LAZY` carrega dados apenas quando acessados, reduzindo uso de memória e melhorando performance inicial. `FetchType.EAGER` carrega dados imediatamente, útil quando relacionamentos são sempre necessários.

Cascade operations permitem que operações em entidades pai sejam automaticamente aplicadas a entidades filhas. `CascadeType.ALL` aplica todas as operações, `CascadeType.PERSIST` apenas persistência, etc. `orphanRemoval = true` remove automaticamente entidades filhas que não estão mais referenciadas pelo pai.

### 4.4 JPQL e Consultas Customizadas

JPQL (Java Persistence Query Language) é uma linguagem de consulta orientada a objetos que opera sobre entidades JPA ao invés de tabelas de banco de dados. JPQL é independente de banco de dados e é traduzida para SQL específico do vendor pelo provider JPA.

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.taskDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByUserAndDateRange(@Param("userId") Long userId, 
                                          @Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.completed = :completed")
    long countTasksByUserAndStatus(@Param("userId") Long userId, @Param("completed") boolean completed);
    
    @Modifying
    @Query("UPDATE Task t SET t.completed = :completed WHERE t.id = :taskId AND t.user.id = :userId")
    int updateTaskStatus(@Param("taskId") Long taskId, @Param("userId") Long userId, @Param("completed") boolean completed);
}
```

Consultas nomeadas podem ser definidas usando convenções de nomenclatura do Spring Data, onde o nome do método é automaticamente traduzido para JPQL. Palavras-chave como `findBy`, `countBy`, `deleteBy` são reconhecidas, assim como operadores como `And`, `Or`, `Between`, `Like`, etc.

Parâmetros podem ser passados por posição (?1, ?2) ou por nome (:paramName). Parâmetros nomeados são preferíveis por serem mais legíveis e menos propensos a erros quando a ordem dos parâmetros muda. A anotação `@Param` vincula parâmetros do método a parâmetros da consulta.

Consultas de modificação (UPDATE, DELETE) devem ser anotadas com `@Modifying` e executadas dentro de transações. Estas consultas retornam o número de registros afetados e podem ser úteis para operações em lote que seriam ineficientes se realizadas entidade por entidade.

### 4.5 Transações e Gerenciamento de Estado

Transações são fundamentais para manter consistência de dados em operações que envolvem múltiplas entidades ou operações de banco de dados. Spring fornece gerenciamento declarativo de transações através da anotação `@Transactional`, que utiliza AOP (Aspect-Oriented Programming) para aplicar comportamento transacional.

```java
@Service
@Transactional
public class TaskService {
    
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasksByUser(Long userId) {
        // Operação somente leitura - otimização de performance
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        // Operação de escrita - transação completa
        Task task = convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
}
```

O atributo `readOnly = true` otimiza transações que apenas leem dados, permitindo que o provider JPA aplique otimizações como flush mode manual e uso de conexões read-only. Esta otimização pode melhorar significativamente performance em operações de consulta.

Propagação de transação controla como transações se comportam quando métodos transacionais chamam outros métodos transacionais. `REQUIRED` (padrão) participa da transação existente ou cria uma nova, `REQUIRES_NEW` sempre cria uma nova transação, `SUPPORTS` participa se existir mas não cria nova.

Isolamento de transação controla como transações concorrentes interagem. `READ_COMMITTED` (padrão na maioria dos bancos) previne dirty reads, `REPEATABLE_READ` também previne non-repeatable reads, `SERIALIZABLE` previne phantom reads mas com impacto significativo na performance.

### 4.6 Otimização de Performance

Performance em aplicações JPA requer compreensão de como consultas são executadas e como dados são carregados. O problema N+1 é comum quando relacionamentos lazy são acessados em loops, resultando em uma consulta inicial mais N consultas adicionais para carregar relacionamentos.

```java
// Problema N+1
List<Task> tasks = taskRepository.findAll(); // 1 consulta
for (Task task : tasks) {
    String userName = task.getUser().getName(); // N consultas adicionais
}

// Solução com fetch join
@Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId")
List<Task> findTasksWithUser(@Param("userId") Long userId);
```

Fetch joins carregam relacionamentos em uma única consulta, eliminando o problema N+1. `JOIN FETCH` força carregamento eager do relacionamento especificado, independentemente da estratégia de fetch configurada na entidade.

Projeções permitem carregar apenas campos necessários ao invés de entidades completas, reduzindo uso de memória e tráfego de rede. Interfaces de projeção ou DTOs podem ser utilizados como tipos de retorno em métodos de repositório.

```java
public interface TaskProjection {
    Long getId();
    String getTitle();
    LocalDate getTaskDate();
    boolean isCompleted();
}

@Query("SELECT t.id as id, t.title as title, t.taskDate as taskDate, t.completed as completed FROM Task t WHERE t.user.id = :userId")
List<TaskProjection> findTaskProjectionsByUserId(@Param("userId") Long userId);
```

Paginação é essencial para aplicações que lidam com grandes volumes de dados. Spring Data fornece suporte nativo através da interface `Pageable`, que pode ser utilizada em métodos de repositório para implementar paginação automática.

Cache de segundo nível pode ser configurado para entidades que são frequentemente acessadas mas raramente modificadas. Hibernate suporta vários providers de cache como Ehcache e Hazelcast, mas deve ser usado com cuidado para evitar problemas de consistência em ambientes distribuídos.


## 5. Frontend Angular e TypeScript

### 5.1 Arquitetura de Componentes Angular

Angular é um framework baseado em componentes onde cada componente encapsula sua própria lógica, template e estilos. Esta arquitetura promove reutilização de código, manutenibilidade e testabilidade através de separação clara de responsabilidades. Componentes são organizados hierarquicamente, formando uma árvore onde componentes pais orquestram componentes filhos.

```typescript
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
  
  onEditTask(task: Task) {
    this.editTask.emit(task);
  }
}
```

O decorator `@Component` define metadados essenciais como seletor CSS, template e estilos. O seletor determina como o componente é utilizado em templates HTML. Templates podem ser inline (template) ou externos (templateUrl), sendo externos preferíveis para templates complexos.

Standalone components são uma funcionalidade moderna do Angular que elimina a necessidade de NgModules para componentes simples. Eles importam diretamente suas dependências através da propriedade `imports`, simplificando a configuração e melhorando tree-shaking.

A comunicação entre componentes é realizada através de `@Input()` para dados que fluem do pai para o filho e `@Output()` com EventEmitter para eventos que fluem do filho para o pai. Esta abordagem unidirecional facilita debugging e mantém o fluxo de dados previsível.

### 5.2 TypeScript e Type Safety

TypeScript é um superset do JavaScript que adiciona tipagem estática opcional, permitindo detecção de erros em tempo de compilação e melhorando significativamente a experiência de desenvolvimento através de IntelliSense e refactoring automático.

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
}

enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  URGENT = 'URGENT'
}
```

Interfaces definem contratos que objetos devem seguir, garantindo que propriedades obrigatórias estejam presentes e tenham tipos corretos. Propriedades opcionais são marcadas com `?`, permitindo flexibilidade quando apropriado. Interfaces são removidas durante compilação, não afetando o bundle final.

Enums fornecem uma forma de definir constantes nomeadas com type safety. String enums são preferíveis a numeric enums por serem mais legíveis em debugging e mais resistentes a mudanças na ordem dos valores. Enums são preservados em runtime, permitindo iteração e validação.

Generics permitem criar componentes reutilizáveis que trabalham com diferentes tipos mantendo type safety. Por exemplo, `Observable<T>` garante que o tipo de dados emitido seja conhecido em tempo de compilação, permitindo que o TypeScript detecte erros de tipo.

Union types (`string | number`) permitem que variáveis aceitem múltiplos tipos, útil para APIs que podem retornar diferentes formatos. Type guards podem ser utilizados para estreitar tipos em runtime, permitindo acesso a propriedades específicas de cada tipo.

### 5.3 Reactive Programming com RxJS

RxJS é uma biblioteca para programação reativa usando observables, que são streams de dados que podem emitir múltiplos valores ao longo do tempo. Angular utiliza RxJS extensivamente para gerenciar operações assíncronas como HTTP requests, eventos de usuário e mudanças de estado.

```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', credentials)
      .pipe(
        tap(response => {
          this.setToken(response.token);
          this.currentUserSubject.next(response.user);
        }),
        catchError(error => {
          console.error('Login failed:', error);
          return throwError(() => error);
        })
      );
  }
}
```

BehaviorSubject é um tipo especial de Subject que mantém o último valor emitido e o fornece imediatamente para novos subscribers. Isto é útil para estado da aplicação que precisa ser acessível por múltiplos componentes e deve ter um valor inicial.

Operators como `tap`, `map`, `filter`, `catchError` permitem transformar e manipular streams de dados de forma declarativa. `tap` executa efeitos colaterais sem modificar o stream, `map` transforma valores, `catchError` trata erros e permite recuperação.

O padrão async pipe no template automaticamente subscribe e unsubscribe de observables, prevenindo memory leaks e simplificando o código do componente. `{{ user$ | async }}` automaticamente gerencia a subscription e atualiza a view quando novos valores são emitidos.

### 5.4 Formulários Reativos

Formulários reativos oferecem uma abordagem model-driven para gerenciar formulários, onde a estrutura e validação são definidas no componente TypeScript ao invés do template. Esta abordagem oferece melhor type safety, testabilidade e controle sobre validação.

```typescript
@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.html'
})
export class TaskFormComponent implements OnInit {
  taskForm: FormGroup;
  
  constructor(private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(200)]],
      description: ['', Validators.maxLength(1000)],
      taskDate: ['', Validators.required],
      priority: [Priority.MEDIUM, Validators.required],
      category: [Category.PERSONAL, Validators.required]
    });
  }
  
  onSubmit() {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;
      // Processar dados do formulário
    }
  }
}
```

FormBuilder simplifica a criação de FormGroups complexos, fornecendo uma API fluente para definir controles, validadores e valores iniciais. FormGroup representa um grupo de controles relacionados, enquanto FormControl representa um campo individual.

Validadores built-in como `Validators.required`, `Validators.email`, `Validators.minLength` cobrem casos comuns. Validadores customizados podem ser criados como funções que retornam null para valores válidos ou um objeto de erro para valores inválidos.

```typescript
// Validador customizado
function dateNotInPast(control: AbstractControl): ValidationErrors | null {
  const today = new Date();
  const inputDate = new Date(control.value);
  
  if (inputDate < today) {
    return { dateInPast: true };
  }
  
  return null;
}
```

Estados de formulário como `valid`, `invalid`, `pending`, `touched`, `dirty` permitem controlar comportamento da UI baseado no estado atual. Estes estados são automaticamente atualizados conforme o usuário interage com o formulário.

### 5.5 Roteamento e Navegação

O Angular Router gerencia navegação entre diferentes views da aplicação, implementando roteamento client-side que permite criar Single Page Applications (SPAs) com URLs amigáveis e navegação fluida.

```typescript
const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'dashboard', 
    component: DashboardComponent, 
    canActivate: [AuthGuard] 
  },
  { path: '**', redirectTo: '/dashboard' }
];
```

Guards são serviços que controlam se uma rota pode ser ativada, desativada ou carregada. `CanActivate` é usado para proteger rotas que requerem autenticação, verificando se o usuário está logado antes de permitir acesso ao componente.

```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  
  canActivate(): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
```

Navegação programática é realizada através do Router service, permitindo redirecionamento baseado em lógica de negócio. `router.navigate(['/path'])` navega para uma rota específica, enquanto `router.navigateByUrl('/path')` aceita URLs completas.

Route parameters e query parameters permitem passar dados entre rotas. Route parameters (`/user/:id`) são parte da URL e obrigatórios, enquanto query parameters (`/search?q=term`) são opcionais e úteis para filtros e estado de busca.

### 5.6 HTTP Client e Interceptors

O HttpClient do Angular fornece uma API simplificada para realizar requisições HTTP, com suporte nativo para observables, interceptors e type safety. Todas as operações retornam observables que podem ser compostos com operators RxJS.

```typescript
@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8080/api/tasks';
  
  constructor(private http: HttpClient) {}
  
  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }
  
  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }
  
  updateTask(id: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, task);
  }
}
```

Type safety é mantida através de generics, onde `http.get<Task[]>()` garante que o retorno seja tipado como array de Task. Isto permite detecção de erros em tempo de compilação e melhor IntelliSense.

Interceptors permitem interceptar e modificar requisições HTTP de forma global. O AuthInterceptor adiciona automaticamente tokens de autenticação a todas as requisições, centralizando esta lógica e eliminando duplicação de código.

```typescript
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    
    if (token) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(authReq);
    }
    
    return next.handle(req);
  }
}
```

Error handling pode ser implementado globalmente através de interceptors ou localmente em cada service. O operator `catchError` permite transformar erros HTTP em mensagens amigáveis ao usuário e implementar retry logic quando apropriado.

### 5.7 Change Detection e Performance

Angular utiliza um sistema de change detection para detectar quando dados mudam e atualizar a view correspondente. Por padrão, Angular verifica todos os componentes da árvore em cada ciclo de change detection, que pode ser ineficiente em aplicações grandes.

OnPush change detection strategy otimiza performance verificando componentes apenas quando suas inputs mudam ou eventos são emitidos. Esta estratégia requer que componentes sejam "puros", ou seja, sua saída depende apenas de suas inputs.

```typescript
@Component({
  selector: 'app-task-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="task-item">
      <h3>{{ task.title }}</h3>
      <p>{{ task.description }}</p>
    </div>
  `
})
export class TaskItemComponent {
  @Input() task: Task;
}
```

TrackBy functions otimizam rendering de listas, permitindo que Angular identifique quais itens mudaram, foram adicionados ou removidos. Sem trackBy, Angular recria todos os elementos DOM quando a lista muda.

```typescript
trackByTaskId(index: number, task: Task): number {
  return task.id;
}
```

Lazy loading de módulos reduz o bundle inicial carregando código apenas quando necessário. Isto melhora significativamente o tempo de carregamento inicial, especialmente importante para aplicações grandes.

Async pipe automaticamente subscribe e unsubscribe de observables, prevenindo memory leaks. Também marca o componente para check quando novos valores são emitidos, garantindo que a view seja atualizada apropriadamente.


## 6. APIs REST e Comunicação HTTP

### 6.1 Princípios REST

REST (Representational State Transfer) é um estilo arquitetural para sistemas distribuídos que define um conjunto de constraints para criar web services escaláveis e maintíveis. REST não é um protocolo ou padrão, mas sim um conjunto de princípios que, quando seguidos, resultam em APIs bem projetadas e interoperáveis.

O princípio fundamental do REST é que recursos são identificados por URIs e manipulados através de representações. Um recurso é qualquer informação que pode ser nomeada, como um usuário, uma tarefa, ou uma coleção de tarefas. As representações são formatos de dados (JSON, XML) que descrevem o estado atual do recurso.

```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    // GET /api/tasks - Listar todas as tarefas
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // GET /api/tasks/123 - Obter tarefa específica
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }
    
    // POST /api/tasks - Criar nova tarefa
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
}
```

Statelessness é outro princípio fundamental onde cada requisição deve conter todas as informações necessárias para ser processada. O servidor não mantém estado de sessão entre requisições, o que facilita escalabilidade e permite que qualquer servidor processe qualquer requisição.

Uniform Interface define que a interface entre cliente e servidor deve ser uniforme, simplificando a arquitetura e permitindo que cada parte evolua independentemente. Isto inclui identificação de recursos através de URIs, manipulação através de representações, mensagens auto-descritivas e HATEOAS.

### 6.2 Métodos HTTP e Semântica

Cada método HTTP tem semântica específica que deve ser respeitada para criar APIs RESTful consistentes. GET é usado para recuperar dados e deve ser idempotente e safe (sem efeitos colaterais). POST é usado para criar recursos e não é idempotente nem safe.

PUT é usado para atualizar recursos completamente e deve ser idempotente. Múltiplas chamadas PUT com os mesmos dados devem resultar no mesmo estado final. PATCH é usado para atualizações parciais e pode ou não ser idempotente dependendo da implementação.

```java
// PUT - Atualização completa (idempotente)
@PutMapping("/{id}")
public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
    TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
    return ResponseEntity.ok(updatedTask);
}

// PATCH - Atualização parcial
@PatchMapping("/{id}/toggle")
public ResponseEntity<TaskDTO> toggleTaskCompletion(@PathVariable Long id, @RequestBody Map<String, Boolean> update) {
    boolean completed = update.get("completed");
    TaskDTO updatedTask = taskService.toggleTaskCompletion(id, completed);
    return ResponseEntity.ok(updatedTask);
}

// DELETE - Remoção de recurso (idempotente)
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
}
```

DELETE é usado para remover recursos e deve ser idempotente. Deletar um recurso que já foi deletado deve retornar o mesmo resultado (tipicamente 404 ou 204). OPTIONS é usado para descobrir quais métodos são suportados por um recurso.

Idempotência é crucial para confiabilidade em redes não confiáveis. Clientes podem repetir requisições idempotentes com segurança em caso de timeout ou falha de rede, sabendo que múltiplas execuções não causarão efeitos colaterais indesejados.

### 6.3 Códigos de Status HTTP

Códigos de status HTTP comunicam o resultado de uma requisição de forma padronizada. Usar códigos apropriados é essencial para criar APIs que sejam fáceis de usar e debugar. Códigos são agrupados em classes: 1xx (informacional), 2xx (sucesso), 3xx (redirecionamento), 4xx (erro do cliente), 5xx (erro do servidor).

```java
@PostMapping
public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
    try {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/tasks/" + createdTask.getId()))
                .body(createdTask);
    } catch (ValidationException e) {
        return ResponseEntity.badRequest().build();
    }
}

@GetMapping("/{id}")
public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
    Optional<TaskDTO> task = taskService.findTaskById(id);
    return task.map(t -> ResponseEntity.ok(t))
               .orElse(ResponseEntity.notFound().build());
}
```

200 OK é usado para requisições bem-sucedidas que retornam dados. 201 Created é usado quando um novo recurso é criado, idealmente incluindo o header Location com a URI do novo recurso. 204 No Content é usado para operações bem-sucedidas que não retornam dados.

400 Bad Request indica que a requisição está malformada ou contém dados inválidos. 401 Unauthorized indica que autenticação é necessária. 403 Forbidden indica que o usuário está autenticado mas não tem permissão para acessar o recurso.

404 Not Found indica que o recurso solicitado não existe. 409 Conflict indica que a requisição conflita com o estado atual do recurso. 422 Unprocessable Entity indica que a requisição está bem formada mas contém erros semânticos.

500 Internal Server Error indica erro no servidor. 502 Bad Gateway e 503 Service Unavailable indicam problemas de infraestrutura. Códigos 5xx devem ser evitados através de tratamento adequado de erros e validação de entrada.

### 6.4 Content Negotiation e Serialização

Content negotiation permite que clientes e servidores negociem o formato de dados mais apropriado para comunicação. O header Accept especifica quais tipos de mídia o cliente pode processar, enquanto Content-Type especifica o formato dos dados enviados.

```java
@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
    TaskDTO task = taskService.getTaskById(id);
    return ResponseEntity.ok(task);
}

@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
    TaskDTO createdTask = taskService.createTask(taskDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
}
```

Jackson é a biblioteca padrão para serialização JSON no Spring Boot, convertendo automaticamente objetos Java para JSON e vice-versa. Anotações como `@JsonProperty`, `@JsonIgnore` e `@JsonFormat` permitem customizar o processo de serialização.

```java
public class TaskDTO {
    @JsonProperty("task_id")
    private Long id;
    
    @JsonIgnore
    private String internalField;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskDate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
}
```

Configuração global de serialização pode ser realizada através de propriedades de aplicação ou beans de configuração. Isto permite controlar comportamentos como inclusão de propriedades null, formato de datas e naming strategy.

Versionamento de API pode ser implementado através de content negotiation usando headers customizados (Accept: application/vnd.api.v1+json) ou através de URLs (/api/v1/tasks). Cada abordagem tem vantagens e desvantagens em termos de simplicidade e flexibilidade.

### 6.5 Tratamento de Erros e Validação

Tratamento robusto de erros é essencial para criar APIs confiáveis e user-friendly. Erros devem ser comunicados de forma consistente e incluir informações suficientes para que clientes possam tomar ações apropriadas.

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = new ErrorResponse("Validation failed", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Resource not found", ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
```

Bean Validation (JSR-303) integra-se automaticamente com Spring MVC, validando objetos anotados com `@Valid`. Validações são aplicadas antes que métodos de controller sejam executados, garantindo que apenas dados válidos sejam processados.

```java
public class TaskDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Task date is required")
    @Future(message = "Task date must be in the future")
    private LocalDate taskDate;
}
```

Estruturas de erro padronizadas facilitam o processamento por clientes. RFC 7807 (Problem Details for HTTP APIs) define um formato padrão para comunicar erros em APIs HTTP, incluindo campos como type, title, status, detail e instance.

Logging apropriado é crucial para debugging e monitoramento. Erros 4xx geralmente indicam problemas do cliente e podem ser logados em nível INFO ou WARN, enquanto erros 5xx indicam problemas do servidor e devem ser logados em nível ERROR com stack traces completos.

### 6.6 Paginação e Filtros

Paginação é essencial para APIs que retornam grandes volumes de dados, melhorando performance e experiência do usuário. Spring Data fornece suporte nativo através da interface Pageable, que pode ser utilizada diretamente em métodos de controller.

```java
@GetMapping
public ResponseEntity<Page<TaskDTO>> getAllTasks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "taskDate") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {
    
    Sort sort = sortDir.equalsIgnoreCase("desc") ? 
        Sort.by(sortBy).descending() : 
        Sort.by(sortBy).ascending();
    
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<TaskDTO> tasks = taskService.getAllTasks(pageable);
    
    return ResponseEntity.ok(tasks);
}
```

Filtros permitem que clientes especifiquem critérios para reduzir o conjunto de dados retornado. Query parameters são a forma mais comum de implementar filtros, sendo fáceis de usar e cachear.

```java
@GetMapping("/search")
public ResponseEntity<List<TaskDTO>> searchTasks(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Priority priority,
        @RequestParam(required = false) Category category,
        @RequestParam(required = false) Boolean completed) {
    
    TaskSearchCriteria criteria = TaskSearchCriteria.builder()
            .title(title)
            .priority(priority)
            .category(category)
            .completed(completed)
            .build();
    
    List<TaskDTO> tasks = taskService.searchTasks(criteria);
    return ResponseEntity.ok(tasks);
}
```

Metadados de paginação devem ser incluídos na resposta para que clientes possam implementar navegação apropriada. Isto pode ser feito através de headers HTTP ou incluindo metadados no corpo da resposta.

Rate limiting protege APIs contra abuso e garante qualidade de serviço para todos os usuários. Pode ser implementado através de filtros customizados ou bibliotecas especializadas como Bucket4j, usando algoritmos como token bucket ou sliding window.

Caching pode ser implementado em múltiplas camadas para melhorar performance. Headers HTTP como Cache-Control e ETag permitem caching no cliente e proxies intermediários. Cache de aplicação pode ser implementado usando Spring Cache com providers como Redis ou Caffeine.


## 7. Banco de Dados e SQL Server

### 7.1 Fundamentos de Bancos Relacionais

Bancos de dados relacionais organizam dados em tabelas relacionadas através de chaves primárias e estrangeiras, seguindo o modelo relacional proposto por Edgar Codd. Este modelo garante integridade de dados através de constraints e normalização, proporcionando uma base sólida para aplicações empresariais que requerem consistência e confiabilidade.

O modelo relacional é baseado em conceitos matemáticos de teoria dos conjuntos e álgebra relacional. Tabelas representam relações, linhas representam tuplas, e colunas representam atributos. Relacionamentos entre tabelas são estabelecidos através de chaves estrangeiras que referenciam chaves primárias de outras tabelas.

```sql
-- Criação da tabela Users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Criação da tabela Tasks
CREATE TABLE tasks (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(1000),
    task_date DATE NOT NULL,
    completed BIT DEFAULT 0,
    priority NVARCHAR(20) NOT NULL,
    category NVARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    completed_at DATETIME2,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

Normalização é o processo de organizar dados para reduzir redundância e melhorar integridade. A primeira forma normal (1NF) elimina grupos repetitivos, a segunda forma normal (2NF) elimina dependências parciais, e a terceira forma normal (3NF) elimina dependências transitivas. O projeto utiliza 3NF para balancear normalização com performance.

ACID (Atomicity, Consistency, Isolation, Durability) são propriedades fundamentais que garantem confiabilidade de transações. Atomicidade garante que transações sejam tudo-ou-nada, Consistência mantém integridade de dados, Isolamento previne interferência entre transações concorrentes, e Durabilidade garante que dados commitados sejam permanentes.

### 7.2 SQL Server Características e Vantagens

SQL Server é um sistema de gerenciamento de banco de dados relacional desenvolvido pela Microsoft, amplamente utilizado em ambientes empresariais por sua robustez, performance e integração com o ecossistema Microsoft. O SQL Server oferece recursos avançados como particionamento, compressão, criptografia e alta disponibilidade.

O motor de banco de dados do SQL Server utiliza arquitetura baseada em páginas de 8KB, organizadas em extents de 64KB. Esta organização otimiza operações de I/O e permite implementação eficiente de índices B-tree. O buffer pool gerencia cache de páginas em memória, reduzindo acesso a disco e melhorando performance.

```sql
-- Índices para otimização de consultas
CREATE INDEX IX_tasks_user_id_task_date ON tasks (user_id, task_date);
CREATE INDEX IX_tasks_user_id_completed ON tasks (user_id, completed);
CREATE INDEX IX_users_email ON users (email);

-- Estatísticas para otimização de consultas
UPDATE STATISTICS tasks;
UPDATE STATISTICS users;
```

O Query Optimizer do SQL Server analisa consultas e gera planos de execução otimizados baseado em estatísticas de dados e índices disponíveis. O optimizer considera múltiplas estratégias de join, ordem de operações e uso de índices para encontrar o plano mais eficiente.

Tipos de dados do SQL Server são ricos e específicos, incluindo NVARCHAR para Unicode, DATETIME2 para timestamps de alta precisão, e IDENTITY para auto-incremento. A escolha apropriada de tipos de dados afeta tanto performance quanto armazenamento.

Transações no SQL Server suportam diferentes níveis de isolamento: READ UNCOMMITTED (permite dirty reads), READ COMMITTED (padrão, previne dirty reads), REPEATABLE READ (previne non-repeatable reads), e SERIALIZABLE (previne phantom reads). Cada nível oferece diferentes trade-offs entre consistência e performance.

### 7.3 Índices e Otimização de Performance

Índices são estruturas de dados que aceleram consultas criando caminhos rápidos para localizar dados. SQL Server utiliza principalmente índices B-tree, que mantêm dados ordenados e permitem busca, inserção e exclusão em tempo logarítmico. Índices clustered determinam a ordem física de armazenamento dos dados.

```sql
-- Índice clustered na chave primária (automático)
ALTER TABLE tasks ADD CONSTRAINT PK_tasks PRIMARY KEY CLUSTERED (id);

-- Índices non-clustered para consultas frequentes
CREATE NONCLUSTERED INDEX IX_tasks_user_date 
ON tasks (user_id, task_date) 
INCLUDE (title, completed, priority);

-- Índice para busca textual
CREATE NONCLUSTERED INDEX IX_tasks_title 
ON tasks (title) 
WHERE title IS NOT NULL;
```

Índices clustered reorganizam fisicamente os dados na tabela baseado na chave do índice. Cada tabela pode ter apenas um índice clustered, tipicamente na chave primária. Índices non-clustered criam estruturas separadas que apontam para as linhas de dados, permitindo múltiplos índices por tabela.

Índices compostos incluem múltiplas colunas e são eficazes quando consultas filtram por múltiplas colunas. A ordem das colunas no índice é importante: colunas mais seletivas devem vir primeiro. Colunas INCLUDE adicionam dados ao nível folha do índice sem afetar a ordenação.

Fragmentação de índices ocorre quando páginas não estão fisicamente contíguas ou quando há espaço livre excessivo. Fragmentação interna (dentro das páginas) e externa (entre páginas) afetam performance. Reorganização e reconstrução de índices são operações de manutenção necessárias.

```sql
-- Verificar fragmentação
SELECT 
    i.name AS IndexName,
    s.avg_fragmentation_in_percent,
    s.page_count
FROM sys.dm_db_index_physical_stats(DB_ID(), NULL, NULL, NULL, 'DETAILED') s
JOIN sys.indexes i ON s.object_id = i.object_id AND s.index_id = i.index_id
WHERE s.avg_fragmentation_in_percent > 10;

-- Reorganizar índices com fragmentação moderada
ALTER INDEX IX_tasks_user_date ON tasks REORGANIZE;

-- Reconstruir índices com fragmentação alta
ALTER INDEX IX_tasks_user_date ON tasks REBUILD;
```

Estatísticas são metadados sobre distribuição de dados que o Query Optimizer utiliza para estimar custos de operações. Estatísticas desatualizadas podem resultar em planos de execução subótimos. Auto-update statistics é habilitado por padrão, mas atualizações manuais podem ser necessárias para tabelas grandes.

### 7.4 Transações e Controle de Concorrência

Transações garantem que operações de banco de dados sejam executadas de forma atômica, consistente, isolada e durável. SQL Server implementa controle de concorrência através de locks e row versioning, permitindo que múltiplas transações executem simultaneamente sem comprometer integridade de dados.

```sql
-- Transação explícita com tratamento de erro
BEGIN TRANSACTION;
BEGIN TRY
    INSERT INTO users (name, email, password) 
    VALUES ('João Silva', 'joao@email.com', 'hashedPassword');
    
    DECLARE @userId BIGINT = SCOPE_IDENTITY();
    
    INSERT INTO tasks (title, description, task_date, user_id)
    VALUES ('Primeira tarefa', 'Descrição da tarefa', '2025-01-15', @userId);
    
    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    THROW;
END CATCH;
```

Níveis de isolamento controlam como transações interagem entre si. READ COMMITTED (padrão) utiliza shared locks para leitura e exclusive locks para escrita. SNAPSHOT isolation utiliza row versioning para fornecer leituras consistentes sem locks, reduzindo bloqueios mas aumentando uso de tempdb.

Deadlocks ocorrem quando duas ou mais transações esperam indefinidamente por recursos que estão sendo utilizados umas pelas outras. SQL Server detecta automaticamente deadlocks e termina uma das transações (deadlock victim). Prevenção inclui acessar recursos em ordem consistente e manter transações curtas.

```sql
-- Configurar timeout de lock
SET LOCK_TIMEOUT 5000; -- 5 segundos

-- Verificar locks ativos
SELECT 
    l.resource_type,
    l.resource_database_id,
    l.resource_associated_entity_id,
    l.request_mode,
    l.request_status,
    s.session_id,
    s.login_name
FROM sys.dm_tran_locks l
JOIN sys.dm_exec_sessions s ON l.request_session_id = s.session_id;
```

Row versioning permite que leituras vejam versões consistentes de dados sem bloquear escritas. Quando habilitado, modificações criam versões anteriores dos dados em tempdb. Isto melhora concorrência mas requer monitoramento de tempdb para evitar crescimento excessivo.

Hints de lock permitem controlar comportamento de locking em consultas específicas. NOLOCK permite dirty reads, HOLDLOCK mantém shared locks até o fim da transação, e UPDLOCK adquire update locks para prevenir deadlocks em padrões read-then-update.

### 7.5 Backup e Recuperação

Estratégias de backup são fundamentais para proteger dados contra falhas de hardware, corrupção, erros humanos e desastres. SQL Server oferece múltiplos tipos de backup: full (completo), differential (diferencial) e transaction log (log de transações), cada um com características específicas de tempo e espaço.

```sql
-- Backup completo
BACKUP DATABASE TaskManager 
TO DISK = 'C:\Backups\TaskManager_Full.bak'
WITH FORMAT, COMPRESSION, CHECKSUM;

-- Backup diferencial
BACKUP DATABASE TaskManager 
TO DISK = 'C:\Backups\TaskManager_Diff.bak'
WITH DIFFERENTIAL, COMPRESSION, CHECKSUM;

-- Backup de log de transações
BACKUP LOG TaskManager 
TO DISK = 'C:\Backups\TaskManager_Log.trn'
WITH COMPRESSION, CHECKSUM;
```

Recovery models determinam como logs de transações são gerenciados. SIMPLE recovery descarta logs automaticamente, permitindo apenas recuperação até o último backup. FULL recovery mantém todos os logs, permitindo point-in-time recovery. BULK_LOGGED é similar ao FULL mas otimiza operações em lote.

Point-in-time recovery permite restaurar banco de dados para um momento específico, útil para recuperar de erros humanos ou corrupção de dados. Requer backup completo, backups diferenciais e backups de log de transações em sequência.

```sql
-- Restauração point-in-time
RESTORE DATABASE TaskManager 
FROM DISK = 'C:\Backups\TaskManager_Full.bak'
WITH NORECOVERY, REPLACE;

RESTORE DATABASE TaskManager 
FROM DISK = 'C:\Backups\TaskManager_Diff.bak'
WITH NORECOVERY;

RESTORE LOG TaskManager 
FROM DISK = 'C:\Backups\TaskManager_Log.trn'
WITH STOPAT = '2025-01-15 14:30:00';
```

Verificação de integridade deve ser realizada regularmente para detectar corrupção de dados. DBCC CHECKDB verifica integridade física e lógica do banco de dados, incluindo páginas, índices e relacionamentos. Problemas detectados podem ser corrigidos automaticamente ou requerem restauração de backup.

Alta disponibilidade pode ser implementada através de Always On Availability Groups, Database Mirroring ou Log Shipping. Cada solução oferece diferentes níveis de proteção, performance e complexidade de configuração.

### 7.6 Segurança e Controle de Acesso

Segurança no SQL Server é implementada através de múltiplas camadas: autenticação (quem pode conectar), autorização (o que podem fazer), e auditoria (o que fizeram). Autenticação pode ser Windows Authentication (integrada) ou SQL Server Authentication (usuário/senha).

```sql
-- Criar login e usuário
CREATE LOGIN task_app_user WITH PASSWORD = 'StrongPassword123!';
USE TaskManager;
CREATE USER task_app_user FOR LOGIN task_app_user;

-- Criar role customizada
CREATE ROLE task_app_role;

-- Conceder permissões específicas
GRANT SELECT, INSERT, UPDATE, DELETE ON users TO task_app_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON tasks TO task_app_role;

-- Adicionar usuário à role
ALTER ROLE task_app_role ADD MEMBER task_app_user;
```

Princípio do menor privilégio deve ser aplicado, concedendo apenas permissões mínimas necessárias. Roles facilitam gerenciamento de permissões agrupando usuários com necessidades similares. Roles built-in como db_datareader e db_datawriter oferecem permissões predefinidas.

Criptografia protege dados em repouso e em trânsito. Transparent Data Encryption (TDE) criptografa automaticamente arquivos de banco de dados. Always Encrypted permite criptografia de colunas específicas com chaves gerenciadas pela aplicação.

```sql
-- Habilitar TDE
USE master;
CREATE MASTER KEY ENCRYPTION BY PASSWORD = 'MasterKeyPassword123!';
CREATE CERTIFICATE TDE_Cert WITH SUBJECT = 'TDE Certificate';
USE TaskManager;
CREATE DATABASE ENCRYPTION KEY WITH ALGORITHM = AES_256 
ENCRYPTION BY SERVER CERTIFICATE TDE_Cert;
ALTER DATABASE TaskManager SET ENCRYPTION ON;
```

Auditoria rastreia atividades no banco de dados para compliance e segurança. SQL Server Audit pode capturar logins, mudanças de schema, acesso a dados sensíveis e operações administrativas. Logs de auditoria podem ser armazenados em arquivos, Windows Event Log ou Azure Storage.

Row-Level Security (RLS) permite controlar acesso a linhas específicas baseado no contexto do usuário. Útil para aplicações multi-tenant onde usuários devem ver apenas seus próprios dados. Implementado através de security policies e predicate functions.


## 8. Azure e Cloud Computing

### 8.1 Fundamentos de Cloud Computing

Cloud Computing é um modelo de entrega de serviços de computação através da internet, oferecendo recursos como servidores, armazenamento, bancos de dados, redes, software e analytics sob demanda. O modelo cloud elimina a necessidade de investimento inicial em infraestrutura física e permite escalabilidade dinâmica baseada na demanda.

Os três modelos principais de serviço cloud são Infrastructure as a Service (IaaS), Platform as a Service (PaaS) e Software as a Service (SaaS). IaaS fornece recursos de computação virtualizados, PaaS oferece plataforma de desenvolvimento e deployment, e SaaS entrega aplicações completas via internet.

```yaml
# Exemplo de configuração Azure Resource Manager (ARM)
{
  "$schema": "https://schema.management.azure.com/schemas/2019-04-01/deploymentTemplate.json#",
  "contentVersion": "1.0.0.0",
  "parameters": {
    "sqlServerName": {
      "type": "string",
      "metadata": {
        "description": "Nome do SQL Server"
      }
    },
    "databaseName": {
      "type": "string",
      "defaultValue": "TaskManager",
      "metadata": {
        "description": "Nome do banco de dados"
      }
    }
  },
  "resources": [
    {
      "type": "Microsoft.Sql/servers",
      "apiVersion": "2021-02-01-preview",
      "name": "[parameters('sqlServerName')]",
      "location": "[resourceGroup().location]",
      "properties": {
        "administratorLogin": "sqladmin",
        "administratorLoginPassword": "[parameters('sqlAdminPassword')]"
      }
    }
  ]
}
```

Modelos de deployment incluem public cloud (recursos compartilhados), private cloud (recursos dedicados), hybrid cloud (combinação de public e private) e multi-cloud (múltiplos provedores). Cada modelo oferece diferentes níveis de controle, segurança e custo.

Vantagens do cloud computing incluem redução de custos operacionais, escalabilidade elástica, alta disponibilidade, disaster recovery automático, acesso global e inovação acelerada através de serviços gerenciados. Desvantagens incluem dependência de conectividade, preocupações de segurança e possível vendor lock-in.

### 8.2 Azure SQL Database

Azure SQL Database é um serviço de banco de dados relacional totalmente gerenciado baseado no SQL Server, oferecendo alta disponibilidade, backup automático, patching automático e escalabilidade dinâmica. O serviço elimina a necessidade de gerenciar infraestrutura de banco de dados.

```java
// Configuração de conexão para Azure SQL Database
spring.datasource.url=jdbc:sqlserver://taskmanager-server.database.windows.net:1433;database=TaskManager;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=${AZURE_SQL_USERNAME}
spring.datasource.password=${AZURE_SQL_PASSWORD}
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Configurações específicas para Azure
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

Modelos de compra incluem DTU (Database Transaction Unit) e vCore. DTU oferece pacotes predefinidos de CPU, memória e I/O, adequados para cargas de trabalho previsíveis. vCore oferece controle granular sobre recursos, adequado para cargas de trabalho com requisitos específicos.

Tiers de serviço incluem Basic (desenvolvimento e teste), Standard (aplicações de produção com requisitos moderados), Premium (aplicações críticas com alta performance) e Hyperscale (bancos de dados muito grandes com escalabilidade automática).

```sql
-- Configurações específicas para Azure SQL Database
-- Habilitar Query Store para monitoramento de performance
ALTER DATABASE TaskManager SET QUERY_STORE = ON;

-- Configurar automatic tuning
ALTER DATABASE TaskManager SET AUTOMATIC_TUNING (FORCE_LAST_GOOD_PLAN = ON);
ALTER DATABASE TaskManager SET AUTOMATIC_TUNING (CREATE_INDEX = ON);
ALTER DATABASE TaskManager SET AUTOMATIC_TUNING (DROP_INDEX = ON);
```

Backup automático é configurado por padrão com retenção de 7-35 dias para Basic/Standard e até 35 dias para Premium. Point-in-time restore permite recuperação para qualquer momento dentro do período de retenção. Geo-restore permite recuperação em diferentes regiões.

Segurança inclui firewall de IP, Virtual Network rules, Always Encrypted, Transparent Data Encryption (TDE) e Advanced Threat Protection. Azure Active Directory integration permite autenticação centralizada e single sign-on.

### 8.3 Azure App Service

Azure App Service é uma plataforma PaaS para hospedar aplicações web, APIs REST e backends móveis. Suporta múltiplas linguagens (Java, .NET, Python, Node.js) e frameworks, oferecendo deployment automático, escalabilidade automática e integração com DevOps.

```yaml
# azure-pipelines.yml para CI/CD
trigger:
- main

pool:
  vmImage: 'ubuntu-latest'

variables:
  buildConfiguration: 'Release'

stages:
- stage: Build
  jobs:
  - job: BuildBackend
    steps:
    - task: Maven@3
      inputs:
        mavenPomFile: 'backend/pom.xml'
        goals: 'clean package'
        options: '-DskipTests=true'
    
    - task: PublishBuildArtifacts@1
      inputs:
        pathToPublish: 'backend/target/*.jar'
        artifactName: 'backend-jar'

- stage: Deploy
  jobs:
  - job: DeployToAzure
    steps:
    - task: AzureWebApp@1
      inputs:
        azureSubscription: 'Azure-Connection'
        appType: 'webAppLinux'
        appName: 'taskmanager-backend'
        package: '$(Pipeline.Workspace)/backend-jar/*.jar'
```

Deployment slots permitem staging de aplicações antes de colocar em produção. Slots podem ser utilizados para blue-green deployment, A/B testing e rollback rápido. Swap de slots é uma operação atômica que minimiza downtime.

Auto-scaling permite que aplicações respondam automaticamente a mudanças na demanda. Regras podem ser baseadas em métricas como CPU, memória, comprimento de fila ou métricas customizadas. Scale-out adiciona instâncias, scale-up aumenta recursos por instância.

```json
// Configuração de auto-scaling
{
  "profiles": [
    {
      "name": "Default",
      "capacity": {
        "minimum": "1",
        "maximum": "10",
        "default": "2"
      },
      "rules": [
        {
          "metricTrigger": {
            "metricName": "CpuPercentage",
            "threshold": 70,
            "timeAggregation": "Average",
            "timeWindow": "PT5M"
          },
          "scaleAction": {
            "direction": "Increase",
            "type": "ChangeCount",
            "value": "1",
            "cooldown": "PT5M"
          }
        }
      ]
    }
  ]
}
```

Application Insights fornece monitoramento de performance de aplicações (APM), incluindo telemetria automática, distributed tracing, dependency tracking e alertas inteligentes. Integração é automática para aplicações Spring Boot através de Java agent.

### 8.4 Azure Active Directory

Azure Active Directory (Azure AD) é um serviço de identidade e acesso baseado em cloud que fornece autenticação, autorização e gerenciamento de identidades para aplicações cloud e on-premises. Suporta protocolos modernos como OAuth 2.0, OpenID Connect e SAML.

```java
// Configuração para integração com Azure AD
@Configuration
@EnableWebSecurity
public class AzureAdSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(azureAdUserService())
                )
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );
        
        return http.build();
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Converter claims do Azure AD para authorities do Spring Security
            return extractAuthorities(jwt);
        });
        return converter;
    }
}
```

Single Sign-On (SSO) permite que usuários acessem múltiplas aplicações com uma única autenticação. Azure AD suporta SSO para milhares de aplicações SaaS pré-integradas e aplicações customizadas através de protocolos padrão.

Multi-Factor Authentication (MFA) adiciona camada extra de segurança requerendo múltiplos fatores de autenticação. Fatores incluem algo que você sabe (senha), algo que você tem (telefone) e algo que você é (biometria).

Conditional Access permite aplicar políticas de acesso baseadas em contexto como localização, dispositivo, aplicação e risco de usuário. Políticas podem bloquear acesso, requerer MFA ou permitir acesso limitado baseado em condições específicas.

```json
// Exemplo de política de Conditional Access
{
  "displayName": "Require MFA for high-risk sign-ins",
  "state": "enabled",
  "conditions": {
    "signInRiskLevels": ["high"],
    "applications": {
      "includeApplications": ["All"]
    },
    "users": {
      "includeUsers": ["All"]
    }
  },
  "grantControls": {
    "operator": "OR",
    "builtInControls": ["mfa"]
  }
}
```

### 8.5 Monitoramento e Observabilidade

Azure Monitor é uma plataforma abrangente para coleta, análise e ação baseada em telemetria de aplicações e infraestrutura. Inclui métricas, logs, traces e alertas em uma única plataforma unificada.

```java
// Configuração de Application Insights
@Configuration
public class TelemetryConfig {
    
    @Bean
    public TelemetryClient telemetryClient() {
        TelemetryClient client = new TelemetryClient();
        client.getContext().getComponent().setVersion("1.0.0");
        return client;
    }
    
    @EventListener
    public void handleTaskCreated(TaskCreatedEvent event) {
        telemetryClient.trackEvent("TaskCreated", 
            Map.of("userId", event.getUserId().toString(),
                   "priority", event.getPriority().toString()));
    }
}
```

Log Analytics workspace centraliza logs de múltiplas fontes, permitindo consultas complexas usando Kusto Query Language (KQL). Logs podem ser correlacionados entre aplicações, infraestrutura e segurança para troubleshooting e análise de root cause.

```kql
// Exemplo de consulta KQL
requests
| where timestamp > ago(1h)
| where resultCode >= 400
| summarize count() by bin(timestamp, 5m), resultCode
| render timechart
```

Alertas podem ser configurados baseados em métricas, logs ou availability tests. Action groups definem como notificações são enviadas (email, SMS, webhook) e quais ações automáticas são executadas (auto-scaling, runbooks).

Distributed tracing rastreia requisições através de múltiplos serviços, fornecendo visibilidade end-to-end de performance e dependências. Application Map visualiza topologia de aplicações e identifica gargalos de performance.

### 8.6 Segurança e Compliance

Azure Security Center fornece postura de segurança unificada e proteção avançada contra ameaças para cargas de trabalho cloud e on-premises. Inclui secure score, recomendações de segurança e detecção de ameaças.

```json
// Configuração de Key Vault para secrets
{
  "type": "Microsoft.KeyVault/vaults",
  "apiVersion": "2021-04-01-preview",
  "name": "taskmanager-keyvault",
  "properties": {
    "sku": {
      "family": "A",
      "name": "standard"
    },
    "tenantId": "[subscription().tenantId]",
    "accessPolicies": [
      {
        "tenantId": "[subscription().tenantId]",
        "objectId": "[parameters('appServicePrincipalId')]",
        "permissions": {
          "secrets": ["get", "list"]
        }
      }
    ],
    "enabledForDeployment": false,
    "enabledForTemplateDeployment": false,
    "enabledForDiskEncryption": false
  }
}
```

Azure Key Vault gerencia secrets, chaves criptográficas e certificados de forma segura. Integração com aplicações permite acesso a secrets sem hardcoding em código ou arquivos de configuração. Managed Identity elimina necessidade de credenciais explícitas.

Network Security Groups (NSGs) controlam tráfego de rede através de regras de firewall. Virtual Networks (VNets) fornecem isolamento de rede e segmentação. Private Endpoints permitem acesso privado a serviços Azure sem exposição à internet pública.

Compliance frameworks incluem SOC, ISO 27001, HIPAA, GDPR e muitos outros. Azure Compliance Manager ajuda a avaliar e gerenciar compliance através de assessments automáticos e recomendações.

```yaml
# Configuração de rede segura
resources:
- type: Microsoft.Network/virtualNetworks
  properties:
    addressSpace:
      addressPrefixes:
      - "10.0.0.0/16"
    subnets:
    - name: "app-subnet"
      properties:
        addressPrefix: "10.0.1.0/24"
        networkSecurityGroup:
          id: "[resourceId('Microsoft.Network/networkSecurityGroups', 'app-nsg')]"
```

Azure Policy permite governança e compliance através de políticas que avaliam recursos e aplicam regras organizacionais. Blueprints combinam policies, role assignments e ARM templates para deployment consistente de ambientes compliant.


## 9. DevOps e Deployment

### 9.1 Continuous Integration e Continuous Deployment

CI/CD é uma prática fundamental de DevOps que automatiza integração de código, testes e deployment, reduzindo riscos e acelerando entrega de valor. Continuous Integration garante que mudanças de código sejam integradas frequentemente e testadas automaticamente, enquanto Continuous Deployment automatiza o processo de release para produção.

```yaml
# GitHub Actions workflow para CI/CD
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:13
        env:
          POSTGRES_PASSWORD: postgres
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
    - name: Run backend tests
      run: |
        cd backend
        mvn clean test
    
    - name: Set up Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
        cache: 'npm'
        cache-dependency-path: frontend/package-lock.json
    
    - name: Install frontend dependencies
      run: |
        cd frontend
        npm ci
    
    - name: Run frontend tests
      run: |
        cd frontend
        npm run test:ci
    
    - name: Build frontend
      run: |
        cd frontend
        npm run build:prod

  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Build and push Docker image
      run: |
        docker build -t taskmanager:${{ github.sha }} .
        docker tag taskmanager:${{ github.sha }} ${{ secrets.REGISTRY_URL }}/taskmanager:latest
        docker push ${{ secrets.REGISTRY_URL }}/taskmanager:latest
    
    - name: Deploy to Azure
      uses: azure/webapps-deploy@v2
      with:
        app-name: 'taskmanager-prod'
        publish-profile: ${{ secrets.AZURE_WEBAPP_PUBLISH_PROFILE }}
        images: '${{ secrets.REGISTRY_URL }}/taskmanager:latest'
```

Pipeline stages incluem build, test, security scan, package e deploy. Cada stage deve ser independente e falhar rapidamente se problemas forem detectados. Paralelização de jobs reduz tempo total de execução do pipeline.

Branching strategies como GitFlow ou GitHub Flow definem como código é organizado e integrado. Feature branches permitem desenvolvimento isolado, pull requests facilitam code review, e branch protection rules garantem que código seja testado antes de merge.

Automated testing é fundamental para CI/CD confiável. Testes unitários verificam componentes isolados, testes de integração verificam interação entre componentes, e testes end-to-end verificam funcionalidade completa. Coverage reports garantem que código crítico seja testado.

### 9.2 Containerização com Docker

Docker permite empacotar aplicações e suas dependências em containers leves e portáveis, garantindo consistência entre ambientes de desenvolvimento, teste e produção. Containers compartilham o kernel do host mas são isolados em termos de processos, rede e sistema de arquivos.

```dockerfile
# Dockerfile multi-stage para otimização
# Stage 1: Build da aplicação
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Build do frontend
FROM node:18-alpine AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci --only=production
COPY frontend/ .
RUN npm run build:prod

# Stage 3: Runtime image
FROM openjdk:17-jre-slim
WORKDIR /app

# Criar usuário não-root para segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copiar JAR da aplicação
COPY --from=build /app/target/*.jar app.jar

# Copiar arquivos estáticos do frontend
COPY --from=frontend-build /app/dist ./static

# Configurar propriedades
COPY backend/src/main/resources/application-docker.properties ./application.properties

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Multi-stage builds otimizam tamanho de imagens finais incluindo apenas artefatos necessários para runtime. Build tools e dependências de desenvolvimento são descartados, resultando em imagens menores e mais seguras.

Docker Compose orquestra múltiplos containers para desenvolvimento local, definindo serviços, redes e volumes em um arquivo YAML. Isto simplifica setup de ambiente e garante consistência entre desenvolvedores.

```yaml
# docker-compose.yml para desenvolvimento
version: '3.8'

services:
  database:
    image: mcr.microsoft.com/mssql/server:2019-latest
    environment:
      SA_PASSWORD: "YourStrong@Passw0rd"
      ACCEPT_EULA: "Y"
    ports:
      - "1433:1433"
    volumes:
      - sqlserver_data:/var/opt/mssql

  backend:
    build: 
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      DB_HOST: database
      DB_PORT: 1433
      DB_NAME: TaskManager
    depends_on:
      - database
    volumes:
      - ./logs:/app/logs

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile.dev
    ports:
      - "4200:4200"
    volumes:
      - ./frontend:/app
      - /app/node_modules
    environment:
      API_URL: http://localhost:8080

volumes:
  sqlserver_data:
```

Container registries armazenam e distribuem imagens Docker. Azure Container Registry (ACR) oferece registries privados com integração nativa ao Azure, incluindo vulnerability scanning e geo-replication.

### 9.3 Orquestração com Kubernetes

Kubernetes é uma plataforma de orquestração de containers que automatiza deployment, scaling e gerenciamento de aplicações containerizadas. Fornece abstrações como Pods, Services, Deployments e Ingress para gerenciar aplicações complexas.

```yaml
# Deployment para backend
apiVersion: apps/v1
kind: Deployment
metadata:
  name: taskmanager-backend
  labels:
    app: taskmanager-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: taskmanager-backend
  template:
    metadata:
      labels:
        app: taskmanager-backend
    spec:
      containers:
      - name: backend
        image: taskmanager:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "kubernetes"
        - name: DB_HOST
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: host
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5

---
# Service para expor backend
apiVersion: v1
kind: Service
metadata:
  name: taskmanager-backend-service
spec:
  selector:
    app: taskmanager-backend
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: ClusterIP

---
# Ingress para roteamento externo
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: taskmanager-ingress
  annotations:
    kubernetes.io/ingress.class: "nginx"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  tls:
  - hosts:
    - taskmanager.example.com
    secretName: taskmanager-tls
  rules:
  - host: taskmanager.example.com
    http:
      paths:
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: taskmanager-backend-service
            port:
              number: 80
      - path: /
        pathType: Prefix
        backend:
          service:
            name: taskmanager-frontend-service
            port:
              number: 80
```

Pods são a menor unidade deployável no Kubernetes, contendo um ou mais containers que compartilham rede e armazenamento. Deployments gerenciam ReplicaSets que garantem que o número desejado de Pods esteja executando.

Services fornecem descoberta de serviços e load balancing para Pods. ClusterIP expõe serviços internamente, NodePort expõe em portas específicas dos nodes, e LoadBalancer provisiona load balancers externos.

ConfigMaps e Secrets gerenciam configuração e dados sensíveis separadamente do código da aplicação. ConfigMaps armazenam dados não-confidenciais, enquanto Secrets armazenam dados sensíveis como senhas e tokens.

### 9.4 Infrastructure as Code

Infrastructure as Code (IaC) trata infraestrutura como código, permitindo versionamento, review e automação de mudanças de infraestrutura. Terraform e Azure Resource Manager (ARM) são ferramentas populares para IaC.

```hcl
# Terraform configuration para Azure
terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~>3.0"
    }
  }
}

provider "azurerm" {
  features {}
}

# Resource Group
resource "azurerm_resource_group" "main" {
  name     = "rg-taskmanager-${var.environment}"
  location = var.location
}

# SQL Server
resource "azurerm_mssql_server" "main" {
  name                         = "sql-taskmanager-${var.environment}"
  resource_group_name          = azurerm_resource_group.main.name
  location                     = azurerm_resource_group.main.location
  version                      = "12.0"
  administrator_login          = var.sql_admin_username
  administrator_login_password = var.sql_admin_password

  tags = {
    environment = var.environment
  }
}

# SQL Database
resource "azurerm_mssql_database" "main" {
  name           = "sqldb-taskmanager-${var.environment}"
  server_id      = azurerm_mssql_server.main.id
  collation      = "SQL_Latin1_General_CP1_CI_AS"
  license_type   = "LicenseIncluded"
  max_size_gb    = 4
  sku_name       = "S0"

  tags = {
    environment = var.environment
  }
}

# App Service Plan
resource "azurerm_service_plan" "main" {
  name                = "asp-taskmanager-${var.environment}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  os_type             = "Linux"
  sku_name            = "P1v2"
}

# App Service
resource "azurerm_linux_web_app" "main" {
  name                = "app-taskmanager-${var.environment}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_service_plan.main.location
  service_plan_id     = azurerm_service_plan.main.id

  site_config {
    application_stack {
      java_version = "17"
    }
  }

  app_settings = {
    "SPRING_PROFILES_ACTIVE" = var.environment
    "DB_HOST"                = azurerm_mssql_server.main.fully_qualified_domain_name
    "DB_NAME"                = azurerm_mssql_database.main.name
    "DB_USERNAME"            = var.sql_admin_username
    "DB_PASSWORD"            = "@Microsoft.KeyVault(SecretUri=${azurerm_key_vault_secret.db_password.id})"
  }

  identity {
    type = "SystemAssigned"
  }
}

# Key Vault
resource "azurerm_key_vault" "main" {
  name                = "kv-taskmanager-${var.environment}"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  tenant_id           = data.azurerm_client_config.current.tenant_id
  sku_name            = "standard"

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = azurerm_linux_web_app.main.identity[0].principal_id

    secret_permissions = [
      "Get",
    ]
  }
}

# Variables
variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

variable "location" {
  description = "Azure region"
  type        = string
  default     = "East US"
}

variable "sql_admin_username" {
  description = "SQL Server admin username"
  type        = string
  sensitive   = true
}

variable "sql_admin_password" {
  description = "SQL Server admin password"
  type        = string
  sensitive   = true
}
```

State management é crucial em Terraform para rastrear recursos criados e suas configurações. Remote state backends como Azure Storage ou Terraform Cloud permitem colaboração em equipe e state locking para prevenir modificações concorrentes.

Modules permitem reutilização de configurações Terraform, encapsulando recursos relacionados em componentes reutilizáveis. Modules podem ser versionados e compartilhados através de registries públicos ou privados.

### 9.5 Monitoramento e Observabilidade

Observabilidade é a capacidade de entender o estado interno de um sistema baseado em suas saídas externas. Os três pilares da observabilidade são métricas (dados numéricos agregados), logs (eventos discretos) e traces (jornada de requisições).

```java
// Configuração de métricas customizadas com Micrometer
@Component
public class TaskMetrics {
    private final Counter taskCreatedCounter;
    private final Timer taskProcessingTimer;
    private final Gauge activeTasksGauge;
    
    public TaskMetrics(MeterRegistry meterRegistry, TaskService taskService) {
        this.taskCreatedCounter = Counter.builder("tasks.created")
                .description("Number of tasks created")
                .tag("type", "business")
                .register(meterRegistry);
        
        this.taskProcessingTimer = Timer.builder("tasks.processing.time")
                .description("Time taken to process tasks")
                .register(meterRegistry);
        
        this.activeTasksGauge = Gauge.builder("tasks.active")
                .description("Number of active tasks")
                .register(meterRegistry, taskService, TaskService::getActiveTaskCount);
    }
    
    public void incrementTaskCreated() {
        taskCreatedCounter.increment();
    }
    
    public Timer.Sample startTaskProcessing() {
        return Timer.start(taskProcessingTimer);
    }
}
```

Structured logging facilita análise e correlação de logs através de formatos consistentes como JSON. Correlation IDs permitem rastrear requisições através de múltiplos serviços e componentes.

```java
// Configuração de logging estruturado
@Component
public class CorrelationIdFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}

// logback-spring.xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <mdc/>
                <message/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

Alerting deve ser baseado em SLIs (Service Level Indicators) e SLOs (Service Level Objectives). Alertas devem ser actionable, evitando alert fatigue através de thresholds apropriados e escalation policies.

Distributed tracing rastreia requisições através de múltiplos serviços, fornecendo visibilidade end-to-end de performance e dependências. OpenTelemetry é o padrão emergente para instrumentação de observabilidade.

### 9.6 Segurança em DevOps (DevSecOps)

DevSecOps integra práticas de segurança no pipeline de desenvolvimento, implementando "security as code" e "shift left security". Segurança deve ser considerada desde o design até produção, não como uma etapa final.

```yaml
# Security scanning no pipeline
security-scan:
  runs-on: ubuntu-latest
  steps:
  - uses: actions/checkout@v3
  
  # SAST - Static Application Security Testing
  - name: Run CodeQL Analysis
    uses: github/codeql-action/init@v2
    with:
      languages: java, javascript
  
  - name: Build application
    run: |
      cd backend && mvn compile
      cd frontend && npm run build
  
  - name: Perform CodeQL Analysis
    uses: github/codeql-action/analyze@v2
  
  # Dependency scanning
  - name: Run Snyk to check for vulnerabilities
    uses: snyk/actions/maven@master
    env:
      SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
    with:
      args: --severity-threshold=high
  
  # Container scanning
  - name: Build Docker image
    run: docker build -t taskmanager:test .
  
  - name: Run Trivy vulnerability scanner
    uses: aquasecurity/trivy-action@master
    with:
      image-ref: 'taskmanager:test'
      format: 'sarif'
      output: 'trivy-results.sarif'
  
  # Infrastructure scanning
  - name: Run Terraform security scan
    uses: bridgecrewio/checkov-action@master
    with:
      directory: ./terraform
      framework: terraform
```

Secret management deve utilizar ferramentas especializadas como Azure Key Vault, HashiCorp Vault ou AWS Secrets Manager. Secrets nunca devem ser commitados em código ou logs, e devem ter rotação automática quando possível.

Container security inclui scanning de vulnerabilidades, uso de imagens base mínimas, execução como usuário não-root e implementação de security policies através de ferramentas como OPA Gatekeeper.

Network security em Kubernetes inclui Network Policies para microsegmentação, Service Mesh para criptografia mTLS, e Ingress controllers com WAF para proteção de aplicações web.

Compliance as Code automatiza verificações de compliance através de políticas como código. Ferramentas como Open Policy Agent (OPA) permitem definir e aplicar políticas de segurança e compliance de forma declarativa.


## 10. Perguntas e Respostas Técnicas

### 10.1 Perguntas sobre Java e Spring Boot

**P: Explique a diferença entre @Component, @Service, @Repository e @Controller.**

R: Todas essas anotações são especializações de @Component e marcam classes para serem gerenciadas pelo Spring IoC container. @Service indica que a classe contém lógica de negócio, @Repository marca classes de acesso a dados (e habilita tradução automática de exceções), @Controller marca classes que lidam com requisições web. A diferença é principalmente semântica, mas ferramentas podem tratá-las de forma específica.

**P: Como funciona a injeção de dependência no Spring?**

R: O Spring utiliza Inversion of Control (IoC) onde o container gerencia criação e injeção de dependências. Pode ser feita via construtor (recomendado), setter ou field injection. O container analisa anotações como @Autowired, cria um grafo de dependências e injeta automaticamente. Constructor injection garante dependências obrigatórias e facilita testes.

**P: O que são Spring Profiles e como são utilizados?**

R: Profiles permitem configurar diferentes comportamentos para diferentes ambientes (dev, test, prod). Podem ser ativados via propriedades, variáveis de ambiente ou programaticamente. Permitem beans condicionais (@Profile), arquivos de propriedades específicos (application-{profile}.properties) e configurações customizadas por ambiente.

**P: Explique o ciclo de vida de um bean no Spring.**

R: 1) Instantiation - container cria instância; 2) Populate properties - injeta dependências; 3) BeanNameAware, BeanFactoryAware - callbacks de awareness; 4) @PostConstruct ou InitializingBean - inicialização customizada; 5) Bean ready for use; 6) @PreDestroy ou DisposableBean - cleanup antes de destruição.

**P: Como implementar transações no Spring?**

R: Usando @Transactional em métodos ou classes. Spring utiliza AOP para interceptar chamadas e gerenciar transações automaticamente. Suporta propagação (REQUIRED, REQUIRES_NEW, etc.), isolamento (READ_COMMITTED, REPEATABLE_READ, etc.), rollback rules e timeout. Para funcionar, métodos devem ser públicos e chamados externamente (não self-invocation).

### 10.2 Perguntas sobre JPA e Banco de Dados

**P: Explique o problema N+1 em JPA e como resolvê-lo.**

R: Ocorre quando uma consulta inicial retorna N registros, e para cada registro é executada uma consulta adicional para carregar relacionamentos lazy, resultando em 1+N consultas. Soluções: 1) Fetch joins (@Query com JOIN FETCH); 2) @EntityGraph; 3) Batch fetching; 4) Mudar para EAGER fetch (cuidado com performance); 5) Usar DTOs com projeções.

**P: Qual a diferença entre EAGER e LAZY loading?**

R: EAGER carrega dados relacionados imediatamente junto com a entidade principal. LAZY carrega apenas quando acessado pela primeira vez. LAZY é padrão para @OneToMany e @ManyToMany, EAGER para @OneToOne e @ManyToOne. LAZY melhora performance inicial mas pode causar LazyInitializationException se acessado fora do contexto de persistência.

**P: Como funcionam os níveis de isolamento de transação?**

R: READ_UNCOMMITTED permite dirty reads; READ_COMMITTED (padrão) previne dirty reads mas permite non-repeatable reads; REPEATABLE_READ previne non-repeatable reads mas permite phantom reads; SERIALIZABLE previne todos os problemas mas com maior impacto na performance. Cada nível oferece trade-off entre consistência e concorrência.

**P: Explique a diferença entre save() e saveAndFlush() no JPA.**

R: save() agenda a operação para ser executada quando a transação for commitada ou quando flush() for chamado explicitamente. saveAndFlush() executa imediatamente a operação no banco de dados e força sincronização. saveAndFlush() é útil quando você precisa do ID gerado imediatamente ou quer garantir que constraints sejam verificadas.

**P: Como otimizar consultas JPA?**

R: 1) Usar projeções/DTOs para carregar apenas dados necessários; 2) Fetch joins para evitar N+1; 3) Paginação para grandes datasets; 4) Índices apropriados no banco; 5) Query hints para controlar cache; 6) Batch processing para operações em lote; 7) @EntityGraph para controlar fetch strategy; 8) Native queries para consultas complexas.

### 10.3 Perguntas sobre Angular e Frontend

**P: Explique o ciclo de vida de componentes Angular.**

R: ngOnChanges (mudanças em @Input), ngOnInit (inicialização), ngDoCheck (change detection customizado), ngAfterContentInit/Checked (conteúdo projetado), ngAfterViewInit/Checked (view inicializada), ngOnDestroy (cleanup). OnInit é usado para inicialização, OnDestroy para cleanup (unsubscribe observables), OnChanges para reagir a mudanças de input.

**P: Qual a diferença entre Observables e Promises?**

R: Promises representam um único valor futuro, são eager (executam imediatamente) e não canceláveis. Observables podem emitir múltiplos valores, são lazy (executam apenas quando subscribed), canceláveis (unsubscribe) e composáveis com operators. Observables são mais poderosos para streams de dados e programação reativa.

**P: Como funciona Change Detection no Angular?**

R: Angular verifica se dados mudaram e atualiza a view correspondente. Por padrão, verifica toda a árvore de componentes em cada ciclo. OnPush strategy otimiza verificando apenas quando inputs mudam ou eventos são emitidos. Triggers incluem eventos DOM, HTTP requests, timers. Pode ser otimizado com OnPush, immutable data e async pipe.

**P: Explique a diferença entre Template-driven e Reactive Forms.**

R: Template-driven: lógica no template, two-way binding com ngModel, validação via atributos HTML, simples mas menos flexível. Reactive: lógica no componente, FormControl/FormGroup, validação programática, mais controle e testabilidade. Reactive forms são preferíveis para formulários complexos e aplicações enterprise.

**P: Como implementar lazy loading no Angular?**

R: Usando loadChildren nas rotas: `{ path: 'feature', loadChildren: () => import('./feature/feature.module').then(m => m.FeatureModule) }`. Com standalone components: `loadComponent: () => import('./feature.component').then(c => c.FeatureComponent)`. Reduz bundle inicial carregando código apenas quando necessário.

### 10.4 Perguntas sobre Segurança

**P: Como funciona JWT e quais suas vantagens/desvantagens?**

R: JWT é self-contained (contém todas as informações), stateless (não requer armazenamento no servidor), e pode ser verificado sem consultar banco de dados. Vantagens: escalabilidade, performance, cross-domain. Desvantagens: tamanho maior que session IDs, difícil revogação, dados expostos se não criptografados. Adequado para APIs stateless e microservices.

**P: Explique CORS e como configurá-lo.**

R: Cross-Origin Resource Sharing permite que recursos sejam acessados por domínios diferentes. Navegador envia preflight request (OPTIONS) para verificar permissões. Configuração inclui allowed origins, methods, headers. No Spring: @CrossOrigin ou CorsConfigurationSource global. Importante configurar adequadamente para segurança (não usar * em produção).

**P: Como prevenir ataques de SQL Injection?**

R: 1) Usar consultas parametrizadas/prepared statements; 2) Validar e sanitizar entrada; 3) Usar ORMs como JPA que escapam automaticamente; 4) Princípio do menor privilégio para usuários de banco; 5) Evitar concatenação de strings em consultas; 6) Input validation com whitelist; 7) Stored procedures quando apropriado.

**P: O que é CSRF e como preveni-lo?**

R: Cross-Site Request Forgery força usuários autenticados a executar ações não intencionais. Prevenção: 1) CSRF tokens únicos por sessão/request; 2) Verificar header Referer; 3) SameSite cookies; 4) Double submit cookies; 5) Custom headers para AJAX. Spring Security habilita proteção CSRF por padrão para formulários.

**P: Explique autenticação vs autorização.**

R: Autenticação verifica identidade ("quem você é") através de credenciais como usuário/senha, tokens, biometria. Autorização determina permissões ("o que você pode fazer") baseado em roles, policies, ACLs. Autenticação vem primeiro, autorização depois. JWT pode conter claims para ambos.

### 10.5 Perguntas sobre Arquitetura e Design

**P: Explique os princípios SOLID.**

R: S - Single Responsibility (uma classe, uma responsabilidade); O - Open/Closed (aberto para extensão, fechado para modificação); L - Liskov Substitution (subtipos devem ser substituíveis); I - Interface Segregation (interfaces específicas vs genéricas); D - Dependency Inversion (depender de abstrações, não implementações). Promovem código maintível, testável e extensível.

**P: Qual a diferença entre arquitetura monolítica e microservices?**

R: Monolítica: aplicação única, deployment conjunto, tecnologia uniforme, comunicação in-process, simples inicialmente. Microservices: serviços independentes, deployment separado, tecnologias diversas, comunicação via rede, complexidade operacional. Microservices oferecem escalabilidade e flexibilidade mas aumentam complexidade de desenvolvimento e operação.

**P: Como implementar cache em uma aplicação Spring Boot?**

R: 1) @EnableCaching na configuração; 2) @Cacheable para cache de métodos; 3) @CacheEvict para invalidação; 4) @CachePut para atualização; 5) Configurar cache provider (Caffeine, Redis, EhCache); 6) Definir TTL e políticas de eviction; 7) Cache de HTTP com headers apropriados; 8) Considerar cache distribuído para múltiplas instâncias.

**P: Explique o padrão Repository.**

R: Encapsula lógica de acesso a dados, fornecendo interface uniforme para diferentes fontes de dados. Benefícios: desacoplamento, testabilidade (mocks), centralização de queries, abstração de tecnologia de persistência. Spring Data implementa automaticamente baseado em convenções de nomenclatura ou @Query customizadas.

**P: Como lidar com transações distribuídas?**

R: Padrões: 1) Two-Phase Commit (2PC) - coordenador garante atomicidade mas pode bloquear; 2) Saga Pattern - transações compensatórias para rollback; 3) Event Sourcing - eventos imutáveis como fonte da verdade; 4) Eventual Consistency - aceitar inconsistência temporária. Evitar quando possível através de design que minimize necessidade.

### 10.6 Perguntas sobre Performance e Escalabilidade

**P: Como otimizar performance de uma aplicação Spring Boot?**

R: 1) Profiling para identificar gargalos; 2) Connection pooling adequado; 3) Cache em múltiplas camadas; 4) Lazy loading e paginação; 5) Async processing para operações longas; 6) Otimização de consultas SQL; 7) Compressão HTTP; 8) CDN para recursos estáticos; 9) JVM tuning; 10) Monitoramento com métricas.

**P: Explique estratégias de scaling horizontal vs vertical.**

R: Vertical (scale up): aumentar recursos (CPU, RAM) da mesma máquina, simples mas limitado. Horizontal (scale out): adicionar mais máquinas, complexo mas ilimitado teoricamente. Aplicações stateless facilitam scaling horizontal. Load balancers distribuem carga. Auto-scaling ajusta recursos baseado em demanda.

**P: Como implementar rate limiting?**

R: Algoritmos: 1) Token Bucket - tokens gerados em taxa fixa; 2) Leaky Bucket - requisições processadas em taxa constante; 3) Fixed Window - contador por janela de tempo; 4) Sliding Window - janela deslizante mais precisa. Implementação: filtros customizados, Redis para estado distribuído, bibliotecas como Bucket4j.

**P: Qual a importância de monitoring e observabilidade?**

R: Monitoring detecta problemas, observabilidade entende por que ocorreram. Três pilares: métricas (agregações numéricas), logs (eventos discretos), traces (jornada de requisições). Ferramentas: Prometheus/Grafana, ELK Stack, Jaeger/Zipkin. SLIs/SLOs definem objetivos de qualidade. Alertas devem ser actionable.

**P: Como implementar circuit breaker pattern?**

R: Previne cascata de falhas em sistemas distribuídos. Estados: Closed (normal), Open (falhas detectadas, requisições rejeitadas), Half-Open (teste de recuperação). Implementação com Hystrix, Resilience4j ou Spring Cloud Circuit Breaker. Configurar thresholds, timeouts e fallbacks apropriados.

### 10.7 Perguntas sobre DevOps e Cloud

**P: Explique CI/CD e seus benefícios.**

R: Continuous Integration integra código frequentemente com testes automáticos. Continuous Deployment automatiza release para produção. Benefícios: detecção precoce de problemas, releases mais frequentes e confiáveis, redução de riscos, feedback rápido, maior produtividade. Pipeline típico: build, test, security scan, deploy.

**P: Qual a diferença entre Docker containers e VMs?**

R: VMs virtualizam hardware completo com OS separado, maior overhead mas isolamento total. Containers compartilham kernel do host, menor overhead, startup mais rápido, maior densidade. Containers são adequados para aplicações cloud-native, VMs para isolamento completo ou sistemas legados.

**P: Como implementar blue-green deployment?**

R: Duas versões idênticas do ambiente (blue/green), apenas uma ativa. Deploy na versão inativa, testes, switch de tráfego instantâneo. Rollback rápido se problemas. Requer load balancer configurável e duplicação de recursos. Variação: canary deployment (tráfego gradual para nova versão).

**P: Explique Infrastructure as Code.**

R: Tratar infraestrutura como código versionável e automatizável. Ferramentas: Terraform, ARM templates, CloudFormation. Benefícios: consistência, versionamento, review process, automação, disaster recovery. Permite criar/destruir ambientes rapidamente e garantir paridade entre ambientes.

**P: Como garantir segurança em pipelines CI/CD?**

R: 1) Secret management com ferramentas dedicadas; 2) SAST/DAST scanning automático; 3) Dependency vulnerability scanning; 4) Container image scanning; 5) Infrastructure security scanning; 6) Least privilege access; 7) Audit logs; 8) Signed commits e artifacts; 9) Environment isolation; 10) Security gates no pipeline.

---

## Conclusão

Este relatório técnico fornece uma base sólida para preparação de entrevistas técnicas, cobrindo conceitos fundamentais e práticos das tecnologias utilizadas no projeto. As perguntas e respostas simulam cenários reais de entrevista, permitindo prática e aprofundamento dos conhecimentos.

A combinação de conhecimento teórico com experiência prática demonstrada através do projeto de gerenciamento de tarefas oferece uma preparação abrangente para posições de desenvolvimento Java/Angular. O foco em explicações conceituais e justificativas técnicas prepara para discussões aprofundadas sobre arquitetura, design e implementação.

Recomenda-se revisar regularmente este material, praticar implementações dos conceitos apresentados e manter-se atualizado com as últimas versões e funcionalidades das tecnologias abordadas. A preparação contínua e a prática são fundamentais para sucesso em entrevistas técnicas e desenvolvimento profissional.

---

**Autor:** Manus AI  
**Data:** Janeiro 2025  
**Versão:** 1.0

