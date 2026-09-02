package service;

import model.Emprestimo;
import model.Livro;
import model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    @Test
    void deveEmprestar() {
        Usuario user = new Usuario(001, "Paulo", "020202", "paulo@email.com");
        Livro livro = new Livro(001, "livro1", "autor1", 2000, true);

        Emprestimo emprestimoNovo = new Emprestimo(1324, 001, 001, LocalDate.of(2022, 8, 29), 23, null);
        int idEmprestimo = emprestimoNovo.getIdEmprestimo();

        assertEquals(1324, idEmprestimo, "emprestimo existe");
    }
}