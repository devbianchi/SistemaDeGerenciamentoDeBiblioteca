package service;

import exception.EmprestimoNaoEncontradoException;
import exception.LivroIndisponivelException;
import exception.LivroNaoEncontradoException;
import exception.UsuarioNaoEncontradoException;
import model.Emprestimo;
import model.Livro;
import model.Usuario;
import org.junit.jupiter.api.Test;
import repository.EmprestimoRepository;
import repository.LivroRepository;
import repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.Month;

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

    @Test
    void emprestimoUsuarioNaoExiste() {
        Livro livro = new Livro(1, "livro1", "autor1", 2000, true);
        LivroRepository livroRepository = new LivroRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
        EmprestimoService emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);

        livroRepository.adicionar(livro);

        assertThrows(UsuarioNaoEncontradoException.class, () -> emprestimoService.emprestar(999, 1, 14));
    }

    @Test
    void emprestimoLivroNaoExiste() {
        Usuario usuario = new Usuario(1, "Usuario1", "121212", "usuario@email.com");
        LivroRepository livroRepository = new LivroRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
        EmprestimoService emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);

        usuarioRepository.adicionar(usuario);

        assertThrows(LivroNaoEncontradoException.class, () -> emprestimoService.emprestar(1, 999, 14));
    }

    @Test
    void emprestimoLivroIndisponivel() {
        Usuario usuario = new Usuario(1, "Usuario1", "121212", "usuario@email.com");
        Livro livro = new Livro(2, "livro2", "autor2", 2022, false);
        LivroRepository livroRepository = new LivroRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
        EmprestimoService emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);

        usuarioRepository.adicionar(usuario);
        livroRepository.adicionar(livro);

        assertThrows(LivroIndisponivelException.class, () -> emprestimoService.emprestar(1, 2, 14));
    }

    @Test
    void emprestimoDevolucao() {
        Livro livro = new Livro(3, "livro3", "autor3", 2023, true);
        Usuario usuario = new Usuario(3, "usuario3", "030303", "usuario3@email.com");
        LivroRepository livroRepository = new LivroRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
        EmprestimoService emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);
        Emprestimo emprestimo = new Emprestimo(3, 3, 3, LocalDate.of(2025, Month.AUGUST, 24), 14, null);
        usuarioRepository.adicionar(usuario);
        livroRepository.adicionar(livro);
        emprestimoRepository.adicionar(emprestimo);

        emprestimoService.devolver(3);
        boolean devolvido = emprestimoRepository.buscarPorId(3).get().isDevolvido();

        assertEquals(true, devolvido, "Livro foi devolvido");
    }

    @Test
    void devolucaoEmprestimoNaoExiste() {
        LivroRepository livroRepository = new LivroRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository();
        EmprestimoService emprestimoService = new EmprestimoService(livroRepository, usuarioRepository, emprestimoRepository);

        assertThrows(EmprestimoNaoEncontradoException.class, () -> emprestimoService.devolver(999));
    }
}