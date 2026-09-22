package com.sysbiblioteca.service;

import com.sysbiblioteca.model.Livro;
import com.sysbiblioteca.model.Usuario;
import com.sysbiblioteca.exception.EmprestimoNaoEncontradoException;
import com.sysbiblioteca.exception.LivroIndisponivelException;
import com.sysbiblioteca.exception.LivroNaoEncontradoException;
import com.sysbiblioteca.exception.UsuarioNaoEncontradoException;
import com.sysbiblioteca.model.Emprestimo;
import com.sysbiblioteca.repository.EmprestimoRepository;
import com.sysbiblioteca.repository.LivroRepository;
import com.sysbiblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(LivroRepository livroRepository, UsuarioRepository usuarioRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public Emprestimo emprestar(Long idUsuario, Long idLivro, int tempoEmprestimoDias) {
        // busca usuario
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));

        // busca livro
        Livro livro = livroRepository.findById(idLivro)
                .orElseThrow(() -> new LivroNaoEncontradoException(idLivro));

        // verifica disponibilidade do livro
        if (!livro.isDisponibilidade()) {
            throw new LivroIndisponivelException(idLivro);
        }

        // Realiza empréstimo
        Emprestimo emprestimo = new Emprestimo(idUsuario, idLivro, LocalDate.now(), tempoEmprestimoDias, null);

        // atualiza disponibilidade do livro
        livro.setDisponibilidade(false);
        livroRepository.save(livro);

        // adiciona o emprestimo no emprestimo repo
        emprestimoRepository.save(emprestimo);

        return emprestimo;
    }

    public Emprestimo devolver(Long idEmprestimo) {
        // busca emprestimo
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new EmprestimoNaoEncontradoException(idEmprestimo));

        // guarda id do livro
        Long idLivro = emprestimo.getIdLivro();

        // busca livro
        Livro livro = livroRepository.findById(idLivro)
                .orElseThrow(() -> new LivroNaoEncontradoException(idLivro));

        // marca como disponível de novo
        livro.setDisponibilidade(true);

        // registra data da devolução
        emprestimo.setDataDevolucaoReal(LocalDate.now());

        // persistência no banco
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        return emprestimo;
    }

    public void listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        if (emprestimos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registrado.\n");
            return;
        }

        System.out.println("\n=== LISTA DE EMPRÉSTIMOS ===");
        for (Emprestimo emprestimo : emprestimos) {
            String status = emprestimo.isDevolvido() ? "Devolvido" : "Pendente";
            System.out.println("ID: " + emprestimo.getIdEmprestimo() +
                    " | Usuário: " + emprestimo.getIdUsuario() +
                    " | Livro: " + emprestimo.getIdLivro() +
                    " | Status: " + status);
        }
        System.out.println();
    }
}
