import exception.BibliotecaException;
import model.Emprestimo;
import model.Livro;
import model.Usuario;
import repository.EmprestimoRepository;
import repository.LivroRepository;
import repository.UsuarioRepository;
import service.EmprestimoService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static LivroRepository livroRepository;
    private static UsuarioRepository usuarioRepository;
    private static EmprestimoRepository emprestimoRepository;
    private static EmprestimoService emprestimoService;
    private static Scanner sc;

    public static void main(String[] args) {
        livroRepository = new LivroRepository();
        usuarioRepository = new UsuarioRepository();
        emprestimoRepository = new EmprestimoRepository();
        emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);
        sc = new Scanner(System.in);

        while (true) {
            exibirMenu();
            int op = lerInteiro("", sc);

            switch (op) {
                case 1:
                    cadastrarLivro();
                    break;
                case 2:
                    cadastrarUsuario();
                    break;
                case 3:
                    emprestarLivro();
                    break;
                case 4:
                    devolverLivro();
                    break;
                case 5:
                    listarLivros();
                    break;
                case 6:
                    listarUsuarios();
                    break;
                case 7:
                    listarEmprestimos();
                    break;
                case 8:
                    System.out.println("\nEncerrando sistema...");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.\n");
            }
        }
    }

    private static int lerInteiro(String mensagem, Scanner sc) {
        while (true) {
            try {
                if (!mensagem.isEmpty()) {
                    System.out.print(mensagem);
                }
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erro: Digite um número válido");
                sc.nextLine();
            }
        }
    }

    private static String lerString(String mensagem, Scanner sc) {
        System.out.print(mensagem);
        return sc.next();
    }

    public static void exibirMenu() {
        System.out.println("\n=== BIBLIOTECA SISTEMA ===");
        System.out.println("1. Cadastrar Livro");
        System.out.println("2. Cadastrar Usuário");
        System.out.println("3. Emprestar Livro");
        System.out.println("4. Devolver Livro");
        System.out.println("5. Listar Livros");
        System.out.println("6. Listar Usuários");
        System.out.println("7. Listar Empréstimos");
        System.out.println("8. Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    public static void cadastrarLivro() {
        try {
            int id = lerInteiro("\nId: ", sc);
            String nome = lerString("Nome: ", sc);
            String autor = lerString("Autor: ", sc);
            int ano = lerInteiro("Ano: ", sc);

            Livro livro = new Livro(id, nome, autor, ano, true);
            livroRepository.adicionar(livro);
            System.out.println("Livro adicionado com êxito!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            sc.nextLine();
        }
    }

    public static void cadastrarUsuario() {
        try {
            int id = lerInteiro("\nId: ", sc);
            String nome = lerString("Nome: ", sc);
            String senha = lerString("Senha: ", sc);
            String email = lerString("Email: ", sc);

            Usuario usuario = new Usuario(id, nome, senha, email);
            usuarioRepository.adicionar(usuario);
            System.out.println("Usuário adicionado com êxito!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            sc.nextLine();
        }
    }

    public static void emprestarLivro() {
        try {
            int idUsuario = lerInteiro("\nId do Usuário: ", sc);
            int idLivro = lerInteiro("Id do Livro: ", sc);
            int tempoEmDias = lerInteiro("Tempo de Empréstimo (em dias): ", sc);

            Emprestimo emprestimo = emprestimoService.emprestar(idUsuario, idLivro, tempoEmDias);
            System.out.printf("Empréstimo realizado com sucesso!\n");
            System.out.printf("Data prevista de devolução: %s\n\n", emprestimo.getDataPrevistaDevolucao());
        } catch (BibliotecaException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            sc.nextLine();
        }
    }

    public static void devolverLivro() {
        try {
            int idEmprestimo = lerInteiro("\nId do Empréstimo: ", sc);

            Emprestimo emprestimo = emprestimoService.devolver(idEmprestimo);
            System.out.printf("Livro devolvido com sucesso!\n");
            System.out.printf("Data da devolução: %s\n\n", emprestimo.getDataDevolucaoReal());
        } catch (BibliotecaException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            sc.nextLine();
        }
    }

    public static void listarLivros() {
        List<Livro> livros = livroRepository.listarTodos();
        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro cadastrado.\n");
            return;
        }

        System.out.println("\n=== LISTA DE LIVROS ===");
        for (Livro livro : livros) {
            String status = livro.isDisponibilidade() ? "Disponível" : "Indisponível";
            System.out.println(livro + " | " + status);
        }
        System.out.println();
    }

    public static void listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("\nNenhum usuário cadastrado.\n");
            return;
        }

        System.out.println("\n=== LISTA DE USUÁRIOS ===");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
        System.out.println();
    }

    public static void listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.listarTodos();
        if (emprestimos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registrado.\n");
            return;
        }

        System.out.println("\n=== LISTA DE EMPRÉSTIMOS ===");
        for (Emprestimo emprestimo : emprestimos) {
            String status = emprestimo.isDevolvido() ? "Devolvido" : "Pendente";
            System.out.println("ID: " + emprestimo.getIdEmprestimo() +
                    " | Usuário: " + emprestimo.getIdUsuario() +
                    " | Livro: " + emprestimo.getIdLivro() +
                    " | Status: " + status);
        }
        System.out.println();
    }
}