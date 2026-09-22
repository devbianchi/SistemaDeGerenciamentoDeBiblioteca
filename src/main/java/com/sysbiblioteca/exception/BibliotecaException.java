package com.sysbiblioteca.exception;

public abstract class BibliotecaException extends RuntimeException {
  public BibliotecaException(String message) {
    super(message);
  }

  public BibliotecaException(String message, Throwable causa) {
    super(message, causa);
  }
}
