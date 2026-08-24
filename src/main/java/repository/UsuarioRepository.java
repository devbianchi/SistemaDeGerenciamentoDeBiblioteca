package repository;

import model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UsuarioRepository {
  private final Map<Integer, Usuario> usuarios = new HashMap<>();

  public void adicionar(Usuario usuario) {
    usuarios.put(usuario.getId(), usuario);
  }

  public Optional<Usuario> buscarPorId(int id) {
    return Optional.ofNullable(usuarios.get(id));
  }

  public List<Usuario> listarTodos() {
    return new ArrayList<>(usuarios.values());
  }

  public void remover(int id) {
    usuarios.remove(id);
  }

  public boolean existePorId(int id) {
    return usuarios.containsKey(id);
  }
}
