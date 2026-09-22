package com.sysbiblioteca.exception;

public class LivroNaoEncontradoException extends BibliotecaException {
  public LivroNaoEncontradoException(Long id) {
    super("Livro não encontrado com id: " + id);
  }
}
