package com.sysbiblioteca.util;

import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ConsoleUtils {
    private final Scanner scanner = new Scanner(System.in);

    public int lerInteiro(String mensagem) {
        while (true) {
            try {
                if (!mensagem.isEmpty()) {
                    System.out.print(mensagem);
                }
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erro: Digite um número válido");
                scanner.nextLine();
            }
        }
    }

    public Long lerLong(String mensagem) {
        while (true) {
            try {
                if (!mensagem.isEmpty()) {
                    System.out.print(mensagem);
                }
                return scanner.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Erro: Digite um número válido");
                scanner.nextLine();
            }
        }
    }

    public String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    public void limparScanner() {
        scanner.nextLine();
    }

    public void fecharScanner() {
        scanner.close();
    }

    public void exibirMenu() {
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

    public void limparTerminal() {
        // \033[H move o cursor para o topo, \033[2J limpa a tela
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
