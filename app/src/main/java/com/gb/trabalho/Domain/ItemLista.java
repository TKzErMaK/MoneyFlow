package com.gb.trabalho.Domain;

public class ItemLista {
    private int id;
    private String valor;
    private String descricao;
    private String data;
    private int tipo;
    private int prazo;

    public ItemLista(String valor, String descricao, String data, int tipo, int id, int prazo) {
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
        this.id = id;
        this.prazo = prazo;
    }

    public String getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public String getData() { return data; }
    public int getTipo() { return tipo; }
    public int getId(){return id;}
    public int getPrazo(){return prazo;}
}