package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Você pode remover o setContentView atual se for apenas redirecionar
        Intent intent = new Intent(MainActivity.this, ListaMetasActivity.class);
        startActivity(intent);
        finish(); // Fecha a MainActivity se você não precisa voltar a ela
    }
}