package com.gb.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

     DrawerLayout drawerLayout;
     NavigationView navigationView;
     FloatingActionButton btnMenu, btnProfile;
     TextView txtTitle;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        btnMenu = findViewById(R.id.btn_esquerda);
        btnProfile = findViewById(R.id.btn_direita);
        txtTitle = findViewById(R.id.txt_titulo);

        // Abre o menu lateral
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Lógica do menu lateral
        navigationView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_Inicio) {
                intent = new Intent(BaseActivity.this, MainActivity.class);

            } else if (item.getItemId() == R.id.nav_CadastroInvestimento) {
                intent = new Intent(BaseActivity.this, CadastroInvestimentoActivity.class);

            } else if (item.getItemId() == R.id.nav_CadastroMeta) {
                intent = new Intent(BaseActivity.this, CadastroMetasActivity.class);

            } else if (item.getItemId() == R.id.nav_CadastroMovimentacao) {
                intent = new Intent(BaseActivity.this, CadastroMovimentacaoActivity.class);

            } else if (item.getItemId() == R.id.nav_CadastroNotificacao) {
                intent = new Intent(BaseActivity.this, CadastroNotificacoesActivity.class);

            } else if (item.getItemId() == R.id.nav_Extrato) {
                intent = new Intent(BaseActivity.this, ExtratoActivity.class);

            } else if (item.getItemId() == R.id.nav_ListaInvestimentos) {
                intent = new Intent(BaseActivity.this, ListaInvestimentosActivity.class);

            } else if (item.getItemId() == R.id.nav_ListaMetas) {
                intent = new Intent(BaseActivity.this, ListaMetasActivity.class);

            } else if (item.getItemId() == R.id.nav_ListaNotificacoes) {
                intent = new Intent(BaseActivity.this, ListaNotificacoesActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }

            if (intent != null) {
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // injeta o layout da activity que de fato vai abrir a tela
    protected void setActivityContent(int layoutResId) {
        LayoutInflater.from(this).inflate(layoutResId, findViewById(R.id.content_frame), true);
    }
}
