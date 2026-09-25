# Biblioteca - Sistema de Gerenciamento de Empréstimos

Um sistema de gerenciamento de biblioteca construído com **Spring Boot** e **Spring Data JPA**, demonstrando princípios sólidos de Programação Orientada a Objetos, arquitetura em camadas, persistência em banco de dados relacional e boas práticas de desenvolvimento.

## Objetivo

Este projeto foi desenvolvido como portfólio para candidaturas a vagas de **Desenvolvedor Backend Júnior em Java**. Implementa um sistema funcional de empréstimo e devolução de livros, com foco em **código limpo, testes automatizados, persistência real em banco de dados e decisões arquiteturais conscientes**.

---

## Arquitetura

O projeto segue o padrão de **arquitetura em camadas** (layered architecture), agora gerenciada pelo container de injeção de dependência do Spring:

```
┌─────────────────────────────────────────┐
│      Main (Spring Boot + Console)       │  ← Interface com usuário
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│       EmprestimoService                 │  ← Regras de negócio (@Service)
│   (Orquestra operações, valida)         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Repositories (extends JpaRepository)   │  ← Acesso a dados (@Repository)
│   (CRUD automático, busca, persistência)│
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Model (@Entity Livro, Usuario)     │  ← Objetos de domínio mapeados
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        PostgreSQL Database              │  ← Persistência real
└─────────────────────────────────────────┘
```

### Pacotes

- **`model/`** → Classes de domínio com `@Entity` (Livro, Usuario, Emprestimo)
- **`repository/`** → Interfaces que estendem `JpaRepository` (CRUD automático via Spring Data JPA)
- **`service/`** → Lógica de negócio (`EmprestimoService`, anotado com `@Service`)
- **`exception/`** → Exceções customizadas (`BibliotecaException` e filhas)
- **`util/`** → Utilitários de apoio (`ConsoleUtils` — leitura segura de input no console)

---

## Decisões de Design

