package com.gb.trabalho.DAO;

import static androidx.core.database.CursorKt.getStringOrNull;
import static com.gb.trabalho.Helper.DatabaseHelper.getInt;
import static com.gb.trabalho.Helper.DatabaseHelper.getString;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.text.TextUtils;
import android.widget.Toast;

import com.gb.trabalho.Helper.DatabaseHelper;
import com.gb.trabalho.Domain.Notificacao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificacaoDAO {
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public NotificacaoDAO(Context context) {
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    public long inserir(Notificacao notificacao) {
        ContentValues values = new ContentValues();
        values.put("descricao", notificacao.getDescricao());
        values.put("valor", notificacao.getValor());
        values.put("prazo", notificacao.getPrazo());
        values.put("tipo", notificacao.getTipo());
        values.put("data_vencimento", dateFormat.format(notificacao.getDataVencimento()));
        /*String dataVencimentoStr = notificacao.getDataVencimento();

        if (dataVencimentoStr != null && !dataVencimentoStr.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date date = sdf.parse(dataVencimentoStr);
                String dataFormatada = sdf.format(date);
                values.put("data_vencimento", dataFormatada);
            } catch (ParseException e) {
                e.printStackTrace();
                values.putNull("data_vencimento");
            }
        } else {
            values.putNull("data_vencimento");
        }*/
        return db.insert("notificacao", null, values);
    }

    public int alterar(Notificacao notificacao) {
        ContentValues values = new ContentValues();
        values.put("descricao", notificacao.getDescricao());
        values.put("valor", notificacao.getValor());
        values.put("prazo", notificacao.getPrazo());
        values.put("tipo", notificacao.getTipo());
        values.put("data_vencimento", dateFormat.format(notificacao.getDataVencimento()));
        return db.update("notificacao", values, "id = ?", new String[]{String.valueOf(notificacao.getId())});
    }

    public int deletar(int id) {
        return db.delete("notificacao", "id = ?", new String[]{String.valueOf(id)});
    }

    public void deletarNotificacoesVencidas() {
        Date hoje = new Date();
        Date dataVencimento;
        Cursor cursor = db.query("notificacao", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                dataVencimento = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_vencimento")));
            } catch (Exception e) {
                dataVencimento = new Date();
            }
            int id = getInt(cursor, "id");

            if (dataVencimento != null) {
                if (dataVencimento.before(hoje)) {
                    db.delete("notificacao", "id = ?", new String[]{String.valueOf(id)});
                }
            }
        }
        cursor.close();
    }

    public List<Notificacao> listarTodos() {
        List<Notificacao> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query("notificacao", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Notificacao n = new Notificacao();
                n.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                n.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                n.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow("valor")));
                n.setPrazo(cursor.getInt(cursor.getColumnIndexOrThrow("prazo")));
                n.setTipo(cursor.getInt(cursor.getColumnIndexOrThrow("tipo")));
                try {
                    n.setDataVencimento(dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_vencimento"))));
                } catch (Exception e) {
                    n.setDataVencimento(new Date());
                }
                lista.add(n);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return lista;
    }


}
