package com.example.echobandapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;

public class Base extends SQLiteOpenHelper {

    public Base(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuario(id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, nombre text UNIQUE, correo text UNIQUE, contrasena text, puntos INTEGER DEFAULT 0, racha INTEGER DEFAULT 0);");
        db.execSQL("CREATE TABLE Logro(id_logro INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, titulo text UNIQUE, FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario));");
        db.execSQL("CREATE TABLE Entrenamiento(id_entrenamiento INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, fecha DATE, concentracion_promedio INTEGER, tipo_ejercicio TEXT, FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario));");
    }

    public void insertarLogro(int id_usuario, String titulo){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("id_usuario", id_usuario);
        registro.put("titulo", titulo);
        base.insert("Logro", null, registro);
    }

    public void insertarEntrenamiento(int id_usuario, int concentracion_promedio, String tipo_ejercicio) {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues registro = new ContentValues();

        // Formatear la fecha actual en el formato "YYYY-MM-DD"
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        registro.put("id_usuario", id_usuario);
        registro.put("fecha", fechaActual);
        registro.put("concentracion_promedio", concentracion_promedio);
        registro.put("tipo_ejercicio", tipo_ejercicio);

        base.insert("Entrenamiento", null, registro);
    }

    public void actualizarRacha(int id_usuario) {
        SQLiteDatabase base = this.getWritableDatabase();

        // Obtener la fecha actual en formato "YYYY-MM-DD"
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Verificar si existe un registro de entrenamiento en la fecha actual
        String query = "SELECT COUNT(*) FROM Entrenamiento WHERE id_usuario = ? AND fecha = ?";
        String[] args = {String.valueOf(id_usuario), fechaActual};
        boolean existeRegistro = false;

        try (android.database.Cursor cursor = base.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                existeRegistro = cursor.getInt(0) > 0;
            }
        }

        // Si no existe registro, incrementar la racha
        if (!existeRegistro) {
            String updateQuery = "UPDATE Usuario SET racha = racha + 1 WHERE id_usuario = ?";
            base.execSQL(updateQuery, new Object[]{id_usuario});
        }
    }

    public int obtenerPuntos(int id_usuario) {
        SQLiteDatabase base = this.getReadableDatabase();
        String query = "SELECT puntos FROM Usuario WHERE id_usuario = ?";
        String[] args = {String.valueOf(id_usuario)};
        int puntos = 0;

        try (android.database.Cursor cursor = base.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                puntos = cursor.getInt(0);
            }
        }

        return puntos;
    }

    public int obtenerRacha(int id_usuario) {
        SQLiteDatabase base = this.getReadableDatabase();
        String query = "SELECT racha FROM Usuario WHERE id_usuario = ?";
        String[] args = {String.valueOf(id_usuario)};
        int racha = 0;

        try (android.database.Cursor cursor = base.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                racha = cursor.getInt(0);
            }
        }

        return racha;
    }

    public int obtenerCantidadLogros(int id_usuario) {
        SQLiteDatabase base = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Logro WHERE id_usuario = ?";
        String[] args = {String.valueOf(id_usuario)};
        int cantidad = 0;

        try (android.database.Cursor cursor = base.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                cantidad = cursor.getInt(0);
            }
        }

        return cantidad;
    }

    public String[] obtenerTitulosLogros(int id_usuario) {
        SQLiteDatabase base = this.getReadableDatabase();
        String query = "SELECT titulo FROM Logro WHERE id_usuario = ?";
        String[] args = {String.valueOf(id_usuario)};
        String[] titulos;

        try (android.database.Cursor cursor = base.rawQuery(query, args)) {
            if (cursor.getCount() > 0) {
                titulos = new String[cursor.getCount()];
                int index = 0;
                while (cursor.moveToNext()) {
                    titulos[index] = cursor.getString(0);
                    index++;
                }
            } else {
                titulos = new String[0]; // Arreglo vacío si no hay logros
            }
        }

        return titulos;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
