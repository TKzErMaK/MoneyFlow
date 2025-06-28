package com.gb.trabalho.Adapter;
import com.gb.trabalho.Domain.Investimento;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class InvestimentoAdapter {

    public static List<PieEntry> converterParaPieEntries(List<Investimento> investimentos) {
        List<PieEntry> entries = new ArrayList<>();

        for (Investimento investimento : investimentos) {
            float valor = (float) investimento.getValor();
            String descricao = investimento.getDescricao();
            entries.add(new PieEntry(valor, descricao));
        }

        return entries;
    }

}
