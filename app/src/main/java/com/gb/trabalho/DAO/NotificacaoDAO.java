package com.gb.trabalho.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gb.trabalho.Helper.DatabaseHelper;
import com.gb.trabalho.Domain.Notificacao;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {
    private SQLiteDatabase db;

    public NotificacaoDAO(Context context) {
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    public long inserir(Notificacao notificacao) {
        ContentValues values = new ContentValues();
        values.put("descricao", notificacao.getDescricao());
        values.put("valor", notificacao.getValor());
        values.put("prazo", notificacao.getPrazo());
        values.put("tipo", notificacao.getTipo());
        return db.insert("notificacao", null, values);
    }

    public int alterar(Notificacao notificacao) {
        ContentValues values = new ContentValues();
        values.put("descricao", notificacao.getDescricao());
        values.put("valor", notificacao.getValor());
        values.put("prazo", notificacao.getPrazo());
        values.put("tipo", notificacao.getTipo());
        return db.update("notificacao", values, "id = ?", new String[]{String.valueOf(notificacao.getId())});
    }

    public int deletar(int id) {
        return db.delete("notificacao", "id = ?", new String[]{String.valueOf(id)});
    }

    public Notificacao buscarPorId(int id) {
        Cursor cursor = db.query("notificacao", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Notificacao n = new Notificacao();
            n.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            n.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            n.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            n.setPrazo(cursor.getInt(cursor.getColumnIndexOrThrow("prazo")));
            n.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
            cursor.close();
            return n;
        }
        cursor.close();
        return null;
    }

    public List<Notificacao> listarTodos() {
        List<Notificacao> lista = new ArrayList<>();
        Cursor cursor = db.query("notificacao", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Notificacao n = new Notificacao();
            n.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            n.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            n.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
            n.setPrazo(cursor.getInt(cursor.getColumnIndexOrThrow("prazo")));
            n.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
            lista.add(n);
        }
        cursor.close();
        return lista;
    }
}
