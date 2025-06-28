package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.Adapter.ItemListaAdapter;
import com.gb.trabalho.DAO.MovimentacaoDAO;
import com.gb.trabalho.Domain.ItemLista;
import com.gb.trabalho.Domain.Movimentacao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExtratoActivity extends BaseActivity {
    FloatingActionButton btnadicionar;
    Intent intent;
    MovimentacaoDAO movimentacaoDAO;
    RecyclerView recyclerView;
    ItemListaAdapter adapter;
    List<ItemLista> itens = new ArrayList<>();
    SimpleDateFormat dataString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    int anoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.extrato);

        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Extrato");

        recyclerView = findViewById(R.id.recycler_extrato);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movimentacaoDAO = new MovimentacaoDAO(this);
        anoAtual = Calendar.getInstance().get(Calendar.YEAR);

        int mesAtual = Calendar.getInstance().get(Calendar.MONTH) + 1;
        carregarMovimentacoesPorMes(mesAtual, anoAtual);

        LinearLayout monthContainer = findViewById(R.id.month_container);
        for (int i = 0; i < monthContainer.getChildCount(); i++) {
            View v = monthContainer.getChildAt(i);
            if (v instanceof Button) {
                final int mesSelecionado = i + 1;
                ((Button) v).setOnClickListener(view -> carregarMovimentacoesPorMes(mesSelecionado, anoAtual));
            }
        }

        btnadicionar = findViewById(R.id.btn_add);
        btnadicionar.setOnClickListener(view -> {
            intent = new Intent(ExtratoActivity.this, CadastroMovimentacaoActivity.class);
            startActivity(intent);
        });
    }

    private void carregarMovimentacoesPorMes(int mes, int ano) {
        List<Movimentacao> movimentacoes = movimentacaoDAO.listarPorMes(mes, ano);
        itens.clear();
        for (Movimentacao movimentacao : movimentacoes) {
            String valor = "R$ " + movimentacao.getValor();
            String descricao = movimentacao.getDescricao();
            Date data = movimentacao.getData();
            String dataString = (data != null) ? this.dataString.format(data) : "";
            int tipo = movimentacao.getTipo();
            int id = movimentacao.getId();
            itens.add(new ItemLista(valor, descricao, dataString, tipo, id, 0));
        }

        adapter = new ItemListaAdapter(this, itens, item -> {
            Intent intent = new Intent(ExtratoActivity.this, CadastroMovimentacaoActivity.class);
            intent.putExtra("valor", item.getValor());
            intent.putExtra("descricao", item.getDescricao());
            intent.putExtra("data", item.getData());
            intent.putExtra("tipo", item.getTipo());
            intent.putExtra("id", item.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}