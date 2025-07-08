package com.gb.trabalho;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;

import com.gb.trabalho.API.IndicadoresAPI;

public class IndicadoresActivity extends BaseActivity {
    EditText edtSELIC, edtIPCA, edtCDI;
    ProgressBar progressBar;
    int respostasRecebidas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.activity_indicadores);

        edtSELIC = findViewById(R.id.txt_selic);
        edtIPCA = findViewById(R.id.txt_ipca);
        edtCDI = findViewById(R.id.txt_cdi);
        progressBar = findViewById(R.id.progress_bar);

        edtSELIC.setVisibility(View.INVISIBLE);
        edtIPCA.setVisibility(View.INVISIBLE);
        edtCDI.setVisibility(View.INVISIBLE);

        IndicadoresAPI api = new IndicadoresAPI();

        api.buscarIPCA(new IndicadoresAPI.ResultadoListener<Double>() {
            @Override
            public void onResultado(Double valor) {
                runOnUiThread(() -> {
                    edtIPCA.setText(valor.toString());
                    verificarConclusao();
                });
            }

            @Override
            public void onErro(String mensagem) {
                verificarConclusao();
            }
        });

        api.buscarCDI(new IndicadoresAPI.ResultadoListener<Double>() {
            @Override
            public void onResultado(Double valor) {
                runOnUiThread(() -> {
                    edtCDI.setText(valor.toString());
                    verificarConclusao();
                });
            }

            @Override
            public void onErro(String mensagem) {
                verificarConclusao();
            }
        });

        api.buscarSELIC(new IndicadoresAPI.ResultadoListener<Double>() {
            @Override
            public void onResultado(Double valor) {
                runOnUiThread(() -> {
                    edtSELIC.setText(valor.toString());
                    verificarConclusao();
                });
            }

            @Override
            public void onErro(String mensagem) {
                verificarConclusao();
            }
        });
    }

    private void verificarConclusao() {
        respostasRecebidas++;
        if (respostasRecebidas == 3) {
            progressBar.setVisibility(View.GONE);
            edtSELIC.setVisibility(View.VISIBLE);
            edtIPCA.setVisibility(View.VISIBLE);
            edtCDI.setVisibility(View.VISIBLE);
        }
    }
}
