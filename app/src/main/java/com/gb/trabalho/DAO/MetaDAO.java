package com.gb.trabalho.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gb.trabalho.Helper.DatabaseHelper;
import com.gb.trabalho.Domain.Meta;

import java.util.ArrayList;
import java.util.List;

public class MetaDAO {
    private SQLiteDatabase db;

    public MetaDAO(Context context) {
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    public long inserir(Meta meta) {
        ContentValues values = new ContentValues();
        values.put("descricao", meta.getDescricao());
        values.put("valor", meta.getValor());
        values.put("data_inicio", meta.getDataInicio());
        values.put("prazo", meta.getPrazo());
        values.put("tipo", meta.getTipo());
        return db.insert("meta", null, values);
    }

    public int alterar(Meta meta) {
        ContentValues values = new ContentValues();
        values.put("descricao", meta.getDescricao());
        values.put("valor", meta.getValor());
        values.put("data_inicio", meta.getDataInicio());
        values.put("prazo", meta.getPrazo());
        values.put("tipo", meta.getTipo());
        return db.update("meta", values, "id = ?", new String[]{String.valueOf(meta.getId())});
    }

    public int deletar(int id) {
        return db.delete("meta", "id = ?", new String[]{String.valueOf(id)});
    }

    public Meta buscarPorId(int id) {
        Cursor cursor = db.query("meta", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Meta m = new Meta();
            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            m.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            m.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            m.setDataInicio(cursor.getString(cursor.getColumnIndexOrThrow("data_inicio")));
            m.setPrazo(cursor.getInt(cursor.getColumnIndexOrThrow("prazo")));
            m.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
            cursor.close();
            return m;
        }
        cursor.close();
        return null;
    }

    public List<Meta> listarTodos() {
        List<Meta> lista = new ArrayList<>();
        Cursor cursor = db.query("meta", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Meta m = new Meta();
            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            m.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            m.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            m.setDataInicio(cursor.getString(cursor.getColumnIndexOrThrow("data_inicio")));
            m.setPrazo(cursor.getInt(cursor.getColumnIndexOrThrow("prazo")));
            m.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
            lista.add(m);
        }
        cursor.close();
        return lista;
    }
}
