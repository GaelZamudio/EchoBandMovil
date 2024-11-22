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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
View fragmentLogIn;
EditText etNombreLogIn, etContrasenaLogIn;
Button btnIniciarSesion;
TextView tvCrearCuenta, tvIncorrecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.log_in);

        //Encontramos el fragmento
        fragmentLogIn = findViewById(R.id.logIn);

        //Encontramos los elementos
        etNombreLogIn = fragmentLogIn.findViewById(R.id.etNombreLogIn);
        etContrasenaLogIn = fragmentLogIn.findViewById(R.id.etContrasenaLogIn);
        btnIniciarSesion = fragmentLogIn.findViewById(R.id.btnIniciarSesion);
        tvCrearCuenta = fragmentLogIn.findViewById(R.id.tvCrearCuenta);
        tvIncorrecto = fragmentLogIn.findViewById(R.id.tvIncorrecto);

        //Establecemos los listeners
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
                //Obtenemos los datos ingresados
                String nombre = etNombreLogIn.getText().toString().trim();
                String contrasena = etContrasenaLogIn.getText().toString().trim();

                //Hacemos la consulta

                Base admin = new Base(this, "EchoBandDB", null, 1);
                SQLiteDatabase base = admin.getWritableDatabase();

                //Verificamos si el usuario está registrado
                Cursor cursor = base.rawQuery("SELECT * FROM Usuario WHERE nombre = ? AND contrasena = ?", new String[]{nombre, contrasena});
                if (cursor.moveToFirst()){
                    Toast.makeText(this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show();
                    //Guardamos los datos principales en preferences
                    guardarDatos(nombre, contrasena);
                    Intent intent = new Intent(this, Entrenamiento.class);
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