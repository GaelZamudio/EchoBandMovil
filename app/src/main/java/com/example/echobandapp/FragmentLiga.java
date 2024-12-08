package com.example.echobandapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentLiga extends Fragment {

    String arrayNombre[] = {"Jared Roa", "Gael Jaña", "Ariel Retana", "Diego War", "Jared UwU", "Mr Logic"};
    String arrayRango[] = {"Maestro III", "Profesional", "Aficionado", "Profesional II", "Maestro", "Aficionado III"};
    String arrayPuntos[] = {"4893", "4530", "2034", "1890", "1290", "0843"};
    String arrayLugar[] = {"1°", "2°", "3°", "4°", "5°", "6°"};
    int arrayFoto[] = {R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn};
    ListView listita;

    ImageView imagencita;
    TextView nombrecito, ranguito, puntitos;
    ProgressBar barrita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ligafragment, container, false);

        listita = view.findViewById(R.id.lv_liga);
        imagencita = view.findViewById(R.id.iv_imagenperfil);
        nombrecito = view.findViewById(R.id.tv_nombre);
        ranguito = view.findViewById(R.id.tv_rango);
        puntitos = view.findViewById(R.id.tv_puntos);
        barrita = view.findViewById(R.id.progress_bar);

        lvAdapterLiga adapter = new lvAdapterLiga(getContext(), arrayNombre, arrayFoto, arrayRango, arrayPuntos, arrayLugar);
        listita.setAdapter(adapter);

        nombrecito.setText(arrayNombre[0]);
        ranguito.setText(arrayRango[0]);
        puntitos.setText(arrayPuntos[0]);
        imagencita.setImageResource(arrayFoto[0]);

        int puntosActuales = Integer.parseInt(arrayPuntos[0]);
        barrita.setProgress(puntosActuales);

        return view;
    }
}