package com.example.echobandapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentConCalma extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_concalma, container, false);

        // Configurar el listener para el botón
        view.findViewById(R.id.botoon).setOnClickListener(v -> {
            // Crear una instancia de FragmentSimonDice
            FragmentSimonDice fragmentSimonDice = new FragmentSimonDice();
            // Reemplazar este fragmento por FragmentSimonDice
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentSimonDice) // Cambiar R.id.fragment_container por el ID correcto de tu contenedor de fragmentos
                    .addToBackStack(null) // Permitir regresar al FragmentConCalma
                    .commit();
        });

        return view;
    }
}
