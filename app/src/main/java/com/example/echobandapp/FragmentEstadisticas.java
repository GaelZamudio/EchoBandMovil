package com.example.echobandapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentEstadisticas extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_estadisticasfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar progressFocalizacionVisual = view.findViewById(R.id.progressFocalizacionVisual);
        ProgressBar progressDesafios = view.findViewById(R.id.progressDesafios);
        ProgressBar progressFocalizacionGeneral = view.findViewById(R.id.progressFocalizacionGeneral);
        ProgressBar progressConcentracion = view.findViewById(R.id.progressConcentracion);

        progressFocalizacionVisual.setProgress(81);
        progressDesafios.setProgress(63);
        progressFocalizacionGeneral.setProgress(52);
        progressConcentracion.setProgress(47);

        LineChartView lineChartView = view.findViewById(R.id.lineChart);

        // ESTOS DATOS DEBEN OBTENERSE DE LOS REGISTROS DE ENTRENAMIENTOS DEL USUARIO EN LA BD
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(i);
        }
        /*
                int maximo = data.get(0);
                int minimo = data.get(0);
                int suma = 0;
                for (int i = 1; i < data.size(); i++) {
                    suma += data.get(i);
                }
                int concentracionPromedio = suma / data.size();
                for (int dato : data){
                    if (dato > maximo){
                        maximo = dato;
                    }
                    if (dato < minimo){
                        minimo = dato;
                    }
                }
                */
                lineChartView.setDataPoints(data);
    }
}
