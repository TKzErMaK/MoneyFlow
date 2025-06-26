package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.gb.trabalho.DAO.InvestimentoDAO;
import com.gb.trabalho.Domain.Investimento;

public class CadastroInvestimentoActivity extends BaseActivity {

    EditText edtDescricao;
    EditText edtValor;
    EditText edtDataInicio;
    EditText edtRentabilidade;
    EditText edtFrequencia;
    Button btnGravar;

    private Investimento investimento;
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

        investimentoDAO = new InvestimentoDAO(this);

        // Verifica se esta Activity foi iniciada para editar uma lista existente
        long idInvestimento = getIntent().getLongExtra("id_investimento", -1);

        if (idInvestimento != -1){

        }

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
/*
            // Criar um novo investimento
            if (investimento.getId() == 0){ // Novo investimento
                investimento = new Investimento(descricao, valor, data_inicio, rentabilidade, frequencia);
                long idInvestimento = investimentoDAO.inserir(investimento);

                Toast.makeText(this, "Investimento salvo!", Toast.LENGTH_SHORT).show();
            }else { // Investimento existente (atualizar)
                investimento.setDescricao(descricao);
                investimento.setValor(valor);
                investimento.setDataInicio(data_inicio);
                investimento.setPercentualRentabilidade(rentabilidade);
                investimento.setFrequencia(frequencia);
                investimentoDAO.alterar(investimento);

                Toast.makeText(this, "Investimento atualizado!", Toast.LENGTH_SHORT).show();
            }
*/
            // Volta para MainActivity
            Intent intent = new Intent(CadastroInvestimentoActivity.this, ListaInvestimentosActivity.class);
            // Limpar a pilha de atividades
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Fechar a activity atual
            finish();

        } catch (Exception e) {
            return;
        }

    }


}