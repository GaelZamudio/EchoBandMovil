package com.example.echobandapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentPerfil extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_perfilfragment, container, false);

        ListView listView = rootView.findViewById(R.id.listViewLogros);

        ArrayList<String> logros = new ArrayList<>();
        logros.add("Primer Entrenamiento");
        logros.add("Concentrado");
        logros.add("Principiante");
        logros.add("Diamante");
        logros.add("Profesional");
        logros.add("Maestro");

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
