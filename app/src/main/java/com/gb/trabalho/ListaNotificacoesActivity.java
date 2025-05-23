package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListaNotificacoesActivity extends BaseActivity {
    FloatingActionButton btnadicionar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.lista_notificacoes);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Notificações");

        RecyclerView recyclerView = findViewById(R.id.recycler_extrato);
        List<ItemLista> itens = new ArrayList<>();
        itens.add(new ItemLista("", "Viagem de final de ano", "", 1)); //receita
        itens.add(new ItemLista("", "Teto de gastos mensal", "", 0)); // despesa

        ItemListaAdapter adapter = new ItemListaAdapter(this, itens, item -> {
            Intent intent = new Intent(this, CadastroNotificacoesActivity.class);
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
                intent = new Intent(ListaNotificacoesActivity.this, CadastroNotificacoesActivity.class);
                startActivity(intent);
            }
        });
    }
}