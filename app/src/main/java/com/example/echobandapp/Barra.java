package com.example.echobandapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import org.w3c.dom.Text;

public class Barra extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, FragmentConfiguracion.OnPerfilUpdatedListener {

    private static final int NAV_AMIGOS = R.id.nav_amigos;
    private static final int NAV_CERRAR = R.id.nav_cerrar;
    private static final int NAV_CONFIG = R.id.nav_config;
    private static final int NAV_ENTRENAR = R.id.nav_entrenar;
    private static final int NAV_ESTAD = R.id.nav_estadisticas;
    private static final int NAV_LIGA = R.id.nav_liga;
    private static final int NAV_PERFIL = R.id.nav_perfil;
    private DrawerLayout drawerLayout;
    TextView tvNombre, tvCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barra_create);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_entrenar);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEntrenamiento()).commit();
        }

        //OBTENEMOS LAS PREFS
        SharedPreferences preferences = getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
        int id_usuario = preferences.getInt("id_usuario", 0);
        String nombre = preferences.getString("nombre", "");
        String correo = preferences.getString("correo", "");

        tvNombre = headerView.findViewById(R.id.tvNombre);
        tvNombre.setText(nombre);

        tvCorreo = headerView.findViewById(R.id.tvCorreo);
        tvCorreo.setText(correo);
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

    //Estamos sobreescribiendo el método de la interfaz que se declara en PerfilFragment
    @Override
    public void onPerfilUpdated(String nuevoNombre, String nuevoCorreo) {
        // Actualizamos los tvs de la barra de navegación
        tvNombre.setText(nuevoNombre);
        tvCorreo.setText(nuevoCorreo);
    }
}
