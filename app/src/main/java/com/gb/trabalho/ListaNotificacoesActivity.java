package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.Adapter.ItemListaAdapter;
import com.gb.trabalho.Domain.ItemLista;
import com.gb.trabalho.DAO.NotificacaoDAO;
import com.gb.trabalho.Domain.Notificacao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaNotificacoesActivity extends BaseActivity {
    FloatingActionButton btnadicionar;
    Intent intent;
    NotificacaoDAO notificacaoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.lista_notificacoes);

        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Notificações");

        RecyclerView recyclerView = findViewById(R.id.recycler_extrato);

        notificacaoDAO = new NotificacaoDAO(this);
        List<Notificacao> notificacoes = notificacaoDAO.listarTodos();

        List<ItemLista> itens = new ArrayList<>();
        for (Notificacao notificacao : notificacoes) {
            String valor = String.valueOf("R$ " + notificacao.getValor());
            String descricao = notificacao.getDescricao();
            Date data = notificacao.getDataInicio();
            String dataString = (data != null) ? data.toString() : "";
            int tipo = notificacao.getTipo();
            int id = notificacao.getId();
            int prazo = notificacao.getPrazo();

            itens.add(new ItemLista(valor, descricao, dataString, tipo, id, prazo));
        }

        ItemListaAdapter adapter = new ItemListaAdapter(this, itens, item -> {
            Intent intent = new Intent(this, CadastroNotificacoesActivity.class);
            intent.putExtra("valor", item.getValor());
            intent.putExtra("descricao", item.getDescricao());
            intent.putExtra("data", item.getData());
            intent.putExtra("tipo", item.getTipo());
            intent.putExtra("id",item.getId());
            intent.putExtra("prazo",item.getPrazo());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnadicionar = findViewById(R.id.btn_add);
        btnadicionar.setOnClickListener(view -> {
            intent = new Intent(ListaNotificacoesActivity.this, CadastroNotificacoesActivity.class);
            startActivity(intent);
        });
    }
}
