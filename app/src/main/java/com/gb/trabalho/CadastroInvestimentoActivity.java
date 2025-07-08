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
import com.gb.trabalho.Helper.FormatacaoDataHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastroInvestimentoActivity extends BaseActivity {

    EditText edtDescricao, edtValor, edtDataInicio, edtRentabilidade, edtFrequencia;
    Button btnSalvar, btnExcluir;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_investimentos);

        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Investimento");

        edtDescricao = findViewById(R.id.txt_descricao);
        edtValor = findViewById(R.id.txt_valor);
        edtDataInicio = findViewById(R.id.txt_data_inicio);
        edtDataInicio.addTextChangedListener(new FormatacaoDataHelper(edtDataInicio));
        edtRentabilidade = findViewById(R.id.txt_rentabilidade);
        btnSalvar = findViewById(R.id.btn_gravar);
        btnExcluir = findViewById(R.id.btn_deletar);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            double valor = intent.getDoubleExtra("valor", 0);
            String descricao = intent.getStringExtra("descricao");
            String datainicio = intent.getStringExtra("datainicio");
            double percentualrentabilidade = intent.getDoubleExtra("percentualrentabilidade", 0);
            if (valor == 0) {
                edtValor.setText("");
            } else {
                edtValor.setText(String.valueOf(valor));
            }

            edtValor.setText(String.valueOf(valor));
            edtDescricao.setText(descricao);
            edtDataInicio.setText(datainicio);
            edtRentabilidade.setText((String.valueOf(percentualrentabilidade)));
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarInvestimento();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirInvestimento();
            }
        });
    }

    private void salvarInvestimento() {
        double valor = Double.parseDouble(edtValor.getText().toString());
        String descricao = edtDescricao.getText().toString();
        String dataStr = edtDataInicio.getText().toString();
        Date data;
        double percentualrentabilidade = Double.parseDouble(edtRentabilidade.getText().toString());

        if (descricao.isEmpty()) {
            Toast.makeText(this, "Descrição não informada", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valor == 0) {
            Toast.makeText(this, "Valor não informado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (percentualrentabilidade == 0) {
            Toast.makeText(this, "Rentabilidade não informada", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat dataString = new SimpleDateFormat("dd/MM/yyyy");
            dataString.setLenient(false);
            data = dataString.parse(dataStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Data inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Investimento investimento = new Investimento();
        investimento.setDescricao(descricao);
        investimento.setValor(valor);
        investimento.setDataInicio(data);
        investimento.setId(id);
        investimento.setPercentualRentabilidade(percentualrentabilidade);

        InvestimentoDAO dao = new InvestimentoDAO(this);
        long resultado;

        if (id > 0) {
            resultado = dao.alterar(investimento);
            if (resultado > 0) {
                Toast.makeText(this, "Investimento alterada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao alterar investimento", Toast.LENGTH_SHORT).show();
            }
        } else {
            resultado = dao.inserir(investimento);
            if (resultado != -1) {
                Toast.makeText(this, "Investimento salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar investimento", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(CadastroInvestimentoActivity.this, ListaInvestimentosActivity.class);
        startActivity(intent);
        finish();
    }

    private void excluirInvestimento() {
        if (id > 0) {
            InvestimentoDAO dao = new InvestimentoDAO(this);
            long resultado = dao.deletar(id);
            if (resultado > 0) {
                Toast.makeText(this, "Investimento excluído com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao excluir investimento", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(CadastroInvestimentoActivity.this, ListaInvestimentosActivity.class);
        startActivity(intent);
        finish();
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