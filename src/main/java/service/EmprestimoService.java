package service;

import exception.EmprestimoNaoEncontradoException;
import exception.LivroIndisponivelException;
import exception.LivroNaoEncontradoException;
import exception.UsuarioNaoEncontradoException;
import model.Emprestimo;
import model.Livro;
import model.Usuario;
import org.springframework.stereotype.Service;
import repository.EmprestimoRepository;
import repository.LivroRepository;
import repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Optional;

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
        boolean livroDisponivel = livro.isDisponibilidade();

        Emprestimo emprestimo = new Emprestimo(idUsuario, idLivro, LocalDate.now(), tempoEmprestimoDias, null);

        // atualiza disponibilidade do livro
        livro.setDisponibilidade(false);

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
}
