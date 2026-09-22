package com.sysbiblioteca.exception;

public class EmprestimoNaoEncontradoException extends BibliotecaException {
    public EmprestimoNaoEncontradoException(Long id) {
        super("Empréstimo não encontrado com id: " + id);
    }
}
