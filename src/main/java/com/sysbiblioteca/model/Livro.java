package com.sysbiblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String autor;
    private int ano;
    private boolean disponibilidade;

    public Livro() { }

    public Livro(String nome, String autor, int ano, boolean disponibilidade) {
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Campo nome não deve estar vazio!");
        }

        if (autor.isBlank()) {
            throw new IllegalArgumentException("Campo autor não deve estar vazio!");
        }

        if (ano < 1500) {
            throw new IllegalArgumentException("Insira um ano válido!");
        }

        this.nome = nome;
        this.autor = autor;
        this.ano = ano;
        this.disponibilidade = disponibilidade;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    @Override
    public String toString() {
        return String.format("Livro{id=%d, nome='%s', autor='%s', ano=%d, disponivel=%s}",
                id, nome, autor, ano, disponibilidade);
    }

}
