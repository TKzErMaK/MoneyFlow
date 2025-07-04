package com.gb.trabalho.Domain;

public class ItemLista {
    private int id;
    private double valor;
    private String descricao;
    private String data;
    private int tipo;
    private int prazo;
    private double percentual_rentabilidade;

    public ItemLista(double valor, String descricao, String data, int tipo, int id, int prazo, double percentual_rentabilidade) {
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
        this.id = id;
        this.prazo = prazo;
        this.percentual_rentabilidade = percentual_rentabilidade;
    }

    public double getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public String getData() { return data; }
    public int getTipo() { return tipo; }
    public int getId(){return id;}
    public int getPrazo(){return prazo;}
    public double getPercentual_rentabilidade(){return percentual_rentabilidade;}
}