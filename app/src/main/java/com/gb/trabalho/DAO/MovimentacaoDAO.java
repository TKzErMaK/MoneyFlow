package com.gb.trabalho.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gb.trabalho.Helper.DatabaseHelper;
import com.gb.trabalho.Domain.Movimentacao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovimentacaoDAO {
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public MovimentacaoDAO(Context context) {
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    public long inserir(Movimentacao mov) {
        ContentValues values = new ContentValues();
        values.put("descricao", mov.getDescricao());
        values.put("valor", mov.getValor());
        values.put("data", dateFormat.format(mov.getData()));
        values.put("tipo", mov.getTipo());
        return db.insert("movimentacao", null, values);
    }

    public int alterar(Movimentacao mov) {
        ContentValues values = new ContentValues();
        values.put("descricao", mov.getDescricao());
        values.put("valor", mov.getValor());
        values.put("data", dateFormat.format(mov.getData()));
        values.put("tipo", mov.getTipo());
        return db.update("movimentacao", values, "id = ?", new String[]{String.valueOf(mov.getId())});
    }

    public int deletar(int id) {
        return db.delete("movimentacao", "id = ?", new String[]{String.valueOf(id)});
    }

    public Movimentacao buscarPorId(int id) {
        Cursor cursor = db.query("movimentacao", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Movimentacao m = new Movimentacao();
            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            m.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            m.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            try {
                m.setData(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("data"))));
            } catch (Exception e) {
                m.setData(new Date());
            }
            m.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
            cursor.close();
            return m;
        }
        cursor.close();
        return null;
    }

    public List<Movimentacao> listarTodos() {
        List<Movimentacao> lista = new ArrayList<>();
        Cursor cursor = db.query("movimentacao", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Movimentacao m = new Movimentacao();
            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            m.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            m.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            try {
                m.setData(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("data"))));
            } catch (Exception e) {
                m.setData(new Date());
            }
            m.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
            lista.add(m);
        }
        cursor.close();
        return lista;
    }
}
