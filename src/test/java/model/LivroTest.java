package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {
    @Test
    void livroExiste() {
        Livro livro = new Livro(2134, "nome 1", "autor 1", 2008, true);

        int idLivro = livro.getId();

        assertEquals(2134, idLivro, "Livro com id 2134 deve existir");
    }

    @Test
    void defineDisponibilidade() {
        Livro livro = new Livro(2134, "nome 1", "autor 1", 2008, true);

        livro.setDisponibilidade(false);

        assertEquals(false, livro.isDisponibilidade(), "Livro nao deve estar disponivel");
    }
}