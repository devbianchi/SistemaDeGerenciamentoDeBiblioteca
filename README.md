#  Biblioteca - Sistema de Gerenciamento de Empréstimos

Um sistema de gerenciamento de biblioteca em **Java puro**, demonstrando princípios sólidos de Programação Orientada a Objetos, arquitetura em camadas e boas práticas de desenvolvimento.

##  Objetivo

Este projeto foi desenvolvido como portfólio para candidaturas a vagas de **Desenvolvedor Backend Júnior em Java**. Implementa um sistema funcional de empréstimo e devolução de livros, com foco em **código limpo, testes automatizados e decisões arquiteturais conscientes**.

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
│  Repositories (LivroRepository, etc)    │  ← Acesso a dados
│   (CRUD, busca, persistência)           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Model (Livro, Usuario, etc)        │  ← Objetos de domínio
│   (Validação, encapsulamento)           │
└─────────────────────────────────────────┘
```

### Pacotes

- **`model/`** → Classes de domínio (Livro, Usuario, Emprestimo)
- **`repository/`** → Acesso a dados (LivroRepository, UsuarioRepository, EmprestimoRepository)
- **`service/`** → Lógica de negócio (EmprestimoService)
- **`exception/`** → Exceções customizadas (BibliotecaException e filhas)

---

##  Decisões de Design

### 1. **Padrão Repository com Map<Integer, Objeto>**
- Usa `HashMap` em memória (não banco de dados) para armazenar dados
- Chave = ID do objeto, Valor = objeto completo
- Facilita busca rápida por ID e evita problema de `equals()`/`hashCode()` desnecessários

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

### 5. **Uso de `Optional`**
- Métodos de busca retornam `Optional<T>` em vez de `null`
- Força quem usa a lidar explicitamente com "não encontrado"
- Evita `NullPointerException` silenciosos

### 6. **Dados Calculados vs. Armazenados**
- `dataPrevistaDevolucao` é **calculada** (via `getDataPrevistaDevolucao()`) usando `dataPedido + tempoEmprestimoDias`
- `dataDevolucaoReal` é **armazenada** (muda com o tempo)
- Evita redundância e inconsistência de dados

---

##  Funcionalidades

### Menu Principal

```
1. Cadastrar Livro          → Adiciona novo livro ao acervo
2. Cadastrar Usuário        → Registra novo usuário
3. Emprestar Livro          → Cria empréstimo (valida disponibilidade)
4. Devolver Livro           → Registra devolução
5. Listar Livros            → Exibe todos os livros + status
6. Listar Usuários          → Exibe todos os usuários
7. Listar Empréstimos       → Exibe histórico de empréstimos
8. Sair                     → Encerra o programa
```

### Validações Implementadas

**Ao emprestar um livro:**
-  Usuário deve existir (senão: `UsuarioNaoEncontradoException`)
-  Livro deve existir (senão: `LivroNaoEncontradoException`)
-  Livro deve estar disponível (senão: `LivroIndisponivelException`)
-  Tempo de empréstimo deve ser > 0 (validação no model)

**Ao devolver:**
-  Empréstimo deve existir (senão: `EmprestimoNaoEncontradoException`)
-  Livro volta a ficar disponível

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
- **Arrange:** prepara dados (cria repositories, adiciona livros/usuários)
- **Act:** executa a ação (`emprestar()`, `devolver()`)
- **Assert:** valida resultado (exceção lançada, estado mudou)

---

## 🛠️ Como Usar

### Compilar

```bash
javac -d bin $(find . -name "*.java")
```

### Rodar

```bash
java -cp bin Main
```

### Rodar Testes

```bash
# Requer JUnit 5 configurado no classpath
javac -cp .:junit-jupiter-api-5.x.x.jar -d bin src/test/java/service/EmprestimoServiceTest.java
java -cp bin:junit-jupiter-api-5.x.x.jar:junit-platform-console-standalone-1.x.x.jar org.junit.platform.console.ConsoleLauncher --scan-classpath bin
```

---

##  Tecnologias

- **Java 21** — Linguagem principal
- **JUnit 5** — Framework de testes
- **HashMap** — Estrutura de dados para persistência em memória
- **LocalDate** — API moderna de datas (java.time)

---

##  Conceitos Demonstrados

### Programação Orientada a Objetos
-  Encapsulamento (`private`, `final`, getters/setters)
-  Herança (exceções customizadas estendem `BibliotecaException`)
-  Abstração (`abstract class`, interfaces implícitas)
-  Polimorfismo (tratamento de múltiplas exceções)

### Arquitetura e Design
-  Padrão Repository (abstração de acesso a dados)
-  Padrão Service (orquestra lógica de negócio)
-  Separação de responsabilidades (camadas)
-  Dependency Injection (repositories passados via construtor)

### Boas Práticas
-  Tratamento de exceções customizadas
-  Validação de entrada (model + service)
-  Código limpo e legível
-  Métodos reutilizáveis (`lerInteiro`, `lerString`)
-  Testes automatizados

---

##  Próximos Passos

Este projeto foi desenvolvido como base sólida antes de migrar para **Spring Boot**, onde conceitos como:
- `@Repository` → encapsula o padrão Repository
- `@Service` → encapsula a lógica de negócio
- `@Entity` → mapeia objetos para banco de dados
- `@RestController` → expõe operações via API REST

...já serão familiares e compreensíveis.

---

##  Estrutura de Arquivos

```
biblioteca/
├── model/
│   ├── Livro.java
│   ├── Usuario.java
│   └── Emprestimo.java
├── repository/
│   ├── LivroRepository.java
│   ├── UsuarioRepository.java
│   └── EmprestimoRepository.java
├── service/
│   └── EmprestimoService.java
├── exception/
│   ├── BibliotecaException.java
│   ├── LivroNaoEncontradoException.java
│   ├── UsuarioNaoEncontradoException.java
│   ├── LivroIndisponivelException.java
│   └── EmprestimoNaoEncontradoException.java
├── service/test/
│   └── EmprestimoServiceTest.java
├── Main.java
└── README.md
```

---

##  Notas de Desenvolvimento

### Por que não usar setters desnecessários?
Em `Livro`, o atributo `id` não tem setter porque a identidade de um objeto não deveria mudar. Setters existem apenas para dados que **realmente mudam** (`disponibilidade`, estado do empréstimo).

### Por que `@Override toString()`?
Sem isso, `System.out.println(livro)` retorna algo como `model.Livro@1a2b3c`. Com `@Override`, retorna formato legível: `Livro{id=1, nome='Dom Casmurro', ...}`.

### Por que `private` em métodos de validação?
Validação é um detalhe interno da classe. Métodos `private` indicam "não é responsabilidade de quem chama fazer isso, apenas use o construtor/setter que já valida".

---

##  Autor

Guilherme Machado  
[LinkedIn](https://linkedin.com/in/guilherme-bmachado) | [GitHub](https://github.com/devbianchi)

---

**Última atualização:** Setembro 2026
