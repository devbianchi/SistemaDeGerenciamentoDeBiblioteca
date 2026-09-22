package com.sysbiblioteca.service;

import com.sysbiblioteca.model.Livro;
import com.sysbiblioteca.repository.LivroRepository;
import com.sysbiblioteca.util.ConsoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ConsoleUtils input;

    public void cadastrarLivro() {
        try {
            String nome = input.lerString("Nome: ");
            String autor = input.lerString("Autor: ");
            int ano = input.lerInteiro("Ano: ");

            Livro livro = new Livro(nome, autor, ano, true);
            livroRepository.save(livro);
            System.out.println("Livro adicionado com êxito!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
            input.limparScanner();
        }
    }

    public void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
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

}