### 1. **Spring Boot + Spring Data JPA**
- O projeto roda como aplicação Spring Boot (`@SpringBootApplication`), com o container gerenciando o ciclo de vida dos beans (`Repository`, `Service`)
- Repositories estendem `JpaRepository<T, ID>`, eliminando a necessidade de implementar CRUD manualmente (como era feito na versão em memória)
- `@Entity` mapeia as classes de domínio para tabelas no PostgreSQL
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` — o **ID agora é gerado automaticamente pelo banco**, em vez do contador manual (`gerarProximoId()`) usado na versão anterior em memória

### 2. **Injeção de Dependência**
- `EmprestimoService` recebe os repositories via **injeção de dependência do Spring** (construtor), em vez de serem instanciados manualmente como na versão em Java puro
- O Spring resolve e gerencia essas dependências automaticamente, sem precisar de `new` espalhado pelo código

### 3. **Encapsulamento e Validação**
- Atributos `private`, com validação centralizada em métodos `private` nos construtores e setters
- Impede estados inválidos desde a criação do objeto, mesmo com o JPA gerenciando a persistência

### 4. **Hierarquia de Exceções Customizadas**
- `BibliotecaException` (classe base `abstract`)
  - `LivroNaoEncontradoException`
  - `UsuarioNaoEncontradoException`
  - `LivroIndisponivelException`
  - `EmprestimoNaoEncontradoException`

**Benefício:** permite capturar "todas as exceções da biblioteca" em um único `catch`, ou tratar casos específicos conforme necessário — usado tanto na camada de serviço quanto no menu de console.

### 5. **Service Orquestra, Não Cria**
- `EmprestimoService` valida pré-requisitos antes de criar um `Emprestimo`
- Não cria usuários/livros automaticamente — rejeita a operação se não existirem
- Persiste mudanças chamando `.save()` nos repositories (o JPA cuida do `INSERT`/`UPDATE`)

### 6. **Uso de `Optional` com `orElseThrow`**
- Métodos de busca (`findById()`, herdado de `JpaRepository`) retornam `Optional<T>` em vez de `null`
- Padrão adotado: `.orElseThrow(() -> new LivroNaoEncontradoException(id))` — busca e validação em uma linha só, sem `null` explícito em lugar nenhum do código

### 7. **Dados Calculados vs. Armazenados**
- `dataPrevistaDevolucao` é **calculada** (via `getDataPrevistaDevolucao()`), usando `dataPedido + tempoEmprestimoDias`
- `dataDevolucaoReal` é **armazenada** (muda com o tempo, e só é preenchida na devolução)
- Evita redundância e inconsistência entre campos

### 8. **`ConsoleUtils` — Leitura de Input Isolada**
- Métodos de leitura de input (número inteiro com validação, texto) foram extraídos da classe `Main` para uma classe utilitária dedicada
- Centraliza o tratamento de `InputMismatchException` (usuário digitando letra em campo numérico) em um único lugar, evitando duplicação de `try/catch` em cada opção do menu

---

## Funcionalidades

### Menu Principal

```
1. Cadastrar Livro          → Adiciona novo livro (persiste no PostgreSQL)
2. Cadastrar Usuário        → Registra novo usuário (persiste no PostgreSQL)
3. Emprestar Livro          → Cria empréstimo (valida disponibilidade)
4. Devolver Livro           → Registra devolução
5. Listar Livros            → Exibe todos os livros + status (direto do banco)
6. Listar Usuários          → Exibe todos os usuários
7. Listar Empréstimos       → Exibe histórico de empréstimos
8. Sair                     → Encerra o programa
```

### Validações Implementadas

**Ao emprestar um livro:**
-  Usuário deve existir no banco (senão: `UsuarioNaoEncontradoException`)
-  Livro deve existir no banco (senão: `LivroNaoEncontradoException`)
-  Livro deve estar disponível (senão: `LivroIndisponivelException`)
-  Tempo de empréstimo deve ser > 0 (validação no model)

**Ao devolver:**
-  Empréstimo deve existir no banco (senão: `EmprestimoNaoEncontradoException`)
-  Livro volta a ficar disponível
-  Data real de devolução é registrada

### Tratamento de Erros

- Validação de entrada do usuário via `ConsoleUtils` (letras quando esperado número)
- Mensagens de erro amigáveis (sem stacktrace) no console
- Retorno automático ao menu após erros

---

## Testes Automatizados

Implementados 6 testes com **JUnit 5**, cobrindo cenários críticos da camada de serviço:

```java
1. testEmpresarComSucesso()
    Livro fica indisponível após empréstimo

2. testEmpresarUsuarioNaoExiste()
    Lança UsuarioNaoEncontradoException

3. testEmpresarLivroNaoExiste()
    Lança LivroNaoEncontradoException

4. testEmpresarLivroIndisponivel()
    Lança LivroIndisponivelException

5. testDevolverComSucesso()
    Livro fica disponível novamente, data registrada

6. testDevolverEmprestimoNaoExiste()
    Lança EmprestimoNaoEncontradoException
