package com.example.echobandapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FragmentAmigos extends Fragment implements AdapterView.OnItemClickListener {

    String arrayNombre[] = {"Jared Roa", "Gael Jaña", "Ariel Retana", "Diego War", "Jared UwU", "Mr Logic"};
    String arrayRango[] = {"Maestro III", "Profesional", "Aficionado", "Profesional II", "Maestro", "Aficionado III"};
    String arrayPuntos[] = {"7893", "1273", "0901", "2034", "1890", "4530"};
    int arrayFoto[] = {R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn, R.drawable.perfilbn};
    ListView listita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigosfragment, container, false);

        listita = view.findViewById(R.id.lv_amigos);
        listita.setOnItemClickListener(this);

        lvAdapterAmigos adapter = new lvAdapterAmigos(getContext(), arrayNombre, arrayFoto,arrayRango, arrayPuntos);
        listita.setAdapter(adapter);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}