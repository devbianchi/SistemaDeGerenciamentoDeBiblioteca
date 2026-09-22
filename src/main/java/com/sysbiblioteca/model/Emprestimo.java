package com.sysbiblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table (name = "emprestimo")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmprestimo;

    private Long idUsuario;
    private Long idLivro;
    private LocalDate dataPedido;
    private int tempoEmprestimoDias;
    private LocalDate dataDevolucaoReal;

    public Emprestimo() { }

    public Emprestimo(Long idUsuario, Long idLivro, LocalDate dataPedido, int tempoEmprestimoDias, LocalDate dataDevolucaoReal) {

        validarIdUsuario(idUsuario);
        validarIdLivro(idLivro);
        validarDataPedido(dataPedido);
        validarTempoEmprestimoDias(tempoEmprestimoDias);

        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataPedido = dataPedido;
        this.tempoEmprestimoDias = tempoEmprestimoDias;
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    private void validarIdUsuario(Long idUsuario) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("Campo Id deve ser maior que 0!");
        }
    }

    private void validarIdLivro(Long idLivro) {
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

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        validarIdUsuario(idUsuario);
        this.idUsuario = idUsuario;
    }

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
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
