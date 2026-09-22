#  Biblioteca - Sistema de Gerenciamento de Empréstimos

Um sistema de gerenciamento de biblioteca em **Java**, demonstrando princípios sólidos de Programação Orientada a Objetos, arquitetura em camadas, persistência em banco de dados e boas práticas de desenvolvimento.

##  Objetivo

Este projeto foi desenvolvido como portfólio para candidaturas a vagas de **Desenvolvedor Backend Júnior em Java**. Implementa um sistema funcional de empréstimo e devolução de livros, com foco em **código limpo, testes automatizados, persistência em BD e decisões arquiteturais conscientes**.

---

##  Arquitetura

O projeto segue o padrão de **arquitetura em camadas** (layered architecture), separando responsabilidades de forma clara:

```
┌─────────────────────────────────────────┐
│            Main (Console)               │  ← Interface com usuário
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│       EmprestimoService                 │  ← Regras de negócio
│   (Orquestra operações, valida)         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Repositories (LivroRepository, etc)    │  ← Acesso a dados (JPA)
│   (CRUD, busca, persistência em BD)     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Model (@Entity Livro, Usuario)     │  ← Objetos de domínio
│   (Validação, encapsulamento, mapeado) │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        PostgreSQL Database              │  ← Persistência
└─────────────────────────────────────────┘
```

### Pacotes

- **`model/`** → Classes de domínio com `@Entity` (Livro, Usuario, Emprestimo)
- **`repository/`** → Interfaces que estendem `JpaRepository` (acesso a dados JPA)
- **`service/`** → Lógica de negócio (EmprestimoService)
- **`exception/`** → Exceções customizadas (BibliotecaException e filhas)

---

##  Decisões de Design

### 1. **Spring Data JPA com PostgreSQL**
- Repositories estendem `JpaRepository<T, ID>` para CRUD automático
- `@Entity` mapeia classes para tabelas no banco
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` para IDs auto-incrementados
- Método `orElseThrow()` em `Optional` para tratamento seguro de "não encontrado"

### 2. **Encapsulamento e Validação**
- Atributos `private` com `final` quando imutáveis (ex: `id` em `Livro`)
- Validação centralizada em métodos `private` nos construtores e setters
- Impede estados inválidos desde a criação do objeto

### 3. **Hierarquia de Exceções Customizadas**
- `BibliotecaException` (classe base `abstract`)
    - `LivroNaoEncontradoException`
    - `UsuarioNaoEncontradoException`
    - `LivroIndisponivelException`
    - `EmprestimoNaoEncontradoException`

**Benefício:** permite capturar "todas as exceções da biblioteca" em um único `catch`, ou tratar casos específicos conforme necessário.

### 4. **Service Orquestra, Não Cria**
- `EmprestimoService` valida pré-requisitos antes de criar `Emprestimo`
- Não cria usuários/livros automaticamente — rejeita se não existirem
- Mantém responsabilidades claras entre camadas
- Chama `.save()` nos repositories para persistir mudanças

### 5. **Uso de `Optional`**
- Métodos de busca (`findById()`) retornam `Optional<T>` em vez de `null`
- Força quem usa a lidar explicitamente com "não encontrado"
- Evita `NullPointerException` silenciosos
- Padrão: `.orElseThrow(() -> new CustomException())`

### 6. **Dados Calculados vs. Armazenados**
- `dataPrevistaDevolucao` é **calculada** (via `getDataPrevistaDevolucao()`) usando `dataPedido + tempoEmprestimoDias`
- `dataDevolucaoReal` é **armazenada** (muda com o tempo)
- Evita redundância e inconsistência de dados

### 7. **Transações Implícitas no JPA**
- `.save()` e `.delete()` gerenciam automaticamente as mudanças
- Não precisa de controle manual de conexões/commits (a menos que use transações explícitas)

---

##  Funcionalidades

### Menu Principal

```
1. Cadastrar Livro          → Adiciona novo livro ao acervo (persiste em BD)
2. Cadastrar Usuário        → Registra novo usuário (persiste em BD)
3. Emprestar Livro          → Cria empréstimo (valida disponibilidade)
4. Devolver Livro           → Registra devolução
5. Listar Livros            → Exibe todos os livros + status (do BD)
6. Listar Usuários          → Exibe todos os usuários (do BD)
7. Listar Empréstimos       → Exibe histórico de empréstimos (do BD)
8. Sair                     → Encerra o programa
```

### Validações Implementadas

**Ao emprestar um livro:**
-  Usuário deve existir no BD (senão: `UsuarioNaoEncontradoException`)
-  Livro deve existir no BD (senão: `LivroNaoEncontradoException`)
-  Livro deve estar disponível (senão: `LivroIndisponivelException`)
-  Tempo de empréstimo deve ser > 0 (validação no model)

**Ao devolver:**
-  Empréstimo deve existir no BD (senão: `EmprestimoNaoEncontradoException`)
-  Livro volta a ficar disponível
-  Data de devolução é registrada

### Tratamento de Erros

- Validação de entrada do usuário (letras quando esperado número)
- Limpeza de buffer do Scanner (`nextLine()`) após erros
- Mensagens de erro amigáveis (sem stacktrace) no console
- Retorno automático ao menu após erros

---

##  Testes Automatizados

Implementados 6 testes com **JUnit 5**, cobrindo cenários críticos:

```java
1. testEmpresarComSucesso()
    Livro fica indisponível após empréstimo, persistido em BD
   
