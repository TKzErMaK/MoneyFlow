package com.gb.trabalho.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;

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

    public double buscarSaldo() {
        double receita = 0.0;
        double despesa = 0.0;

        Cursor cursorReceita = db.rawQuery("SELECT SUM(valor) FROM movimentacao WHERE tipo = 1", null);
        if (cursorReceita.moveToFirst()) {
            receita = cursorReceita.isNull(0) ? 0.0 : cursorReceita.getDouble(0);
        }
        cursorReceita.close();

        Cursor cursorDespesa = db.rawQuery("SELECT SUM(valor) FROM movimentacao WHERE tipo = 0", null);
        if (cursorDespesa.moveToFirst()) {
            despesa = cursorDespesa.isNull(0) ? 0.0 : cursorDespesa.getDouble(0);
        }
        cursorDespesa.close();

        return receita - despesa;
    }

    public double buscarReceitaPeriodo(String dataInicial) {
        double receita = 0.0;
        String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery(
                "SELECT SUM(valor) FROM movimentacao WHERE tipo = 1 AND data BETWEEN ? AND ?",
                new String[]{dataInicial, dataAtual}
        );

        if (cursor.moveToFirst()) {
            receita = cursor.isNull(0) ? 0.0 : cursor.getDouble(0);
        }
        cursor.close();
        return receita;
    }

    public double buscarDespesaPeriodo(String dataInicial) {
        double despesa = 0.0;
        String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery(
                "SELECT SUM(valor) FROM movimentacao WHERE tipo = 0 AND data BETWEEN ? AND ?",
                new String[]{dataInicial, dataAtual}
        );

        if (cursor.moveToFirst()) {
            despesa = cursor.isNull(0) ? 0.0 : cursor.getDouble(0);
        }
        cursor.close();
        return despesa;
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

    public List<Movimentacao> listarPorMes(int mes, int ano) {
        List<Movimentacao> lista = new ArrayList<>();
        List<Movimentacao> todos = listarTodos();

        for (Movimentacao movimentacao : todos) {
            if (movimentacao.getData().getMonth() == mes - 1 && movimentacao.getData().getYear() + 1900 == ano) {
                Movimentacao m = new Movimentacao();
                m.setId(movimentacao.getId());
                m.setDescricao(movimentacao.getDescricao());
                m.setValor(movimentacao.getValor());
                m.setData(movimentacao.getData());
                m.setTipo(movimentacao.getTipo());
                lista.add(m);
            }
        }
        return lista;
    }
}
