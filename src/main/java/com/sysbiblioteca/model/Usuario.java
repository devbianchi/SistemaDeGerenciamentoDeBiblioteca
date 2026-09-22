package com.sysbiblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String senha;
    private String email;

    public Usuario() { }

    public Usuario(String nome, String senha, String email) {

        if (id <= 0) {
            throw new IllegalArgumentException("Campo Id deve ser maior que 0!");
        }

        validarNome(nome);
        validarSenha(senha);
        validarEmail(email);

        this.nome = nome;
        this.senha = senha;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        validarSenha(senha);
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarNome(String nome) {
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Campo Nome não deve ser vazio!");
        }
    }

    private void validarSenha(String senha) {
        if (senha.isBlank()) {
            throw new IllegalArgumentException("Campo Senha não deve ser vazio!");
        }
    }

    private void validarEmail(String email) {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Campo Email não deve ser vazio!");
        }
    }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nome='%s', email='%s'}",
                id, nome, email);
    }
}
