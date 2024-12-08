package com.example.echobandapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Barra extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final int NAV_AMIGOS = R.id.nav_amigos;
    private static final int NAV_CERRAR = R.id.nav_cerrar;
    private static final int NAV_CONFIG = R.id.nav_config;
    private static final int NAV_ENTRENAR = R.id.nav_entrenar;
    private static final int NAV_ESTAD = R.id.nav_estadisticas;
    private static final int NAV_LIGA = R.id.nav_liga;
    private static final int NAV_PERFIL = R.id.nav_perfil;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barra_create);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_entrenar);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEntrenamiento()).commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);
        if (itemId == NAV_AMIGOS)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAmigos()).commit();
        else
            if (itemId == NAV_CERRAR) {
                Intent intentito = new Intent(this, SignUp.class);
                startActivity(intentito);
            } else
                if (itemId == NAV_CONFIG)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfiguracion()).commit();
                else
                    if (itemId == NAV_ENTRENAR)
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEntrenamiento()).commit();
                    else
                        if (itemId == NAV_ESTAD)
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEstadisticas()).commit();
                        else
                            if (itemId == NAV_LIGA)
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLiga()).commit();
                            else
                                if (itemId == NAV_PERFIL)
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentPerfil()).commit();
        return true;
    }
}
