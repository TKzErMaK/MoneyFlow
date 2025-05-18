package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CadastroInvestimentoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_investimentos);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Investimento");

        Intent intent = getIntent();
        if (intent != null) {
            String valor = intent.getStringExtra("valor");
            String descricao = intent.getStringExtra("descricao");
            String data = intent.getStringExtra("data");
            int tipo = intent.getIntExtra("tipo", 0);

            EditText edtValor = findViewById(R.id.txt_valor);
            EditText edtDescricao = findViewById(R.id.txt_descricao);
            EditText edtData = findViewById(R.id.txt_data);

            edtValor.setText(valor);
            edtDescricao.setText(descricao);
            edtData.setText(data);
        }
    }
}