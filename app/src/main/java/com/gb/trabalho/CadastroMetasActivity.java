package com.gb.trabalho;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import com.gb.trabalho.DAO.MetaDAO;
import com.gb.trabalho.Domain.Meta;
import com.gb.trabalho.Helper.FormatacaoDataHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class CadastroMetasActivity extends BaseActivity {

    private TextInputEditText edtDescription, edtValue, edtStartDate, edtDeadline;
    private RadioGroup radioGroup;
    private boolean isEdit = false;
    private int metaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityContent(R.layout.cadastro_metas);
        TextView txtTitle = findViewById(R.id.txt_titulo);
        txtTitle.setText("Meta");

        edtDescription = findViewById(R.id.edt_description);
        edtValue = findViewById(R.id.edt_value);
        edtStartDate = findViewById(R.id.edt_start_date);
        edtStartDate.addTextChangedListener(new FormatacaoDataHelper(edtStartDate));
        edtDeadline = findViewById(R.id.edt_deadline);
        radioGroup = findViewById(R.id.radio_group);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        if (intent != null) {
            metaId = intent.getIntExtra("id", -1);
            String descricao = intent.getStringExtra("descricao");
            String valor = intent.getStringExtra("valor");
            String data = intent.getStringExtra("data");
            int prazo = intent.getIntExtra("prazo", 0);
            boolean isReceita = intent.getBooleanExtra("isReceita", true);

            if (descricao != null) edtDescription.setText(descricao);
            if (valor != null) edtValue.setText(valor);
            if (data != null) edtStartDate.setText(data);
            if (prazo > 0) edtDeadline.setText(String.valueOf(prazo));
            radioGroup.check(isReceita ? R.id.rb_income : R.id.rb_expense);

            isEdit = metaId != -1;

            if (isEdit) {
                btnDelete.setVisibility(Button.VISIBLE);
            }
        }

        btnDelete.setOnClickListener(v -> confirmarExclusao());

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

        if (!valorStr.matches("^\\d+(\\.\\d{1,2})?$")) {
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
        sdf.setLenient(false);

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
                edtDeadline.setError("Prazo deve ser zero ou maior");
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
        String valorTexto = Objects.requireNonNull(edtValue.getText()).toString().replace(",", ".");
        double valor = Double.parseDouble(valorTexto);
        String data = Objects.requireNonNull(edtStartDate.getText()).toString();
        int prazo = Integer.parseInt(Objects.requireNonNull(edtDeadline.getText()).toString());
        int tipo = radioGroup.getCheckedRadioButtonId() == R.id.rb_income ? 1 : 0;

        Meta meta = new Meta(descricao, valor, data, prazo, tipo);

        MetaDAO metaDAO = new MetaDAO(this);

        if (isEdit) {
            meta.setId(metaId);
            metaDAO.alterar(meta);
            Toast.makeText(this, "Meta atualizada com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            metaDAO.inserir(meta);
            Toast.makeText(this, "Meta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void confirmarExclusao() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja deletar esta meta?")
                .setPositiveButton("Sim", (dialog, which) -> deletarMeta())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deletarMeta() {
        if (metaId != -1) {
            MetaDAO metaDAO = new MetaDAO(this);
            int deletado = metaDAO.deletar(metaId);
            if (deletado > 0) {
                Toast.makeText(this, "Meta deletada com sucesso!", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();  // volta para a lista
            } else {
                Toast.makeText(this, "Erro ao deletar a meta.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
