package com.example.echobandapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        db.execSQL("CREATE TABLE Logro(id_logro INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, titulo text, FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario));");
        db.execSQL("CREATE TABLE Entrenamiento(id_entrenamiento INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, fecha DATE, concentracion_promedio INTEGER, tipo_ejercicio TEXT, FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario));");
    }

    public void insertarLogro(int id_usuario, String titulo) {
        SQLiteDatabase base = this.getWritableDatabase();

        // Verificar si ya existe un logro con el mismo id_usuario y título
        String query = "SELECT COUNT(*) FROM Logro WHERE id_usuario = ? AND titulo = ?";
        String[] args = {String.valueOf(id_usuario), titulo};
        boolean existeLogro = false;

        try (android.database.Cursor cursor = base.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                existeLogro = cursor.getInt(0) > 0;  // Si el conteo es mayor a 0, existe el logro
            }
        }

        // Si no existe, insertamos el nuevo logro
        if (!existeLogro) {
            ContentValues registro = new ContentValues();
            registro.put("id_usuario", id_usuario);
            registro.put("titulo", titulo);
            base.insert("Logro", null, registro);
        }

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

    public void actualizarPuntos(int id_usuario, int puntosAGrabar) {
        SQLiteDatabase base = this.getWritableDatabase();

        // Obtener los puntos actuales del usuario
        int puntosActuales = obtenerPuntos(id_usuario);

        // Sumar los puntos nuevos a los puntos actuales
        int nuevosPuntos = puntosActuales + puntosAGrabar;

        // Actualizar los puntos en la base de datos
        ContentValues registro = new ContentValues();
        registro.put("puntos", nuevosPuntos);

        base.update("Usuario", registro, "id_usuario = ?", new String[]{String.valueOf(id_usuario)});

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

    public void actualizarUsuario(int id_usuario, String nuevoNombre, String nuevoCorreo, String nuevaContrasena) {
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues registro = new ContentValues();

        // Insertamos los nuevos valores para los campos: nombre, correo y contraseña
        registro.put("nombre", nuevoNombre);
        registro.put("correo", nuevoCorreo);
        registro.put("contrasena", nuevaContrasena);

        // Realizamos la actualización en la tabla Usuario utilizando el id del usuario
        base.update("Usuario", registro, "id_usuario = ?", new String[]{String.valueOf(id_usuario)});
    }

    public ArrayList<Integer> obtenerConcentraciones(int idUsuario, int limite) {
        ArrayList<Integer> concentraciones = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT concentracion_promedio FROM Entrenamiento WHERE id_usuario = ? LIMIT ?",
                new String[]{String.valueOf(idUsuario), String.valueOf(limite)}
        );

        if (cursor.moveToFirst()) {
            do {
                concentraciones.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return concentraciones;
    }

    public int obtenerEntrenamientosPorTipo(int id_usuario, String tipoEjercicio) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Entrenamiento WHERE id_usuario = ? AND tipo_ejercicio = ?";
        String[] args = {String.valueOf(id_usuario), tipoEjercicio};
        int count = 0;

        try (Cursor cursor = db.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }

        return count;
    }

    public int obtenerTotalEntrenamientos(int id_usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM Entrenamiento WHERE id_usuario = ?";
        String[] args = {String.valueOf(id_usuario)};
        int total = 0;

        try (Cursor cursor = db.rawQuery(query, args)) {
            if (cursor.moveToFirst()) {
                total = cursor.getInt(0);
            }
        }

        return total;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Elimina las tablas si ya existen
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Logro");
        db.execSQL("DROP TABLE IF EXISTS Entrenamiento");

        // Vuelve a crear las tablas
        onCreate(db);
    }

}
