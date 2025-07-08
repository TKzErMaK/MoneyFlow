package com.gb.trabalho;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import androidx.activity.EdgeToEdge;
import com.gb.trabalho.DAO.MovimentacaoDAO;
import com.gb.trabalho.DAO.NotificacaoDAO;
import com.gb.trabalho.Domain.Movimentacao;
import com.gb.trabalho.Domain.Notificacao;
import com.gb.trabalho.Helper.FormatacaoDataHelper;

public class CadastroMovimentacaoActivity extends BaseActivity {

    EditText edtValor, edtDescricao, edtData;
    RadioButton rbReceita, rbDespesa;
    Button btnSalvar, btnExcluir;
    int id = 0;
    MovimentacaoDAO movimentacaodao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_movimentacao);

        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Receita/Despesa");

        movimentacaodao = new MovimentacaoDAO(this);

        edtValor = findViewById(R.id.txt_valor);
        edtDescricao = findViewById(R.id.txt_descricao);
        edtData = findViewById(R.id.txt_data);
        edtData.addTextChangedListener(new FormatacaoDataHelper(edtData));
        rbReceita = findViewById(R.id.rb_valor);
        rbDespesa = findViewById(R.id.rb_tempo);
        btnSalvar = findViewById(R.id.btn_gravar);
        btnExcluir = findViewById(R.id.btn_excluir);

        Intent intent = getIntent();
        if (intent != null) {
            double valor = intent.getDoubleExtra("valor",0);
            String descricao = intent.getStringExtra("descricao");
            String data = intent.getStringExtra("data");
            int tipo = intent.getIntExtra("tipo", 0);
            id = intent.getIntExtra("id", 0);

            edtValor.setText(String.valueOf(valor));
            edtDescricao.setText(descricao);
            edtData.setText(data);

            if (tipo == 1) {
                rbReceita.setChecked(true);
            } else {
                rbDespesa.setChecked(true);
            }
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarMovimentacao();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirMovimentacao();
            }
        });
    }

    private void salvarMovimentacao() {
        String descricao = edtDescricao.getText().toString();
        String dataStr = edtData.getText().toString();
        Date data;
        double valor = Double.parseDouble(edtValor.getText().toString());
        int tipo = rbReceita.isChecked() ? 1 : 0;

        try {
            SimpleDateFormat dataString = new SimpleDateFormat("dd/MM/yyyy");
            dataString.setLenient(false);
            data = dataString.parse(dataStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Data inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (descricao.isEmpty()) {
            Toast.makeText(this, "Descrição não informada", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valor == 0.0) {
            Toast.makeText(this, "Valor não informado ou inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Movimentacao mov = new Movimentacao();
        mov.setDescricao(descricao);
        mov.setValor(valor);
        mov.setData(data);
        mov.setTipo(tipo);
        mov.setId(id);

        MovimentacaoDAO dao = new MovimentacaoDAO(this);
        long resultado;

        if (id > 0) {
            resultado = dao.alterar(mov);
            if (resultado > 0) {
                Toast.makeText(this, "Movimentação alterada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao alterar movimentação", Toast.LENGTH_SHORT).show();
            }
        } else {
            resultado = dao.inserir(mov);
            if (resultado != -1) {
                Toast.makeText(this, "Movimentação salva com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar movimentação", Toast.LENGTH_SHORT).show();
            }
        }
        exibirNotificacao(mov);
    }

    private void excluirMovimentacao() {
        if (id > 0) {
            long resultado = movimentacaodao.deletar(id);
            if (resultado > 0) {
                Toast.makeText(this, "Movimentação excluída com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao excluir movimentação", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = new Intent(CadastroMovimentacaoActivity.this, ExtratoActivity.class);
        startActivity(intent);
        finish();
    }

    private void exibirNotificacao(Movimentacao mov) {
        double totalDespesas = 0;
        double totalReceitas = 0;
        NotificacaoDAO notificacaodao = new NotificacaoDAO(this);
        List<Notificacao> notificacoes = notificacaodao.listarTodos();

        for (Notificacao notificacao : notificacoes) {
            if (notificacao.getTipo() == 1) {

                String dataVencStr = notificacao.getDataVencimento() != null ? notificacao.getDataVencimento().toString() : "";
                totalReceitas = movimentacaodao.buscarReceitaPeriodo(dataVencStr);
                totalDespesas = movimentacaodao.buscarDespesaPeriodo(dataVencStr);

                if (totalReceitas >= notificacao.getValor() || totalDespesas >= notificacao.getValor()) {
                    mostrarDialogoNotificacao(notificacao.getDescricao());
                    return;
                }
            }
        }
        navegarParaExtrato();
    }

    private void mostrarDialogoNotificacao(String mensagem) {
        new AlertDialog.Builder(this)
                .setTitle("Notificação")
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> navegarParaExtrato())
                .show();
    }
    private void navegarParaExtrato() {
        Intent intent = new Intent(CadastroMovimentacaoActivity.this, ExtratoActivity.class);
        startActivity(intent);
        finish();
    }
}
