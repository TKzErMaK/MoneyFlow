package com.gb.trabalho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.gb.trabalho.DAO.MetaDAO;
import com.gb.trabalho.Domain.Meta;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class VisualizaMetasActivity extends BaseActivity {

    private static final String TAG = "VisualizaMetasActivity";
    private Meta meta;
    private int metaId;

    private ActivityResultLauncher<Intent> editMetaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.visualiza_metas);
        TextView titulo = findViewById(R.id.txt_titulo);
        titulo.setText(R.string.txt_dynamic_title_goal);

        TextView txtGoalTitle = findViewById(R.id.txt_goal_title);
        TextView txtGoalValue = findViewById(R.id.txt_goal_value);
        TextView txtGoalDeadline = findViewById(R.id.txt_goal_deadline);
        TextView txtGoalDaysRemaining = findViewById(R.id.txt_goal_days_remaining);
        TextView txtProgressValue = findViewById(R.id.txt_progress_value);
        CircularProgressIndicator progressIndicator = findViewById(R.id.progress_goal);

        // Recebe o ID da meta selecionada
        metaId = getIntent().getIntExtra("id", -1);
        if (metaId == -1) {
            Log.e(TAG, "ID da meta não foi recebido.");
            finish();
            return;
        }

        // Buscar meta no banco
        MetaDAO metaDAO = new MetaDAO(this);
        meta = metaDAO.buscarPorId(metaId);

        if (meta == null) {
            Log.e(TAG, "Meta não encontrada no banco de dados.");
            finish();
            return;
        }

        editMetaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Recarrega meta atualizada do banco
                        meta = metaDAO.buscarPorId(metaId);
                        preencherCampos(txtGoalTitle, txtGoalValue, txtGoalDeadline, txtGoalDaysRemaining, txtProgressValue, progressIndicator);
                    }
                }
        );

        preencherCampos(txtGoalTitle, txtGoalValue, txtGoalDeadline, txtGoalDaysRemaining, txtProgressValue, progressIndicator);

        // Botão de editar
        Button btnEditGoal = findViewById(R.id.btn_edit_goal);
        btnEditGoal.setOnClickListener(v -> {
            Intent intent = new Intent(VisualizaMetasActivity.this, CadastroMetasActivity.class);
            intent.putExtra("id", meta.getId());
            intent.putExtra("descricao", meta.getDescricao());
            intent.putExtra("valor", String.valueOf(meta.getValor()));
            intent.putExtra("data", meta.getDataInicio());
            intent.putExtra("prazo", meta.getPrazo());
            intent.putExtra("isReceita", meta.getTipo() == 1);
            editMetaLauncher.launch(intent);
        });
    }

    private void preencherCampos(TextView titulo, TextView valor, TextView deadline, TextView diasRestantes, TextView progressoTexto, CircularProgressIndicator progresso) {
        titulo.setText(meta.getDescricao());
        valor.setText(getString(R.string.formatted_value, String.format(Locale.getDefault(), "%.2f", meta.getValor())));

        String dataInicio = meta.getDataInicio();
        int prazo = meta.getPrazo();

        if (dataInicio != null && !dataInicio.trim().isEmpty()) {
            int dias = calcularDiasRestantes(dataInicio, prazo);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date dataInicial = sdf.parse(dataInicio);
                if (dataInicial != null) {
                    long dataFinalMillis = dataInicial.getTime() + (long) prazo * 24 * 60 * 60 * 1000;
                    Date dataFinal = new Date(dataFinalMillis);
                    String dataFinalFormatada = sdf.format(dataFinal);
                    deadline.setText(dataFinalFormatada);
                } else {
                    deadline.setText(R.string.data_indefinida);
                }
            } catch (ParseException e) {
                Log.e(TAG, "Erro ao calcular data final: " + dataInicio, e);
                deadline.setText(R.string.data_indefinida);
            }

            if (dias >= 0) {
                diasRestantes.setText(getString(R.string.days_remaining_format, dias));
            } else {
                diasRestantes.setText(R.string.expired_deadline);
            }
        } else {
            deadline.setText(R.string.data_indefinida);
            diasRestantes.setText(R.string.data_indefinida);
        }

        // Simulação de progresso (valor fixo como placeholder)
        int progressoAtual = 0;
        progressoTexto.setText(String.format(Locale.getDefault(), getString(R.string.progress_format), progressoAtual));
        progresso.setProgress(progressoAtual);
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
