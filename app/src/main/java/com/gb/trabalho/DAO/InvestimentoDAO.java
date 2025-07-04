package com.gb.trabalho.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gb.trabalho.Domain.Notificacao;
import com.gb.trabalho.Helper.DatabaseHelper;
import com.gb.trabalho.Domain.Investimento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvestimentoDAO {
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public InvestimentoDAO(Context context) {
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    public long inserir(Investimento investimento) {
        ContentValues values = new ContentValues();
        values.put("descricao", investimento.getDescricao());
        values.put("valor", investimento.getValor());
        values.put("data_inicio", dateFormat.format(investimento.getDataInicio()));
        values.put("percentual_rentabilidade", investimento.getPercentualRentabilidade());
        return db.insert("investimento", null, values);
    }

    public int alterar(Investimento investimento) {
        ContentValues values = new ContentValues();
        values.put("descricao", investimento.getDescricao());
        values.put("valor", investimento.getValor());
        values.put("data_inicio", dateFormat.format(investimento.getDataInicio()));
        values.put("percentual_rentabilidade", investimento.getPercentualRentabilidade());
        return db.update("investimento", values, "id = ?", new String[]{String.valueOf(investimento.getId())});
    }
    public int deletar(int id) {
        return db.delete("investimento", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Investimento> listarTodos() {
        List<Investimento> lista = new ArrayList<>();
        Cursor cursor = db.query("investimento", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Investimento i = new Investimento();
            i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            i.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            i.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            i.setPercentualRentabilidade(cursor.getDouble(cursor.getColumnIndexOrThrow("percentual_rentabilidade")));
            try {
                i.setDataInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"))));
            } catch (Exception e) {
                i.setDataInicio(new Date());
            }
            lista.add(i);
        }
        cursor.close();
        return lista;
    }

    public List<Investimento> listarporFiltro(String texto) {
        List<Investimento> lista = new ArrayList<>();
        Cursor cursor = null;

        if (texto == null || texto.trim().isEmpty()) {
            return lista;
        }
        try {
            String selection = "descricao LIKE ?";
            String[] selectionArgs = { "%" + texto + "%" };

            cursor = db.query("investimento", null, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                Investimento i = new Investimento();
                i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                i.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                i.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
                i.setPercentualRentabilidade(cursor.getDouble(cursor.getColumnIndexOrThrow("percentual_rentabilidade")));
                try {
                    i.setDataInicio(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"))));
                } catch (Exception e) {
                    i.setDataInicio(new Date());
                }
                lista.add(i);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }
}
