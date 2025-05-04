package com.gb.trabalho;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaMetasActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> cadastroMetaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_metas);

        ConstraintLayout topBar = findViewById(R.id.top_bar_metas);
        TextView titulo = topBar.findViewById(R.id.txt_title);
        titulo.setText(R.string.txt_dynamic_title_goal);

        cadastroMetaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String descricao = data.getStringExtra("descricao");
                        String valor = data.getStringExtra("valor");
                        String dataMeta = data.getStringExtra("data");
                        int prazo = data.getIntExtra("prazo", 0);
                        boolean isReceita = data.getBooleanExtra("isReceita", true);

                        adicionarCard(descricao, valor, dataMeta, prazo, isReceita);
                    }
                }
        );

        FloatingActionButton btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListaMetasActivity.this, CadastroMetasActivity.class);
            cadastroMetaLauncher.launch(intent);
        });
    }

    private void adicionarCard(String descricao, String valor, String data, int prazo, boolean isReceita) {
        LinearLayout container = findViewById(R.id.goals_container);

        CardView card = new CardView(this);
        card.setCardElevation(8);
        card.setUseCompatPadding(true);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        TextView txtValor = new TextView(this);
        txtValor.setText(valor);
        txtValor.setTextSize(16);
        txtValor.setTextColor(Color.parseColor("#000000"));
        txtValor.setTypeface(Typeface.DEFAULT_BOLD);

        TextView txtDescricao = new TextView(this);
        txtDescricao.setText(descricao);
        txtDescricao.setTextSize(14);

        TextView txtData = new TextView(this);
        txtData.setText(data);
        txtData.setGravity(Gravity.END);

        int corTextoData = isReceita ? Color.parseColor("#1A237E") : Color.parseColor("#880E4F");
        txtData.setTextColor(corTextoData);

        layout.addView(txtValor);
        layout.addView(txtDescricao);
        layout.addView(txtData);

        card.addView(layout);

        int cor = isReceita ? Color.parseColor("#D0E7FF") : Color.parseColor("#FFE3E3");
        card.setCardBackgroundColor(cor);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, VisualizaMetasActivity.class);
            intent.putExtra("descricao", descricao);
            intent.putExtra("valor", valor);
            intent.putExtra("data", data);
            intent.putExtra("prazo", prazo);
            Log.d("ListaMetas", "Abrindo meta: " + descricao + ", " + valor + ", " + prazo + ", " + data);
            startActivity(intent);
        });

        container.addView(card, params);
    }
}