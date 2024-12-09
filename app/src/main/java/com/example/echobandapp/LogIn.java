package com.example.echobandapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    View fragmentLogIn;
    EditText etNombreLogIn, etContrasenaLogIn;
    Button btnIniciarSesion;
    TextView tvCrearCuenta, tvIncorrecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        SharedPreferences sharedPreferences = getSharedPreferences("EchoBandPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        fragmentLogIn = findViewById(R.id.logIn);

        etNombreLogIn = fragmentLogIn.findViewById(R.id.etNombreLogIn);
        etContrasenaLogIn = fragmentLogIn.findViewById(R.id.etContrasenaLogIn);
        btnIniciarSesion = fragmentLogIn.findViewById(R.id.btnIniciarSesion);
        tvCrearCuenta = fragmentLogIn.findViewById(R.id.tvCrearCuenta);
        tvIncorrecto = fragmentLogIn.findViewById(R.id.tvIncorrecto);

        btnIniciarSesion.setOnClickListener(this);
        tvCrearCuenta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCrearCuenta){
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.btnIniciarSesion){
            if (etNombreLogIn.getText().toString().isEmpty() || etContrasenaLogIn.getText().toString().isEmpty()){
                Toast.makeText(this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
            } else {

                String nombre = etNombreLogIn.getText().toString().trim();
                String contrasena = etContrasenaLogIn.getText().toString().trim();

                Base admin = new Base(this, "EchoBandDB", null, 1);
                SQLiteDatabase base = admin.getWritableDatabase();

                Cursor cursor = base.rawQuery("SELECT * FROM Usuario WHERE nombre = ? AND contrasena = ?", new String[]{nombre, contrasena});
                if (cursor.moveToFirst()){
                    Toast.makeText(this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show();
                    guardarDatos(nombre, contrasena);

                    Intent intent = new Intent(LogIn.this, Barra.class);
                    startActivity(intent);

                } else {
                    tvIncorrecto.setText("Nombre o contraseña incorrectos");
                }

                base.close();
                cursor.close();
            }
        }
    }

    private void guardarDatos(String nombre, String contrasena){
        int id_usuario = 0;
        Base admin = new Base(this, "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        Cursor cursor = base.rawQuery("SELECT id_usuario, correo FROM Usuario WHERE nombre = ? AND contrasena = ?", new String[]{nombre, contrasena});

        if (cursor.moveToFirst()) {
            id_usuario = cursor.getInt(0);
            String correo = cursor.getString(1);

            SharedPreferences preferences = getSharedPreferences("EchoBandPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putInt("id_usuario", id_usuario);
            editor.putString("nombre", nombre);
            editor.putString("correo", correo);
            editor.putString("contrasena", contrasena);

            editor.apply();
        }
    }

}