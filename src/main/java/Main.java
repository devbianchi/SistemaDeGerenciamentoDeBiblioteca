import model.Livro;
import model.Usuario;
import repository.EmprestimoRepository;
import repository.LivroRepository;
import repository.UsuarioRepository;
import service.EmprestimoService;

import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        LivroRepository livroRepository = new LivroRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
        EmprestimoService emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);
        Scanner sc = new Scanner(System.in);

        while (true) {
            exibirMenu();
            int op = sc.nextInt();

            switch (op) {
                case 1:
                    cadastrarLivro(sc, livroRepository);
                    break;
                case 2:
                    cadastrarUsuario(sc, usuarioRepository);
                    break;
                case 3:

            }
        }

    }

    public static void exibirMenu() {
        System.out.println("=== BIBLIOTECA SISTEMA ===");
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

    public static void cadastrarLivro(Scanner sc, LivroRepository livroRepository) {
        System.out.print("\nId: ");
        int id = sc.nextInt();
        System.out.print("\nNome: ");
        String nome = sc.next();
        System.out.print("\nAutor: ");
        String autor = sc.next();
        System.out.print("\nAno: ");
        int ano = sc.nextInt();

        Livro livro = new Livro(id, nome, autor, ano, true);
        livroRepository.adicionar(livro);
        System.out.println("Livro adicionado com êxito!");
    }

    public static void cadastrarUsuario(Scanner sc, UsuarioRepository usuarioRepository) {
        System.out.print("\nId: ");
        int id = sc.nextInt();
        System.out.print("\nNome: ");
        String nome = sc.next();
        System.out.print("\nSenha: ");
        String senha = sc.next();
        System.out.print("\nEmail: ");
        String email = sc.next();

        Usuario usuario = new Usuario(id, nome, senha, email);
        usuarioRepository.adicionar(usuario);
        System.out.println("Usuário adicionado com êxito!");
    }

    public static void emprestarLivro(Scanner sc, EmprestimoService emprestimoService) {
        System.out.print("\nId do Usuário: ");
        int idUsuario = sc.nextInt();
        System.out.print("\nId do Livro: ");
        int idLivro = sc.nextInt();
        System.out.print("\nTempo de Empréstimo (em dias): ");
        int tempoEmDias = sc.nextInt();

        emprestimoService.emprestar(idUsuario, idLivro, tempoEmDias);

    }
}
