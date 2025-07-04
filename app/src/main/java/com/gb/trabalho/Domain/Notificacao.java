package com.gb.trabalho.Domain;

import java.util.Date;

public class Notificacao {

    private int id;
    private String descricao;
    private double valor;
    private int prazo;
    private int tipo;
    private Date dataVencimento;

    public Notificacao(){}

    public Notificacao(String descricao, double valor, int prazo, int tipo, Date dataVencimento) {
        this.descricao = descricao;
        this.valor = valor;
        this.prazo = prazo;
        this.tipo = tipo;
        this.dataVencimento = dataVencimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    public Date getDataVencimento() {return dataVencimento;}
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

}
