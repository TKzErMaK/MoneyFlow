package com.gb.trabalho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CadastroMetasActivity extends AppCompatActivity {

    private TextInputEditText edtDescription, edtValue, edtStartDate, edtDeadline;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_metas);

        ConstraintLayout topBar = findViewById(R.id.top_bar_metas);
        TextView titulo = topBar.findViewById(R.id.txt_title);
        titulo.setText(R.string.txt_dynamic_title_goal);

        edtDescription = findViewById(R.id.edt_description);
        edtValue = findViewById(R.id.edt_value);
        edtStartDate = findViewById(R.id.edt_start_date);
        edtDeadline = findViewById(R.id.edt_deadline);
        radioGroup = findViewById(R.id.radio_group);
        Button btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        if (intent != null) {
            String descricao = intent.getStringExtra("descricao");
            String valor = intent.getStringExtra("valor");
            String data = intent.getStringExtra("data");
            int prazo = intent.getIntExtra("prazo", 0);

            if (descricao != null) edtDescription.setText(descricao);
            if (valor != null) edtValue.setText(valor);
            if (data != null) edtStartDate.setText(data);
            if (prazo > 0) edtDeadline.setText(String.valueOf(prazo));
        }

        btnSave.setOnClickListener(view -> {
            if (validarCampos()) {
                salvarMeta();
            }
        });
    }

    private boolean validarCampos() {
        String descricao = Objects.requireNonNull(edtDescription.getText()).toString().trim();
        String valorStr = Objects.requireNonNull(edtValue.getText()).toString().trim();
        String dataInicio = Objects.requireNonNull(edtStartDate.getText()).toString().trim();
        String prazoStr = Objects.requireNonNull(edtDeadline.getText()).toString().trim();

        if (descricao.isEmpty()) {
            edtDescription.setError("Descrição obrigatória");
            edtDescription.requestFocus();
            return false;
        }

        if (valorStr.isEmpty()) {
            edtValue.setError("Valor obrigatório");
            edtValue.requestFocus();
            return false;
        }

        if (!valorStr.matches("^\\d+(.\\d{1,2})?$")) {
            edtValue.setError("Valor inválido. Ex: 1000.00");
            edtValue.requestFocus();
            return false;
        }

        if (dataInicio.isEmpty()) {
            edtStartDate.setError("Data de início obrigatória");
            edtStartDate.requestFocus();
            return false;
        }

        if (dataInicio.length() != 10 || !dataInicio.matches("\\d{2}/\\d{2}/\\d{4}")) {
            edtStartDate.setError("Formato de data inválido. Use dd/MM/yyyy");
            edtStartDate.requestFocus();
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false); // Não permite datas inválidas como 31/02

        try {
            sdf.parse(dataInicio);

            String[] partes = dataInicio.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            if (dia <= 0 || mes <= 0 || mes > 12 || ano <= 0) {
                edtStartDate.setError("Data inválida");
                edtStartDate.requestFocus();
                return false;
            }

        } catch (ParseException e) {
            edtStartDate.setError("Data inválida");
            edtStartDate.requestFocus();
            return false;
        }


        if (prazoStr.isEmpty()) {
            edtDeadline.setError("Prazo obrigatório");
            edtDeadline.requestFocus();
            return false;
        }

        try {
            int prazo = Integer.parseInt(prazoStr);
            if (prazo < 0) {
                edtDeadline.setError("Prazo deve ser zero ou ma00ior");
                edtDeadline.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            edtDeadline.setError("Prazo inválido");
            edtDeadline.requestFocus();
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione Receita ou Despesa", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void salvarMeta() {
        String descricao = Objects.requireNonNull(edtDescription.getText()).toString();
        String valorStr = Objects.requireNonNull(edtValue.getText()).toString();
        double valorDouble = Double.parseDouble(valorStr.replace(",", "."));
        String data = Objects.requireNonNull(edtStartDate.getText()).toString();
        int prazo = Integer.parseInt(Objects.requireNonNull(edtDeadline.getText()).toString());
        boolean isReceita = radioGroup.getCheckedRadioButtonId() == R.id.rb_income;

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = nf.format(valorDouble);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("descricao", descricao);
        resultIntent.putExtra("valor", valorFormatado);
        resultIntent.putExtra("data", data);
        resultIntent.putExtra("prazo", prazo);
        resultIntent.putExtra("isReceita", isReceita);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

        Toast.makeText(this, "Meta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
    }
}