2. testEmpresarUsuarioNaoExiste()
    Lança UsuarioNaoEncontradoException
   
3. testEmpresarLivroNaoExiste()
    Lança LivroNaoEncontradoException
   
4. testEmpresarLivroIndisponivel()
    Lança LivroIndisponivelException
   
5. testDevolverComSucesso()
    Livro fica disponível novamente, data registrada, persistido em BD
   
6. testDevolverEmprestimoNaoExiste()
    Lança EmprestimoNaoEncontradoException
```

**Padrão usado:** Arrange-Act-Assert (AAA)
- **Arrange:** prepara dados (cria repositories, adiciona livros/usuários)
- **Act:** executa a ação (`emprestar()`, `devolver()`)
- **Assert:** valida resultado (exceção lançada, estado mudou no BD)

---

## 🛠️ Como Usar

### Pré-requisitos

- **Java 21** (ou superior)
- **PostgreSQL** instalado e rodando
- **Maven** (para build e testes)

### 1. Configurar Banco de Dados

```bash
# Conecte ao PostgreSQL
psql -U postgres

# Crie a database
CREATE DATABASE biblioteca;
```

### 2. Configurar `application.properties`

Crie `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**Opções de `ddl-auto`:**
- `create-drop` → Recria tabelas a cada execução (desenvolvimento)
- `create` → Cria tabelas (sem dropar)
- `update` → Altera schema conforme necessário
- `validate` → Apenas valida (produção)

### 3. Compilar e Rodar

```bash
# Com Maven
mvn clean install
mvn spring-boot:run

# Ou gere o JAR e rode
mvn package
java -jar target/biblioteca-1.0.jar
```

### 4. Rodar Testes

```bash
mvn test
```

---

##  Tecnologias

- **Java 21** — Linguagem principal
- **Spring Data JPA** — ORM (Object-Relational Mapping)
- **Hibernate** — Provider JPA (gerencia entidades e persistência)
- **PostgreSQL 14+** — Banco de dados relacional
- **JUnit 5** — Framework de testes unitários
- **Maven** — Build tool
- **LocalDate** — API moderna de datas (java.time)

---

##  Conceitos Demonstrados

### Programação Orientada a Objetos
-  Encapsulamento (`private`, `final`, getters/setters)
-  Herança (exceções customizadas estendem `BibliotecaException`)
-  Abstração (`abstract class`, `interface` JpaRepository)
-  Polimorfismo (tratamento de múltiplas exceções)

### Arquitetura e Design
-  Padrão Repository (abstração de acesso a dados via JPA)
-  Padrão Service (orquestra lógica de negócio)
-  Separação de responsabilidades (camadas)
-  Dependency Injection (repositories injetados via construtor)

### JPA e Persistência
-  `@Entity`, `@Id`, `@GeneratedValue` para mapeamento
-  `JpaRepository` para CRUD automático
-  `Optional<T>` para acesso seguro a dados
-  Transações implícitas em operações `.save()` e `.delete()`

