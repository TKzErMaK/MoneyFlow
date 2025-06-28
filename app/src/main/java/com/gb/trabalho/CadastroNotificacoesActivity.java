package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.gb.trabalho.DAO.NotificacaoDAO;
import com.gb.trabalho.Domain.Notificacao;

import java.util.Date;

public class CadastroNotificacoesActivity extends BaseActivity {

    EditText edtValor, edtDescricao, edtPrazo;
    RadioButton rbValor, rbTempo;
    Button btnSalvar, btnExcluir;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_notificacoes);

        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Notificação");

        edtValor = findViewById(R.id.txt_valor);
        edtDescricao = findViewById(R.id.txt_descricao);
        edtPrazo = findViewById(R.id.txt_prazo);
        rbValor = findViewById(R.id.rb_valor);
        rbTempo = findViewById(R.id.rb_tempo);
        btnSalvar = findViewById(R.id.btn_gravar);
        btnExcluir = findViewById(R.id.btn_excluir);

        Intent intent = getIntent();
        if (intent != null) {
            String valor = intent.getStringExtra("valor");
            String descricao = intent.getStringExtra("descricao");
            int prazo = intent.getIntExtra("prazo", 0);
            int tipo = intent.getIntExtra("tipo", 0);
            id = intent.getIntExtra("id", 0);

            edtValor.setText(valor);
            edtDescricao.setText(descricao);
            edtPrazo.setText(String.valueOf(prazo));

            if (tipo == 1) {
                rbValor.setChecked(true);
            } else {
                rbTempo.setChecked(true);
            }
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarNotificacao();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirNotificacao();
            }
        });
    }

    private void salvarNotificacao() {
        String valorStr = edtValor.getText().toString();
        String descricao = edtDescricao.getText().toString();
        String prazoStr = edtPrazo.getText().toString();

        double valor = valorStr.isEmpty() ? 0.0 : converteValor(valorStr);
        int prazo = prazoStr.isEmpty() ? 0 : Integer.parseInt(prazoStr);
        int tipo = rbValor.isChecked() ? 1 : 0;

        if (descricao.isEmpty()) {
            Toast.makeText(this, "Descrição não informada", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valor == 0 && prazo == 0) {
            Toast.makeText(this, "Informe o valor ou o prazo", Toast.LENGTH_SHORT).show();
            return;
        }

        Notificacao notificacao = new Notificacao();
        notificacao.setDescricao(descricao);
        notificacao.setValor(valor);
        notificacao.setPrazo(prazo);
        notificacao.setTipo(tipo);
        notificacao.setDataInicio(DefineData(prazo));
        notificacao.setId(id);

        NotificacaoDAO dao = new NotificacaoDAO(this);
        long resultado;

        if (id > 0) {
            resultado = dao.alterar(notificacao);
            if (resultado > 0) {
                Toast.makeText(this, "Notificação alterada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao alterar notificação", Toast.LENGTH_SHORT).show();
            }
        } else {
            resultado = dao.inserir(notificacao);
            if (resultado != -1) {
                Toast.makeText(this, "Notificação salva com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar notificação", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(CadastroNotificacoesActivity.this, ListaNotificacoesActivity.class);
        startActivity(intent);
        finish();
    }

    private void excluirNotificacao() {
        if (id > 0) {
            NotificacaoDAO dao = new NotificacaoDAO(this);
            long resultado = dao.deletar(id);
            if (resultado > 0) {
                Toast.makeText(this, "Notificação excluída com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao excluir notificação", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(CadastroNotificacoesActivity.this, ListaNotificacoesActivity.class);
        startActivity(intent);
        finish();
    }

    private Date DefineData(int prazo) {
        Date currentData = new Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(currentData);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, prazo);
        return calendar.getTime();
    }

    private double converteValor(String valorStr) {
        try {
            return Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
