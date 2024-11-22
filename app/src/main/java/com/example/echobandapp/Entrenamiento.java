package com.example.echobandapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class Entrenamiento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento);

        SharedPreferences preferences = getSharedPreferences("EchoBandPrefs", MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "");
        int id_usuario = preferences.getInt("id_usuario", -1);
        String correo = preferences.getString("correo", "");
        Toast.makeText(this, "Hola "+nombre+", eres el usuario número: "+id_usuario+"\n tu correo es: "+correo, Toast.LENGTH_SHORT).show();
    }
}