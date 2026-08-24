package repository;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import model.Emprestimo;

public class EmprestimoRepository {
  private final Map<Integer, Emprestimo> emprestimos = new HashMap<>();

  public void adicionar(Emprestimo emprestimo) {
    emprestimos.put(emprestimo.getIdEmprestimo(), emprestimo);
  }

  public Optional<Emprestimo> buscarPorId(int id) {
    return Optional.ofNullable(emprestimos.get(id));
  }

  public List<Emprestimo> listarTodos() {
    return new ArrayList<>(emprestimos.values());
  }

  public void remover(int id) {
    emprestimos.remove(id);
  }

  public boolean existePorId(int id) {
    return emprestimos.containsKey(id);
  }
}
