package com.gb.trabalho.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gb.trabalho.Helper.DatabaseHelper;
import com.gb.trabalho.Domain.Investimento;

import java.util.ArrayList;
import java.util.List;

public class InvestimentoDAO {
    private SQLiteDatabase db;

    public InvestimentoDAO(Context context) {
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    public long inserir(Investimento investimento) {
        ContentValues values = new ContentValues();
        values.put("descricao", investimento.getDescricao());
        values.put("valor", investimento.getValor());
        values.put("data_inicio", investimento.getDataInicio());
        values.put("percentual_rentabilidade", investimento.getPercentualRentabilidade());
        values.put("frequencia", investimento.getFrequencia());
        return db.insert("investimento", null, values);
    }

    public int alterar(Investimento investimento) {
        ContentValues values = new ContentValues();
        values.put("descricao", investimento.getDescricao());
        values.put("valor", investimento.getValor());
        values.put("data_inicio", investimento.getDataInicio());
        values.put("percentual_rentabilidade", investimento.getPercentualRentabilidade());
        values.put("frequencia", investimento.getFrequencia());
        return db.update("investimento", values, "id = ?", new String[]{String.valueOf(investimento.getId())});
    }

    public int deletar(int id) {
        return db.delete("investimento", "id = ?", new String[]{String.valueOf(id)});
    }

    public Investimento buscarPorId(int id) {
        Cursor cursor = db.query("investimento", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Investimento i = new Investimento();
            i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            i.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            i.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            i.setDataInicio(cursor.getString(cursor.getColumnIndexOrThrow("data_inicio")));
            i.setPercentualRentabilidade(cursor.getDouble(cursor.getColumnIndexOrThrow("percentual_rentabilidade")));
            i.setFrequencia(cursor.getString(cursor.getColumnIndexOrThrow("frequencia")));
            cursor.close();
            return i;
        }
        cursor.close();
        return null;
    }

    public List<Investimento> listarTodos() {
        List<Investimento> lista = new ArrayList<>();
        Cursor cursor = db.query("investimento", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Investimento i = new Investimento();
            i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            i.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            i.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            i.setDataInicio(cursor.getString(cursor.getColumnIndexOrThrow("data_inicio")));
            i.setPercentualRentabilidade(cursor.getDouble(cursor.getColumnIndexOrThrow("percentual_rentabilidade")));
            i.setFrequencia(cursor.getString(cursor.getColumnIndexOrThrow("frequencia")));
            lista.add(i);
        }
        cursor.close();
        return lista;
    }
}
