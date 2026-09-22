package com.sysbiblioteca.tui;

import com.sysbiblioteca.exception.BibliotecaException;
import com.sysbiblioteca.model.Emprestimo;
import com.sysbiblioteca.service.EmprestimoService;
import com.sysbiblioteca.service.LivroService;
import com.sysbiblioteca.service.UsuarioService;
import com.sysbiblioteca.util.ConsoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuPrincipal {
    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroService livroService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ConsoleUtils input;

    public void iniciarMenu() {
        input.limparTerminal();
        while (true) {
            input.exibirMenu();
            int op = input.lerInteiro("");

            switch (op) {
                case 1:
                    livroService.cadastrarLivro();
                    break;
                case 2:
                    usuarioService.cadastrarUsuario();
                    break;
                case 3:
                    emprestarLivro();
                    break;
                case 4:
                    devolverLivro();
                    break;
                case 5:
                    livroService.listarLivros();
                    break;
                case 6:
                    usuarioService.listarUsuarios();
                    break;
                case 7:
                    emprestimoService.listarEmprestimos();
                    break;
                case 8:
                    System.out.println("\nEncerrando sistema...");
                    input.fecharScanner();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.\n");
            }
        }
    }

    public void emprestarLivro() {
        try {
            Long idUsuario = input.lerLong("\nId do Usuário: ");
            Long idLivro = input.lerLong("Id do Livro: ");
            int tempoEmDias = input.lerInteiro("Tempo de Empréstimo (em dias): ");

            Emprestimo emprestimo = emprestimoService.emprestar(idUsuario, idLivro, tempoEmDias);
            System.out.print("Empréstimo realizado com sucesso!\n");
            System.out.printf("Data prevista de devolução: %s\n\n", emprestimo.getDataPrevistaDevolucao());
        } catch (BibliotecaException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            input.limparScanner();
        }
    }

    public void devolverLivro() {
        try {
            Long idEmprestimo = input.lerLong("\nId do Empréstimo: ");

            Emprestimo emprestimo = emprestimoService.devolver(idEmprestimo);
            System.out.print("Livro devolvido com sucesso!\n");
            System.out.printf("Data da devolução: %s\n\n", emprestimo.getDataDevolucaoReal());
        } catch (BibliotecaException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            input.limparScanner();
        }
    }

}
