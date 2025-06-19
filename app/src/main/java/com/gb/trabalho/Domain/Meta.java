package com.gb.trabalho.Domain;

public class Meta {
    private int id;
    private String descricao;
    private double valor;
    private String dataInicio;
    private int prazo;
    private int tipo;

    public Meta() {
    }

    public Meta(String descricao, double valor, String dataInicio, int prazo, int tipo) {
        this.descricao = descricao;
        this.valor = valor;
        this.dataInicio = dataInicio;
        this.prazo = prazo;
        this.tipo = tipo;
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

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
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
}
