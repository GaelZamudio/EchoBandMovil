package com.example.echobandapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.view.Gravity;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentPerfil extends Fragment {

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_perfilfragment, container, false);
        Button btnPuntos, btnRacha, btnLogros;

        ListView listView = rootView.findViewById(R.id.listViewLogros);
        TextView tvNoHayLogros = rootView.findViewById(R.id.tvNoHayLogros);
        TextView tvNombre = rootView.findViewById(R.id.tvNombre);
        TextView tvCorreo = rootView.findViewById(R.id.tvCorreo);
        TextView tvTitulo = rootView.findViewById(R.id.tvTitulo);

        btnPuntos = rootView.findViewById(R.id.btnPuntos);
        btnRacha = rootView.findViewById(R.id.btnRacha);
        btnLogros = rootView.findViewById(R.id.btnLogros);

        //OBTENEMOS LAS PREFS
        SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
        int id_usuario = preferences.getInt("id_usuario", 0);
        String nombre = preferences.getString("nombre", "");
        String correo = preferences.getString("correo", "");

        //MOSTRAMOS LOS DATOS DEL USUARIO
        tvNombre.setText(nombre);
        tvCorreo.setText(correo);

        //INICIAMOS LA BASE
        Base base = new Base(getContext(), "EchoBandDB", null, 1);

        //OBTENEMOS LOS PUNTOS Y LOS MOSTRAMOS
        int puntos = base.obtenerPuntos(id_usuario);
        String rango = "";
        btnPuntos.setText(puntos+" PUNTOS");

        //OBTENEMOS RANGO DEL USUARIO Y LO MOSTRAMOS
        if (puntos < 25) {
            rango = "Principiante";
        } else if (puntos < 50) {
            rango = "Aficionado";
        } else if (puntos < 100) {
            rango = "Avanzado";
        } else if (puntos < 200) {
            rango = "Profesional";
        } else if (puntos < 400) {
            rango = "Experto";
        } else if (puntos < 800) {
            rango = "Maestro";
        } else {
            rango = "Gran Maestro";
        }
        tvTitulo.setText(rango);

        //OBTENEMOS LA RACHA Y LA MOSTRAMOS
        int racha = base.obtenerRacha(id_usuario);
        btnRacha.setText("RACHA DE "+racha+" DÍAS");

        //OBTENEMOS LA CANTIDAD DE LOGROS Y LA MOSTRAMOS
        int cantLogros = base.obtenerCantidadLogros(id_usuario);
        btnLogros.setText(cantLogros+"/10 LOGROS");

        //OBTENEMOS LOS LOGROS DEL USUARIO Y LOS MOSTRAMOS
        String[] logrosUsuario = base.obtenerTitulosLogros(id_usuario);
        ArrayList<String> logros = new ArrayList<>(Arrays.asList(logrosUsuario));
        if (logros.isEmpty()) {
            tvNoHayLogros.setText("Todavía no consigues tu primer logro.\n¡Completa niveles para obtener tu primer logro!");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, logros) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                textView.setBackgroundColor(Color.parseColor("#FFF9C4"));
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(16, 16, 16, 16);
                textView.setTextSize(16);
                textView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                return textView;
            }
        };

        listView.setAdapter(adapter);

        return rootView;
    }
}
