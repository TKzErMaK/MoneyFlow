package com.gb.trabalho.Domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Investimento {

    private int id;
    private String descricao;
    private double valor;
    private Date dataInicio;
    private double percentualRentabilidade;
    private String frequencia;;

    public Investimento() {
    }

    public Investimento(String descricao, double valor, Date dataInicio, double percentualRentabilidade, String frequencia) {
        this.descricao = descricao;
        this.valor = valor;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.dataInicio = dataInicio;
        this.percentualRentabilidade = percentualRentabilidade;
        this.frequencia = frequencia;
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

    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public double getPercentualRentabilidade() {
        return percentualRentabilidade;
    }
    public void setPercentualRentabilidade(double percentualRentabilidade) {
        this.percentualRentabilidade = percentualRentabilidade;
    }
    public String getFrequencia() {
        return frequencia;
    }
    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }
}
