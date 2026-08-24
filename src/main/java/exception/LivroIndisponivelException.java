package exception;

public class LivroIndisponivelException extends BibliotecaException {
  public LivroIndisponivelException(int id) {
    super("Livro indisponível com id: " + id);
  }
}
