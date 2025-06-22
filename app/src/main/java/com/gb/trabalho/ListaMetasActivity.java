package com.gb.trabalho;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;

import com.gb.trabalho.DAO.MetaDAO;
import com.gb.trabalho.Domain.Meta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListaMetasActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> cadastroMetaLauncher;
    private MetaDAO metaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.lista_metas);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Metas");

        metaDAO = new MetaDAO(this);

        cadastroMetaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        carregarMetas();
                    }
                }
        );

        FloatingActionButton btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListaMetasActivity.this, CadastroMetasActivity.class);
            cadastroMetaLauncher.launch(intent);
        });

        carregarMetas();
    }

    private void carregarMetas() {
        LinearLayout container = findViewById(R.id.goals_container);
        container.removeAllViews();

        List<Meta> metas = metaDAO.listarTodos();

        for (Meta meta : metas) {
            adicionarCard(meta);
        }
    }

    private void adicionarCard(Meta meta) {
        LinearLayout container = findViewById(R.id.goals_container);

        CardView card = new CardView(this);
        card.setCardElevation(8);
        card.setUseCompatPadding(true);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = nf.format(meta.getValor());

        TextView txtValor = new TextView(this);
        txtValor.setText(valorFormatado);
        txtValor.setTextSize(16);
        txtValor.setTextColor(Color.parseColor("#000000"));
        txtValor.setTypeface(Typeface.DEFAULT_BOLD);

        TextView txtDescricao = new TextView(this);
        txtDescricao.setText(meta.getDescricao());
        txtDescricao.setTextSize(14);

        TextView txtData = new TextView(this);
        txtData.setText(meta.getDataInicio());
        txtData.setGravity(Gravity.END);

        int corTextoData = meta.getTipo() == 1 ? Color.parseColor("#1A237E") : Color.parseColor("#880E4F");
        txtData.setTextColor(corTextoData);

        layout.addView(txtValor);
        layout.addView(txtDescricao);
        layout.addView(txtData);

        card.addView(layout);

        int cor = meta.getTipo() == 1 ? Color.parseColor("#D0E7FF") : Color.parseColor("#FFE3E3");
        card.setCardBackgroundColor(cor);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, VisualizaMetasActivity.class);
            intent.putExtra("descricao", meta.getDescricao());
            intent.putExtra("valor", valorFormatado);
            intent.putExtra("data", meta.getDataInicio());
            intent.putExtra("prazo", meta.getPrazo());
            intent.putExtra("isReceita", meta.getTipo() == 1);
            startActivity(intent);
        });

        container.addView(card, params);
    }
}
