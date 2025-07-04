package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.Adapter.ItemListaAdapter;
import com.gb.trabalho.DAO.InvestimentoDAO;
import com.gb.trabalho.Domain.Investimento;
import com.gb.trabalho.Domain.ItemLista;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListaInvestimentosActivity extends BaseActivity {
    FloatingActionButton btnadicionar;
    private EditText edtSearchInput;
    InvestimentoDAO investimentoDAO;
    List<Investimento> investimentos;
    Intent intent;
    List<ItemLista> itens;
    SimpleDateFormat dataString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.lista_investimentos);
        edtSearchInput = findViewById(R.id.edt_search_input);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Investimentos");
        RecyclerView recyclerView = findViewById(R.id.recycler_extrato);
        investimentoDAO = new InvestimentoDAO(this);

        edtSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String texto = charSequence.toString();
                buscarporTexto(texto.trim(), recyclerView);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        buscarporTexto("", recyclerView);

        btnadicionar = findViewById(R.id.btn_add);
        btnadicionar.setOnClickListener(view -> {
            intent = new Intent(ListaInvestimentosActivity.this, CadastroInvestimentoActivity.class);
            startActivity(intent);
        });
    }

    private void buscarporTexto(String texto, RecyclerView recyclerView) {

        if (texto.isEmpty()) {
            investimentos = investimentoDAO.listarTodos();
        } else {
            investimentos = investimentoDAO.listarporFiltro(texto);
        }
        itens = new ArrayList<>();

        for (Investimento investimento : investimentos) {
            int id = investimento.getId();
            double valor = investimento.getValor();
            String descricao = investimento.getDescricao();
            double percentualrentabilidade = investimento.getPercentualRentabilidade();
            Date data = investimento.getDataInicio();
            String dataString = (data != null) ? this.dataString.format(data) : "";

            itens.add(new ItemLista(valor, descricao, dataString, 1, id, 0, percentualrentabilidade));
        }

        ItemListaAdapter adapter = new ItemListaAdapter(this, itens, item -> {
            Intent intent = new Intent(this, CadastroInvestimentoActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("valor", item.getValor());
            intent.putExtra("descricao", item.getDescricao());
            intent.putExtra("datainicio", item.getData());
            intent.putExtra("percentualrentabilidade", item.getPercentual_rentabilidade());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}