package com.gb.trabalho;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

        String descricao = getIntent().getStringExtra("descricao");
        String valor = getIntent().getStringExtra("valor");
        String data = getIntent().getStringExtra("data");

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

        // Data e dias restantes
        if (data != null && !data.trim().isEmpty()) {
            txtGoalDeadline.setText(data);
            int diasRestantes = calcularDiasRestantes(data);
            if (diasRestantes >= 0) {
                txtGoalDaysRemaining.setText(getString(R.string.days_remaining_format, diasRestantes));
            } else {
                txtGoalDaysRemaining.setText(getString(R.string.expired_deadline));
            }
        } else {
            txtGoalDeadline.setText(R.string.data_indefinida);
            txtGoalDaysRemaining.setText(R.string.data_indefinida);
        }

        // Progresso fixo (temporário)
        int progresso = 0;
        txtProgressValue.setText(String.format(Locale.getDefault(), getString(R.string.progress_format), progresso));
        progressIndicator.setProgress(progresso);
    }

    private int calcularDiasRestantes(String dataFinal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date hoje = new Date();
            Date dataMeta = sdf.parse(dataFinal);
            long diff = Objects.requireNonNull(dataMeta).getTime() - hoje.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            Log.e(TAG, "Erro ao converter data: " + dataFinal, e);
            return -1;
        }
    }
}
