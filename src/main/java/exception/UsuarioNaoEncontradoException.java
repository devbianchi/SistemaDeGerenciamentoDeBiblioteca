package exception;

public class UsuarioNaoEncontradoException extends BibliotecaException {
  public UsuarioNaoEncontradoException(int id) {
    super("Usuário não encontrado com id: " + id);
  }
}
