package model;

import java.time.LocalDate;

public class Emprestimo {
    private final int idEmprestimo;
    private int idUsuario;
    private int idLivro;
    private LocalDate dataPedido;
    private int tempoEmprestimoDias;
    private LocalDate dataDevolucaoReal;

    public Emprestimo(int idEmprestimo, int idUsuario, int idLivro, LocalDate dataPedido, int tempoEmprestimoDias, LocalDate dataDevolucaoReal) {

        validarIdUsuario(idUsuario);
        validarIdLivro(idLivro);
        validarDataPedido(dataPedido);
        validarTempoEmprestimoDias(tempoEmprestimoDias);

        this.idEmprestimo = idEmprestimo;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataPedido = dataPedido;
        this.tempoEmprestimoDias = tempoEmprestimoDias;
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    private void validarIdUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("Campo Id deve ser maior que 0!");
        }
    }

    private void validarIdLivro(int idLivro) {
        if (idLivro <= 0) {
            throw new IllegalArgumentException("Campo Id deve ser maior que 0!");
        }
    }

    private void validarDataPedido(LocalDate dataPedido) {
        if (dataPedido == null) {
            throw new IllegalArgumentException("Data de pedido não pode ser nula!");
        }
    }

    private void validarTempoEmprestimoDias(int tempoEmprestimoDias) {
        if (tempoEmprestimoDias < 0) {
            throw new IllegalArgumentException("Tempo do empréstimo em dias deve ser maior que 0!");
        }
    }

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        validarIdUsuario(idUsuario);
        this.idUsuario = idUsuario;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        validarIdLivro(idLivro);
        this.idLivro = idLivro;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        validarDataPedido(dataPedido);
        this.dataPedido = dataPedido;
    }

    public int getTempoEmprestimoDias() {
        return tempoEmprestimoDias;
    }

    public void setTempoEmprestimoDias(int tempoEmprestimoDias) {
        validarTempoEmprestimoDias(tempoEmprestimoDias);
        this.tempoEmprestimoDias = tempoEmprestimoDias;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPedido.plusDays(tempoEmprestimoDias);
    }

    public boolean isDevolvido() {
        return dataDevolucaoReal != null;
    }
}