```

**Padrão usado:** Arrange-Act-Assert (AAA)
- **Arrange:** prepara dados (cria/injeta repositories, adiciona livros/usuários)
- **Act:** executa a ação (`emprestar()`, `devolver()`)
- **Assert:** valida resultado (exceção lançada, estado mudou)

---

## Como Usar

### Pré-requisitos

- **Java 21** (ou superior)
- **PostgreSQL** instalado e rodando
- **Maven**

### 1. Configurar Banco de Dados

```bash
psql -U postgres
CREATE DATABASE biblioteca;
```

### 2. Configurar `application.properties`

Em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Compilar e Rodar

```bash
mvn clean install
mvn spring-boot:run
```

### 4. Rodar Testes

```bash
mvn test
```

---

## Tecnologias

- **Java 21** — Linguagem principal
- **Spring Boot** — Framework principal, injeção de dependência
- **Spring Data JPA** — ORM (Object-Relational Mapping)
- **Hibernate** — Provider JPA (gerencia entidades e persistência)
- **PostgreSQL** — Banco de dados relacional
- **JUnit 5** — Framework de testes
- **Maven** — Build tool
- **LocalDate** — API moderna de datas (java.time)

---

## Conceitos Demonstrados

### Programação Orientada a Objetos
-  Encapsulamento (`private`, getters/setters, validação)
-  Herança (exceções customizadas estendem `BibliotecaException`)
-  Abstração (`abstract class`, interfaces `JpaRepository`)
-  Polimorfismo (tratamento de múltiplas exceções)

### Spring Boot e Persistência
-  `@SpringBootApplication`, `@Entity`, `@Service`, `@Repository`
-  Injeção de dependência via construtor
-  `JpaRepository` para CRUD automático
-  Geração automática de ID (`@GeneratedValue`)
-  `Optional<T>` + `orElseThrow()` para acesso seguro a dados

### Boas Práticas
-  Tratamento de exceções customizadas
-  Validação de entrada (model + service)
-  Utilitários reutilizáveis (`ConsoleUtils`) em vez de código duplicado
-  Código limpo e legível
-  Testes automatizados com padrão AAA

---

## Evolução do Projeto

### v1 — Fundamentos (Java puro)
- HashMap em memória, sem persistência real
- Foco em modelagem OOP, arquitetura em camadas e testes, sem depender de framework

### v2 — Atual (Spring Boot + JPA + PostgreSQL)
- Migração para Spring Data JPA, com persistência real em PostgreSQL
- ID gerado automaticamente pelo banco
- Injeção de dependência via Spring
- Leitura de console isolada em `ConsoleUtils`

### v3 — Próximos passos
- Exposição via API REST (`@RestController`)
- Validação automática com Bean Validation (`@Valid`, `@NotNull`)
- Autenticação com Spring Security

---

## Estrutura de Arquivos

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
│   │   ├── util/
│   │   │   └── ConsoleUtils.java
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

## Notas de Desenvolvimento

### Por que migrar de HashMap para JPA/PostgreSQL?
A versão em memória (v1) foi essencial para aprender os fundamentos sem a complexidade de um framework escondendo os detalhes. Migrar para JPA depois de já entender o "por quê" de cada camada tornou a transição natural, em vez de mágica.

### Por que `Optional` + `orElseThrow` em vez de `if/null`?
```java
// Antes (v1, HashMap)
Livro livro = livroRepository.buscarPorId(idLivro).get();

// Agora (v2, JPA)
Livro livro = livroRepository.findById(idLivro)
    .orElseThrow(() -> new LivroNaoEncontradoException(idLivro));
```
A versão com JPA já une busca + validação + exceção customizada em uma única linha.

### Por que extrair `ConsoleUtils`?
Antes, cada método do menu (`cadastrarLivro`, `emprestarLivro`, etc.) repetia o mesmo `try/catch` para tratar entrada inválida do usuário. Centralizar essa lógica em `ConsoleUtils` elimina duplicação e deixa os métodos do menu focados apenas na sua responsabilidade (coletar dados e chamar o service).

### Por que ID gerado automaticamente agora?
Na v1 (HashMap), o ID era gerado manualmente (`emprestimos.size() + 1`), o que funciona em memória mas não é seguro em banco de dados real (risco de concorrência, exclusões gerando IDs repetidos). Com `@GeneratedValue`, o próprio PostgreSQL garante unicidade.

---

## Troubleshooting

**Erro: `SQLException: FATAL: database "biblioteca" does not exist`**
→ Crie a database: `CREATE DATABASE biblioteca;`

**Erro: `org.hibernate.exception.JDBCConnectionException`**
→ Verifique se o PostgreSQL está rodando e se `application.properties` está correto

---

## Autor

Guilherme Machado
[LinkedIn](https://linkedin.com/in/guilherme-bmachado) | [GitHub](https://github.com/devbianchi) | [Portfolio](https://guilherme-bmachado.vercel.app)

---

**Última atualização:** Setembro 2026
