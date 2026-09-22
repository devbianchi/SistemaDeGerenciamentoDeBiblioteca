package exception;

public class UsuarioNaoEncontradoException extends BibliotecaException {
  public UsuarioNaoEncontradoException(Long id) {
    super("Usuário não encontrado com id: " + id);
  }
}
