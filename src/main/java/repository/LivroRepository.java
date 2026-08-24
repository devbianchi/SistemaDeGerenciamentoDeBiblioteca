package repository;

import model.Livro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

public class LivroRepository {
  private final Map<Integer, Livro> livros = new HashMap<>();

  public void adicionar(Livro livro) {
    livros.put(livro.getId(), livro);
    // guarda no map, usando livro.getId() como chave
  }

  public Optional<Livro> buscarPorId(int id) {
    return Optional.ofNullable(livros.get(id));
    // busca no map; Optional porque pode não existir
  }

  public List<Livro> listarTodos() {
    return new ArrayList<>(livros.values());
    // retorna todos os valores do map como lista
  }

  public void remover(int id) {
    livros.remove(id);
    // remove do map pelo id
  }

  public boolean existePorId(int id) {
    return livros.containsKey(id);
    // verifica se a chave existe no map
  }
}
