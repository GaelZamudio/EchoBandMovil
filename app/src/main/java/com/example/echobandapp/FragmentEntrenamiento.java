package com.example.echobandapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentEntrenamiento extends Fragment {

    private ListView lvEntrenamientos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entrenarfragment, container, false);

        lvEntrenamientos = rootView.findViewById(R.id.lvEntrenamientos);

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
