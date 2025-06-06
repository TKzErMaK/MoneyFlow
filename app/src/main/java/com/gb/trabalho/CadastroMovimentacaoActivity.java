package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CadastroMovimentacaoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_movimentacao);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Receita/Despesa");

        Intent intent = getIntent();
        if (intent != null) {
            String valor = intent.getStringExtra("valor");
            String descricao = intent.getStringExtra("descricao");
            String data = intent.getStringExtra("data");
            int tipo = intent.getIntExtra("tipo", 0);

            EditText edtValor = findViewById(R.id.txt_valor);
            EditText edtDescricao = findViewById(R.id.txt_descricao);
            EditText edtData = findViewById(R.id.txt_data);
            RadioButton rbReceita = findViewById(R.id.rb_receita);
            RadioButton rbDespesa = findViewById(R.id.rb_despesa);

            if (tipo == 1) {
                rbReceita.setChecked(true);
            } else {
                rbDespesa.setChecked(true);
            }

            edtValor.setText(valor);
            edtDescricao.setText(descricao);
            edtData.setText(data);
        }
    }
}