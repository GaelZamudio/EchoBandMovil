package com.example.echobandapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class FragmentEntrenamiento extends Fragment {

    private ListView lvEntrenamientos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entrenarfragment, container, false);

        lvEntrenamientos = rootView.findViewById(R.id.lvEntrenamientos);

        String[] titulos = {"Entrenamiento 1", "Entrenamiento 2", "Entrenamiento 3"};
        int[] imagenes = {R.drawable.entremosencalor, R.drawable.estrellas, R.drawable.concalma}; // Asegúrate de tener las imágenes en res/drawable

        Adaptador adaptador = new Adaptador(getActivity(), titulos, imagenes);
        lvEntrenamientos.setAdapter(adaptador);

        return rootView;
    }
}
