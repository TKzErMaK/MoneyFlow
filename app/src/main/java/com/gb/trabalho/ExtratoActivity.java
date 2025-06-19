package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.Adapter.ItemListaAdapter;
import com.gb.trabalho.Domain.ItemLista;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExtratoActivity extends BaseActivity {
    FloatingActionButton btnadicionar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.extrato);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Extrato");

        RecyclerView recyclerView = findViewById(R.id.recycler_extrato);
        List<ItemLista> itens = new ArrayList<>();
        itens.add(new ItemLista("R$ 3.000,00", "Salário", "06/04/2025", 1,0, 0));
        itens.add(new ItemLista("R$ 120,00", "Energia elétrica", "10/04/2025", 0,0,0));

        ItemListaAdapter adapter = new ItemListaAdapter(this, itens, item -> {
            Intent intent = new Intent(ExtratoActivity.this, CadastroMovimentacaoActivity.class);
            intent.putExtra("valor", item.getValor());
            intent.putExtra("descricao", item.getDescricao());
            intent.putExtra("data", item.getData());
            intent.putExtra("tipo", item.getTipo());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnadicionar = findViewById(R.id.btn_add);
        btnadicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ExtratoActivity.this, CadastroMovimentacaoActivity.class);
                startActivity(intent);
            }
        });
    }
}