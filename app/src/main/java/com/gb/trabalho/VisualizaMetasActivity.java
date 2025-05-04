package com.gb.trabalho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class VisualizaMetasActivity extends AppCompatActivity {

    private static final String TAG = "VisualizaMetasActivity";

    private String descricao, valor, data;
    private int prazo;
    private boolean isReceita;

    private ActivityResultLauncher<Intent> editMetaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualiza_metas);

        ConstraintLayout topBar = findViewById(R.id.top_bar_metas);
        TextView titulo = topBar.findViewById(R.id.txt_title);
        titulo.setText(R.string.txt_dynamic_title_goal);

        TextView txtGoalTitle = findViewById(R.id.txt_goal_title);
        TextView txtGoalValue = findViewById(R.id.txt_goal_value);
        TextView txtGoalDeadline = findViewById(R.id.txt_goal_deadline);
        TextView txtGoalDaysRemaining = findViewById(R.id.txt_goal_days_remaining);
        TextView txtProgressValue = findViewById(R.id.txt_progress_value);
        CircularProgressIndicator progressIndicator = findViewById(R.id.progress_goal);

        // Recuperar dados recebidos pela Intent
        descricao = getIntent().getStringExtra("descricao");
        valor = getIntent().getStringExtra("valor");
        prazo = getIntent().getIntExtra("prazo", 0);
        data = getIntent().getStringExtra("data");
        isReceita = getIntent().getBooleanExtra("isReceita", true);

        editMetaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent dataIntent = result.getData();
                        descricao = dataIntent.getStringExtra("descricao");
                        valor = dataIntent.getStringExtra("valor");
                        prazo = dataIntent.getIntExtra("prazo", 0);
                        data = dataIntent.getStringExtra("data");
                        isReceita = dataIntent.getBooleanExtra("isReceita", true);
                        recreate(); // Recarrega a tela com os novos dados
                    }
                }
        );

        // Preenche os campos da tela
        if (descricao != null && !descricao.trim().isEmpty()) {
            txtGoalTitle.setText(descricao);
        } else {
            txtGoalTitle.setText(R.string.descricao_indefinida);
        }

        if (valor != null && !valor.trim().isEmpty()) {
            txtGoalValue.setText(getString(R.string.formatted_value, valor));
        } else {
            txtGoalValue.setText(R.string.valor_indefinido);
        }

        if (data != null && !data.trim().isEmpty()) {
            int diasRestantes = calcularDiasRestantes(data, prazo);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date dataInicial = sdf.parse(data);
                if (dataInicial != null) {
                    long dataFinalMillis = dataInicial.getTime() + (long) prazo * 24 * 60 * 60 * 1000;
                    Date dataFinal = new Date(dataFinalMillis);
                    String dataFinalFormatada = sdf.format(dataFinal);
                    txtGoalDeadline.setText(dataFinalFormatada);
                } else {
                    txtGoalDeadline.setText(R.string.data_indefinida);
                }
            } catch (ParseException e) {
                Log.e(TAG, "Erro ao calcular data final: " + data, e);
                txtGoalDeadline.setText(R.string.data_indefinida);
            }

            if (diasRestantes >= 0) {
                txtGoalDaysRemaining.setText(getString(R.string.days_remaining_format, diasRestantes));
            } else {
                txtGoalDaysRemaining.setText(R.string.expired_deadline);
            }
        } else {
            txtGoalDeadline.setText(R.string.data_indefinida);
            txtGoalDaysRemaining.setText(R.string.data_indefinida);
        }

        // Simulação de progresso
        int progresso = 0;
        txtProgressValue.setText(String.format(Locale.getDefault(), getString(R.string.progress_format), progresso));
        progressIndicator.setProgress(progresso);

        // Botão de editar
        Button btnEditGoal = findViewById(R.id.btn_edit_goal);
        btnEditGoal.setOnClickListener(v -> {
            Intent intent = new Intent(VisualizaMetasActivity.this, CadastroMetasActivity.class);
            intent.putExtra("descricao", descricao);
            if (valor != null) {
                String valorLimpo = valor.replace("R$", "").replaceAll("\\s+", "").replace(",", ".");
                intent.putExtra("valor", valorLimpo);
            }
            intent.putExtra("data", data);
            intent.putExtra("prazo", prazo);
            intent.putExtra("isReceita", isReceita);

            editMetaLauncher.launch(intent);
        });
    }

    private int calcularDiasRestantes(String dataInicio, int prazo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date hoje = new Date();
            Date dataInicial = sdf.parse(dataInicio);
            long dataFinalMillis = Objects.requireNonNull(dataInicial).getTime() + (long) prazo * 24 * 60 * 60 * 1000;
            long diff = dataFinalMillis - hoje.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            Log.e(TAG, "Erro ao converter data: " + dataInicio, e);
            return -1;
        }
    }
}
