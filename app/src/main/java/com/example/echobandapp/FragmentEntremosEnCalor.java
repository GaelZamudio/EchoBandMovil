package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class FragmentEntremosEnCalor extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_entremosencalorfragment, container, false);

        // Buscar el botón
        Button btnToMemorama = view.findViewById(R.id.botoon);

        // Configurar la acción al hacer clic
        btnToMemorama.setOnClickListener(v -> {
            // Reemplazar este fragmento por el FragmentMemorama
            FragmentMemorama fragmentMemorama = new FragmentMemorama();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentMemorama) // Cambia fragment_container por el ID del contenedor
                    .addToBackStack(null) // Permite regresar al fragmento actual si es necesario
                    .commit();
        });

        return view;
    }
}
