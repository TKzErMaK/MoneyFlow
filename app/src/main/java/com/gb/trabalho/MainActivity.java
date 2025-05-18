package com.gb.trabalho;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    Button btnmovimentacao, btnextrato, btninvestimentos, btnnotificacoes, btnmetas;
    Intent intent;
    TextView txtinvestimentos, txtsaldo;
    double totalInvestimentos = 0, saldo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.main);
        CarregarGrafico();
        CarregaSaldo();

        txtsaldo = findViewById(R.id.txt_saldo);
        txtinvestimentos = findViewById(R.id.txt_investimentos);
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        txtsaldo.setText(moeda.format(saldo));
        txtinvestimentos.setText(moeda.format(totalInvestimentos));

        btnmovimentacao = findViewById(R.id.btn_movimentacao);
        btnmovimentacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CadastroMovimentacaoActivity.class);
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
    private void CarregaSaldo() {
        // consulta o saldo do extrato no mês corrente
        saldo = 658.47;
    }

    private void CarregarGrafico() {
        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(81596.00f, "CDB"));
        entries.add(new PieEntry(31256.00f, "Ações"));
        entries.add(new PieEntry(253416.00f, "Imóveis"));
        entries.add(new PieEntry(5527.00f, "Espécie"));

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
}