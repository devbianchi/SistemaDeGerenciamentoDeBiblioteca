package exception;

public class EmprestimoNaoEncontradoException extends BibliotecaException {
    public EmprestimoNaoEncontradoException(int id) {
        super("Empréstimo não encontrado com id: " + id);
    }
}
