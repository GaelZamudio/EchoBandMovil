package com.example.echobandapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
View fragmentSignUp;
EditText etNombreSignUp, etCorreoSignUp, etContrasenaSignUp;
Button btnCrearCuenta;
TextView tvIniciaSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);

        //Encontramos el fragmento del Sign Up
        fragmentSignUp = findViewById(R.id.signUp);

        //Definimos las variables
        etNombreSignUp = fragmentSignUp.findViewById(R.id.etNombreSignUp);
        etCorreoSignUp = fragmentSignUp.findViewById(R.id.etCorreoSignUp);
        etContrasenaSignUp = fragmentSignUp.findViewById(R.id.etContrasenaSignUp);

        tvIniciaSesion = fragmentSignUp.findViewById(R.id.tvIniciaSesion);

        btnCrearCuenta = fragmentSignUp.findViewById(R.id.btnCrearCuenta);

        //Asignamos los listeners correspondientes
        tvIniciaSesion.setOnClickListener(this);
        btnCrearCuenta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvIniciaSesion){
            //Redirigimos al módulo de Iniciar Sesión
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.btnCrearCuenta){
            //Verificamos que todos los canmpos estén llenos
            if (etNombreSignUp.getText().toString().isEmpty() || etCorreoSignUp.getText().toString().isEmpty() || etContrasenaSignUp.getText().toString().isEmpty()){
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                //Obtenemos los datos que ingresó el usuario
                String nombre = etNombreSignUp.getText().toString().trim();
                String correo = etCorreoSignUp.getText().toString().trim();
                String contrasena = etContrasenaSignUp.getText().toString().trim();
                //Validamos datos
                if (esNombreValido(nombre)){
                    if (esCorreoValido(correo)){
                        if (esContrasenaValida(contrasena)){
                            //Comprobamos que no estén registrados ni el correo ni el usuario
                            if (existeUsuario(nombre)){
                                Toast.makeText(this, "Este nombre está ocupado", Toast.LENGTH_SHORT).show();
                            } else {
                                if (estaRegistradoCorreo(correo)){
                                    Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Insertamos al usuario y guardamos los datos en prefs
                                    insertarUsuario(nombre, correo, contrasena);
                                    guardarDatosPorNombre(nombre, contrasena, correo);
                                    Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, Entrenamiento.class);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            Toast.makeText(this, "Por favor, ingresa una contraseña válida", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Por favor, ingresa un correo válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Nombre no válido, prueba con otro", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Método para validar formato de correo
    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";
        return correo.matches(patronCorreo);
    }

    //Método para validar el nombre de usuario
    private boolean esNombreValido(String nombre) {
        return nombre.matches("^[a-zA-Z0-9]+$");
    }

    //Método para validar la contraseña
    private boolean esContrasenaValida(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$");
    }

    //Método para comprobar si el nombre ya está ocupado
    private boolean existeUsuario(String nombre){
        Base admin = new Base(this, "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM Usuario WHERE nombre = ?", new String[]{nombre});

        if (cursor.moveToFirst()){
            base.close();
            cursor.close();
            return true;
        } else {
            base.close();
            cursor.close();
            return false;
        }
    }

    //Método para comprobar si el correo ya está registrado
    private boolean estaRegistradoCorreo(String correo){
        Base admin = new Base(this, "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM Usuario WHERE correo = ?", new String[]{correo});

        if (cursor.moveToFirst()){
            base.close();
            cursor.close();
            return true;
        } else {
            base.close();
            cursor.close();
            return false;
        }
    }

    //Método para insertar nuevo usuario
    private void insertarUsuario(String nombre, String correo, String contrasena){
        Base admin = new Base(this, "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("correo", correo);
        registro.put("contrasena", contrasena);

        base.insert("Usuario", null, registro);
        base.close();
    }

    //Método para obtener el id por medio del nombre
    private void guardarDatosPorNombre(String nombre, String contrasena, String correo){
        Base admin = new Base(this, "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        Cursor cursor = base.rawQuery("SELECT id_usuario FROM Usuario WHERE nombre = ?", new String[]{nombre});

        if (cursor.moveToFirst()){
            int id_usuario = cursor.getInt(0);

            SharedPreferences preferences = getSharedPreferences("EchoBandPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("id_usuario", id_usuario);
            editor.putString("nombre", nombre);
            editor.putString("contrasena", contrasena);
            editor.putString("correo", correo);
            editor.apply();
        }

        base.close();
        cursor.close();
    }
}