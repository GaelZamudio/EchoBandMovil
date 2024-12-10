package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentEntrenamiento extends Fragment {

    private ListView lvEntrenamientos;
    private TextView tvHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entrenarfragment, container, false);

        //OBTENEMOS LAS PREFS
        SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "");

        lvEntrenamientos = rootView.findViewById(R.id.lvEntrenamientos);
        tvHeader = rootView.findViewById(R.id.tvHeader);

        tvHeader.setText("Hola de nuevo, "+nombre);

        String[] titulos = {"Entrenamiento 1", "Entrenamiento 2", "Entrenamiento 3"};
        int[] imagenes = {R.drawable.entremosencalor, R.drawable.estrellas, R.drawable.concalma};

        Adaptador adaptador = new Adaptador(getActivity(), titulos, imagenes);
        lvEntrenamientos.setAdapter(adaptador);

        // Configurar el listener de clic
        lvEntrenamientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;

                // Verificar el ítem seleccionado con if
                if (position == 0) {
                    fragment = new FragmentEntremosEnCalor();
                } else if (position == 1) {
                    fragment = new FragmentEstrellas();
                } else if (position == 2) {
                    fragment = new FragmentConCalma();
                }

                // Reemplazar el fragmento si es necesario
                if (fragment != null) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment); // Cambia `fragment_container` por el ID de tu contenedor de fragmentos
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return rootView;
    }
}
