package model;

public class Livro {
    private final int id;
    private final String nome;
    private final String autor;
    private final int ano;
    private boolean disponibilidade;

    public Livro(int id, String nome, String autor, int ano, boolean disponibilidade) {

        if (id <= 0) {
            throw new IllegalArgumentException("Campo Id deve ser maior que 0!");
        }

        if (nome.isBlank()) {
            throw new IllegalArgumentException("Campo nome não deve estar vazio!");
        }

        if (autor.isBlank()) {
            throw new IllegalArgumentException("Campo autor não deve estar vazio!");
        }

        if (ano < 1500) {
            throw new IllegalArgumentException("Insira um ano válido!");
        }

        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.ano = ano;
        this.disponibilidade = disponibilidade;
    }

    public int getId() {
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
