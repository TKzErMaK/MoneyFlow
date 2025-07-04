package com.gb.trabalho;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import com.gb.trabalho.Adapter.InvestimentoAdapter;
import com.gb.trabalho.DAO.InvestimentoDAO;
import com.gb.trabalho.DAO.MovimentacaoDAO;
import com.gb.trabalho.DAO.NotificacaoDAO;
import com.gb.trabalho.Domain.Investimento;
import com.gb.trabalho.Domain.Notificacao;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    Button btnmovimentacao, btnindicadores, btnextrato, btninvestimentos, btnnotificacoes, btnmetas;
    Intent intent;
    TextView txtinvestimentos, txtsaldo;
    double totalInvestimentos = 0, saldo = 0;
    InvestimentoDAO investimentoDAO;
    MovimentacaoDAO movimentacaoDAO;
    NotificacaoDAO notificacaoDAO;
    InvestimentoAdapter investimentoAdapter;
    SimpleDateFormat dataString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.main);

        notificacaoDAO = new NotificacaoDAO(this);
        notificacaoDAO.deletarNotificacoesVencidas();
        exibirNotificacao();

        txtsaldo = findViewById(R.id.txt_saldo);
        txtinvestimentos = findViewById(R.id.txt_investimentos);
        btnmovimentacao = findViewById(R.id.btn_movimentacao);
        btnmovimentacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CadastroMovimentacaoActivity.class);
                startActivity(intent);
            }
        });

        btnindicadores = findViewById(R.id.btn_indicadores);
        btnindicadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, IndicadoresActivity.class);
                startActivity(intent);
            }
        });

        btnextrato = findViewById(R.id.btn_extrato);
        btnextrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, ExtratoActivity.class);
                startActivity(intent);
            }
        });

        btninvestimentos = findViewById(R.id.btn_investimentos);
        btninvestimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, ListaInvestimentosActivity.class);
                startActivity(intent);
            }
        });

        btnmetas = findViewById(R.id.btn_metas);
        btnmetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, ListaMetasActivity.class);
                startActivity(intent);
            }
        });

        btnnotificacoes = findViewById(R.id.btn_notificacoes);
        btnnotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, ListaNotificacoesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CarregarGrafico();
        CarregaSaldo();
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtsaldo.setText(moeda.format(saldo));
        txtinvestimentos.setText(moeda.format(totalInvestimentos));
    }

    private void CarregaSaldo() {
        movimentacaoDAO = new MovimentacaoDAO(this);
        saldo = movimentacaoDAO.buscarSaldo();
    }

    private void CarregarGrafico() {

        investimentoDAO = new InvestimentoDAO(this);

        List<Investimento> investimentos = investimentoDAO.listarTodos();

        investimentoAdapter = new InvestimentoAdapter();

        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> entries = investimentoAdapter.converterParaPieEntries(investimentos);
        for (PieEntry entry : entries) {
            totalInvestimentos += entry.getValue();
        }

        List<Integer> colors = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String colorName = "color_" + i;
            int colorId = getResources().getIdentifier(colorName, "color", getPackageName());
            if (colorId != 0) {
                colors.add(ContextCompat.getColor(this, colorId));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(10f);

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();
    }

    private void exibirNotificacao() {
        NotificacaoDAO notificacaodao = new NotificacaoDAO(this);
        List<Notificacao> notificacoes = notificacaodao.listarTodos();
        Date hoje = new Date();
        Date dataVencimento;

        for (Notificacao notificacao : notificacoes) {
            if (notificacao.getTipo() == 0 && notificacao.getDataVencimento() != null) {
                dataVencimento = notificacao.getDataVencimento();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String hojeFormatado = sdf.format(hoje);
                String vencimentoFormatado = sdf.format(dataVencimento);

                if (hojeFormatado.equals(vencimentoFormatado)) {
                    mostrarDialogoNotificacao(notificacao.getDescricao());
                    return;
                }
            }
        }
    }

    private void mostrarDialogoNotificacao(String mensagem) {
        new AlertDialog.Builder(this)
                .setTitle("Notificação")
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}