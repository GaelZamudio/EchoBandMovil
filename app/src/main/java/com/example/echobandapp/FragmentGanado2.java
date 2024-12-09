package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentGanado2 extends Fragment {
TextView tvMaximo, tvMinimo, tvPromedio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ganadofragment1, container, false);

        // Referenciar botones
        Button btnVolver = rootView.findViewById(R.id.btn_volver);
        Button btnDenuevo = rootView.findViewById(R.id.btn_denuevo);



        // Configurar el botón "Volver al inicio"
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reemplazar por el FragmentEntrenamiento
                Fragment entrenamientoFragment = new FragmentEntrenamiento();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, entrenamientoFragment);
                transaction.addToBackStack(null);  // Agregar a la pila de retroceso para permitir navegación atrás
                transaction.commit();
            }
        });

        // Configurar el botón "Siguiente ejercicio"
        btnDenuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reemplazar por el mismo fragmento para "repetir" el ejercicio
                Fragment simondiceFragment = new FragmentSimonDice();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, simondiceFragment);
                transaction.addToBackStack(null);  // Agregar a la pila de retroceso
                transaction.commit();
            }
        });

        //OBTENEMOS LAS PREFS
        SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);

        int id_usuario = preferences.getInt("id_usuario", 0);
        String nombre = preferences.getString("nombre", "");
        String correo = preferences.getString("correo", "");
        int maximo = preferences.getInt("maximo",0);
        int minimo = preferences.getInt("minimo",0);
        int promedio = preferences.getInt("promedio", 0);
        float tiempoReaccion = preferences.getFloat("tiempoReaccion", 0.00f);
        int puntos = 0;

        if (tiempoReaccion<0.4){
            puntos = 25;
        } else if (tiempoReaccion < 0.6){
            puntos = 15;
        } else {
            puntos = 5;
        }
        //INICIAMOS LA BASE
        Base base = new Base(getContext(), "EchoBandDB", null, 1);
        base.insertarLogro(id_usuario, "Listo para la F1");
        base.actualizarRacha(id_usuario);

        base.insertarEntrenamiento(id_usuario,promedio, "Rapidez");

        base.actualizarPuntos(id_usuario, puntos);

        base.close();

        //MOSTRAMOS LOS DATOS
        tvMaximo = rootView.findViewById(R.id.tvMaximo);
        tvMinimo = rootView.findViewById(R.id.tvMinimo);
        tvPromedio = rootView.findViewById(R.id.tvPromedio);

        tvMaximo.setText("- Nivel máximo de concentración: "+maximo);
        tvMinimo.setText("- Nivel más bajo de concentración: "+minimo);
        tvPromedio.setText("- Concentración promedio: "+promedio);
        return rootView;
    }
}
