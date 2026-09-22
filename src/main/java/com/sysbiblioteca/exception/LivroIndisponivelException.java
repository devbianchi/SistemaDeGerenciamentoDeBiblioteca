package com.sysbiblioteca.exception;

public class LivroIndisponivelException extends BibliotecaException {
  public LivroIndisponivelException(Long id) {
    super("Livro indisponível com id: " + id);
  }
}
