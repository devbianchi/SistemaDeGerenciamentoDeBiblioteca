package service;

import exception.EmprestimoNaoEncontradoException;
import exception.LivroIndisponivelException;
import exception.LivroNaoEncontradoException;
import exception.UsuarioNaoEncontradoException;
import model.Emprestimo;
import model.Livro;
import repository.EmprestimoRepository;
import repository.LivroRepository;
import repository.UsuarioRepository;

import java.time.LocalDate;

public class EmprestimoService {
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(LivroRepository livroRepository, UsuarioRepository usuarioRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public Emprestimo emprestar(int idUsuario, int idLivro, int tempoEmprestimoDias) {
        // valida ‘user’
        if (!usuarioRepository.existePorId(idUsuario)) {
            throw new UsuarioNaoEncontradoException(idUsuario);
        }

        // valida livro
        if (!livroRepository.existePorId(idLivro)) {
            throw new LivroNaoEncontradoException(idLivro);
        }

        // valida disponibilidade do livro
        if (!livroRepository.buscarPorId(idLivro).get().isDisponibilidade()) {
            throw new LivroIndisponivelException(idLivro);
        }

        // gera o emprestimo
        int emprestimoId= emprestimoRepository.gerarProximoId();
        Emprestimo emprestimo = new Emprestimo(emprestimoId, idUsuario, idLivro, LocalDate.now(), tempoEmprestimoDias, null);

        // atualiza disponibilidade do livro
        livroRepository.buscarPorId(idLivro).get().setDisponibilidade(false);

        // adiciona o emprestimo no emprestimo repo
        emprestimoRepository.adicionar(emprestimo);

        return emprestimo;
    }

    public Emprestimo devolver(int idEmprestimo) {
        // valida emprestimo
        if (!emprestimoRepository.existePorId(idEmprestimo)) {
            throw new EmprestimoNaoEncontradoException(idEmprestimo);
        }

        // busca emprestimo e livro
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(idEmprestimo).get();
        Livro livro = livroRepository.buscarPorId(emprestimo.getIdLivro()).get();

        // atualiza disponibilidade
        livro.setDisponibilidade(true);

        // registra data da devolucao real
        emprestimo.setDataDevolucaoReal(LocalDate.now());

        return emprestimo;
    }
}