### Boas Práticas
-  Tratamento de exceções customizadas
-  Validação de entrada (model + service)
-  Código limpo e legível
-  Métodos reutilizáveis
-  Testes automatizados com padrão AAA

---

##  Evolução do Projeto

### v1 (Inicial - Setembro 2026)
- Java puro com HashMap em memória
- Sem persistência em BD
- Foco em fundamentos OOP e arquitetura

### v2 (Atual - Setembro 2026)
- Migração para **Spring Data JPA**
- **PostgreSQL** como persistência real
- Entidades mapeadas com `@Entity`
- Repositories via `JpaRepository`

### v3 (Futuro)
- Spring Boot com `@RestController` (API REST)
- Validação com `@Valid` e Bean Validation
- Integração com Spring Security (autenticação)

---

##  Próximos Passos

Este projeto foi desenvolvido como base sólida antes de escalar para **Spring Boot full**, onde conceitos como:
- `@RestController` → expõe operações via API REST
- `@Service` com `@Transactional` → gerencia transações explícitas
- Spring Validation (`@Valid`, `@NotNull`) → validação automática
- Spring Security → autenticação e autorização

...já serão familiares e compreensíveis.

---

##  Estrutura de Arquivos

```
biblioteca/
├── src/
│   ├── main/java/
│   │   ├── model/
│   │   │   ├── Livro.java
│   │   │   ├── Usuario.java
│   │   │   └── Emprestimo.java
│   │   ├── repository/
│   │   │   ├── LivroRepository.java
│   │   │   ├── UsuarioRepository.java
│   │   │   └── EmprestimoRepository.java
│   │   ├── service/
│   │   │   └── EmprestimoService.java
│   │   ├── exception/
│   │   │   ├── BibliotecaException.java
│   │   │   ├── LivroNaoEncontradoException.java
│   │   │   ├── UsuarioNaoEncontradoException.java
│   │   │   ├── LivroIndisponivelException.java
│   │   │   └── EmprestimoNaoEncontradoException.java
│   │   └── Main.java
│   ├── resources/
│   │   └── application.properties
│   └── test/java/
│       └── service/
│           └── EmprestimoServiceTest.java
├── pom.xml
└── README.md
```

---

##  Notas de Desenvolvimento

### Por que `Optional` em vez de `null`?
Com JPA, `findById()` retorna `Optional`. Força explicitamente a lidar com "não encontrado", evitando `NullPointerException`.

```java
//  Seguro
Livro livro = livroRepository.findById(idLivro)
    .orElseThrow(() -> new LivroNaoEncontradoException(idLivro));

//  Arriscado (antigo)
Livro livro = livroRepository.get(idLivro); // pode ser null
if (livro == null) { ... } // cód desnecessário
```

### Por que `@Entity` em vez de HashMap?
- **Consistência:** dados são persistidos mesmo após fechar a aplicação
- **Escalabilidade:** suporta mais dados (BD vs. memória RAM)
- **Queries:** `JpaRepository.findByAutor()` sem implementar buscas manuais

### Por que Spring Data JPA e não JDBC puro?
- JDBC puro é didático, mas repetitivo (boilerplate)
- JPA abstrai detalhes de conexões, mas você ainda entende o "por quê"
- Transição natural para Spring Boot depois

### Por que PostgreSQL?
- Mais robusto que SQLite para projetos em crescimento
- Suporte melhor a Java/JDBC
- Alinhado com stack para candidaturas em empresas

---

##  Troubleshooting

**Erro: `SQLException: FATAL: database "biblioteca" does not exist`**
→ Crie a database: `CREATE DATABASE biblioteca;`

**Erro: `org.hibernate.exception.JDBCConnectionException`**
→ Verifique se PostgreSQL está rodando e `application.properties` está correto

**Erro: `ValidationException: HV000149`**
→ Faltam dependências de validation. Adicione ao `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

##  Autor

Guilherme Machado  
[LinkedIn](https://linkedin.com/in/guilherme-bmachado) | [GitHub](https://github.com/devbianchi) | [Portfolio](https://guilherme-bmachado.vercel.app)

---

**Última atualização:** Setembro 2026
