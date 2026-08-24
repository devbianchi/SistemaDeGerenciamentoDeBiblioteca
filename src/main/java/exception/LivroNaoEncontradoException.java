package exception;

public class LivroNaoEncontradoException extends BibliotecaException {
  public LivroNaoEncontradoException(int id) {
    super("Livro não encontrado com id: " + id);
  }
}
