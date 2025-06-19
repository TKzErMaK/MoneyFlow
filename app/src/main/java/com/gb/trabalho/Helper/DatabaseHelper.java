package com.gb.trabalho.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "Financeiro.db";
    private static final int VERSAO = 1;

    public static final String TABELA_MOVIMENTACAO = "movimentacao";
    public static final String TABELA_INVESTIMENTO = "investimento";
    public static final String TABELA_META = "meta";
    public static final String TABELA_NOTIFICACAO = "notificacao";

    public DatabaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMovimentacao = "CREATE TABLE " + TABELA_MOVIMENTACAO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT, " +
                "valor REAL, " +
                "data TEXT, " +
                "tipo INTEGER)";

        String createInvestimento = "CREATE TABLE " + TABELA_INVESTIMENTO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT, " +
                "valor REAL, " +
                "data_inicio TEXT, " +
                "percentual_rentabilidade REAL, " +
                "prazo INTEGER)";

        String createMeta = "CREATE TABLE " + TABELA_META + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT, " +
                "valor REAL, " +
                "data_inicio TEXT, " +
                "prazo INTEGER, " +
                "tipo INTEGER)";

        String createNotificacao = "CREATE TABLE " + TABELA_NOTIFICACAO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT, " +
                "valor REAL, " +
                "prazo INTEGER, " +
                "tipo INTEGER, " +
                "data TEXT)";

        db.execSQL(createMovimentacao);
        db.execSQL(createInvestimento);
        db.execSQL(createMeta);
        db.execSQL(createNotificacao);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_MOVIMENTACAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_INVESTIMENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_META);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_NOTIFICACAO);
        onCreate(db);
    }
}
