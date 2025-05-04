package com.gb.trabalho;

public class ItemLista {
    private String valor;
    private String descricao;
    private String data;
    private int tipo;

    public ItemLista(String valor, String descricao, String data, int tipo) {
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
    }

    public String getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public String getData() { return data; }
    public int getTipo() { return tipo; }
}