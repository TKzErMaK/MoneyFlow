package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.Adapter.ItemListaAdapter;
import com.gb.trabalho.Domain.ItemLista;
import com.gb.trabalho.DAO.NotificacaoDAO;
import com.gb.trabalho.Domain.Notificacao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListaNotificacoesActivity extends BaseActivity {
    FloatingActionButton btnadicionar;
    Intent intent;
    NotificacaoDAO notificacaoDAO;
    private EditText edtSearchInput;
    List<Notificacao> notificacoes;
    List<ItemLista> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.lista_notificacoes);
        edtSearchInput = findViewById(R.id.edt_search_input);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Notificações");
        RecyclerView recyclerView = findViewById(R.id.recycler_extrato);
        notificacaoDAO = new NotificacaoDAO(this);

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
            intent = new Intent(ListaNotificacoesActivity.this, CadastroNotificacoesActivity.class);
            startActivity(intent);
        });
    }

    private void buscarporTexto(String texto, RecyclerView recyclerView) {

        if (texto.isEmpty()) {
            notificacoes = notificacaoDAO.listarTodos();
        }
        else{
            notificacoes = notificacaoDAO.listarporFiltro(texto);
        }
        itens = new ArrayList<>();

        for (Notificacao notificacao : notificacoes) {
            String valor = String.valueOf("R$ " + notificacao.getValor());
            String descricao = notificacao.getDescricao();
            int tipo = notificacao.getTipo();
            int id = notificacao.getId();
            int prazo = notificacao.getPrazo();

            itens.add(new ItemLista(valor, descricao, "", tipo, id, prazo));
        }

        ItemListaAdapter adapter = new ItemListaAdapter(this, itens, item -> {
            Intent intent = new Intent(this, CadastroNotificacoesActivity.class);
            intent.putExtra("valor", item.getValor());
            intent.putExtra("descricao", item.getDescricao());
            intent.putExtra("data", item.getData());
            intent.putExtra("tipo", item.getTipo());
            intent.putExtra("id", item.getId());
            intent.putExtra("prazo", item.getPrazo());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
