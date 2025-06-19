package com.gb.trabalho.Domain;

public class Investimento {

    private int id;
    private String descricao;
    private double valor;
    private String dataInicio;
    private double percentualRentabilidade;
    private int prazo;

    public Investimento() {
    }

    public Investimento(String descricao, double valor, String dataInicio, double percentualRentabilidade, int prazo) {
        this.descricao = descricao;
        this.valor = valor;
        this.dataInicio = dataInicio;
        this.percentualRentabilidade = percentualRentabilidade;
        this.prazo = prazo;
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

    public double getPercentualRentabilidade() {
        return percentualRentabilidade;
    }

    public void setPercentualRentabilidade(double percentualRentabilidade) {
        this.percentualRentabilidade = percentualRentabilidade;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }
}
