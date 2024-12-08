package com.example.echobandapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentTerminado3 extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terminado1, container, false);

        // Configurar el botón "Volver al inicio"
        view.findViewById(R.id.btn_volver).setOnClickListener(v -> {
            // Reemplazar este fragmento con el fragmento de entrenamiento
            FragmentEntrenamiento fragmentEntrenamiento = new FragmentEntrenamiento();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentEntrenamiento) // Cambia fragment_container por el ID del contenedor
                    .addToBackStack(null) // Permite regresar al fragmento actual si es necesario
                    .commit();
        });

        // Configurar el botón "Inténtalo de nuevo"
        view.findViewById(R.id.btn_denuevo).setOnClickListener(v -> {
            // Reemplazar este fragmento con el fragmento del ejercicio actual
            FragmentSimonDice fragmentSimonDice = new FragmentSimonDice();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentSimonDice) // Cambia fragment_container por el ID del contenedor
                    .addToBackStack(null) // Permite regresar al fragmento actual si es necesario
                    .commit();
        });

        return view;
    }
}
