package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gb.trabalho.DAO.InvestimentoDAO;
import com.gb.trabalho.Domain.Investimento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CadastroInvestimentoActivity extends BaseActivity {

    EditText edtDescricao;
    EditText edtValor;
    EditText edtDataInicio;
    EditText edtRentabilidade;
    EditText edtFrequencia;
    Button btnGravar;

    private InvestimentoDAO investimentoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_investimentos);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Investimento");

        // Iniciar os campos de texto do layout
        edtDescricao = findViewById(R.id.txt_descricao);
        edtValor = findViewById(R.id.txt_valor);
        edtDataInicio = findViewById(R.id.txt_data);
        edtRentabilidade = findViewById(R.id.txt_rentabilidade);
        edtFrequencia = findViewById(R.id.txt_frequencia);
/*
        Intent intent = getIntent();
        if (intent != null) {
            String descricao = intent.getStringExtra("descricao");
            String valor = intent.getStringExtra("valor");
            String data_inicio = intent.getStringExtra("data_inicio");
            String rentabilidade = intent.getStringExtra("rentabilidade");
            String frequencia = intent.getStringExtra("frequencia");

            edtValor.setText(valor);
            edtDescricao.setText(descricao);
            edtDataInicio.setText(data_inicio);
            edtRentabilidade.setText(rentabilidade);
            edtFrequencia.setText(frequencia);
        }
*/
        // Iniciar o DAO para salvar os dados
        btnGravar = findViewById(R.id.btn_gravar);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarInvestimento();
            }
        });

    }

    // Metodo para salvar os dados do investimento
    private void SalvarInvestimento() {
        // Obter os valores dos campos
        String descricao = edtDescricao.getText().toString();
        String strValor = edtValor.getText().toString();
        String data_inicio = edtDataInicio.getText().toString();
        String strRentabilidade = edtRentabilidade.getText().toString();
        String frequencia = edtFrequencia.getText().toString();

        // Validar os campos
        if (descricao.isEmpty()){
            edtDescricao.setError("Informe uma descrição");
            return;
        }
        if (strValor.isEmpty()){
            edtValor.setError("Informe o valor");
            return;
        }
        if (data_inicio.isEmpty()){
            edtDataInicio.setError("Informe a data de início");
            return;
        }
        if (strRentabilidade.isEmpty()){
            edtRentabilidade.setError("Informe a rentabilidade");
            return;
        }
        if (frequencia.isEmpty()){
            edtFrequencia.setError("Informe a frequência");
            return;
        }

        try {
            double valor = Double.parseDouble(strValor);
            float rentabilidade = Float.parseFloat(strRentabilidade);

            // Criar um novo investimento
            Investimento novoInvestimento = new Investimento(0, descricao, valor, data_inicio, rentabilidade, frequencia);

        } catch (Exception e) {
            return;
        }

    }


}