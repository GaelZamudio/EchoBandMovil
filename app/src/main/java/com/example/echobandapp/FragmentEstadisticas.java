package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentEstadisticas extends Fragment {
    int id_usuario;
    String nombre;
    String correo;
    ProgressBar progressRapidez, progressMemorizacion, progressRetencion;
    TextView tvMemorizacionPorcentaje, tvRetencionPorcentaje, tvRapidezPorcentaje;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticasfragment, container, false);

        TextView tvNombre = view.findViewById(R.id.tvNombre);
        TextView tvCorreo = view.findViewById(R.id.tvCorreo);
        progressMemorizacion = view.findViewById(R.id.progressMemorizacion);
        progressRapidez = view.findViewById(R.id.progressRapidez);
        progressRetencion = view.findViewById(R.id.progressRetencion);

        tvMemorizacionPorcentaje = view.findViewById(R.id.tvMemorizacionPorcentaje);
        tvRetencionPorcentaje = view.findViewById(R.id.tvRetencionPorcentaje);
        tvRapidezPorcentaje = view.findViewById(R.id.tvRapidezPorcentaje);

        //OBTENEMOS LAS PREFS
        SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
        id_usuario = preferences.getInt("id_usuario", 0);
        nombre = preferences.getString("nombre", "");
        correo = preferences.getString("correo", "");

        tvNombre.setText(nombre);
        tvCorreo.setText(correo);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtenemos el total de entrenamientos
        Base base = new Base(getContext(), "EchoBandDB", null, 4);
        int totalEntrenamientos = base.obtenerTotalEntrenamientos(id_usuario);

        // Obtenemos el número de entrenamientos por tipo
        int numMemorizacion = base.obtenerEntrenamientosPorTipo(id_usuario, "Memorización");
        int numRapidez = base.obtenerEntrenamientosPorTipo(id_usuario, "Rapidez");
        int numRetencion = base.obtenerEntrenamientosPorTipo(id_usuario, "Retención");

        // Calculamos los porcentajes
        int porcentajeMemorizacion = (totalEntrenamientos > 0) ? (numMemorizacion * 100) / totalEntrenamientos : 0;
        int porcentajeRapidez = (totalEntrenamientos > 0) ? (numRapidez * 100) / totalEntrenamientos : 0;
        int porcentajeRetencion = (totalEntrenamientos > 0) ? (numRetencion * 100) / totalEntrenamientos : 0;

        // Actualizamos los ProgressBar y TextViews con los porcentajes
        progressMemorizacion.setProgress(porcentajeMemorizacion);
        progressRapidez.setProgress(porcentajeRapidez);
        progressRetencion.setProgress(porcentajeRetencion);

        tvMemorizacionPorcentaje.setText(porcentajeMemorizacion + "%");
        tvRapidezPorcentaje.setText(porcentajeRapidez + "%");
        tvRetencionPorcentaje.setText(porcentajeRetencion + "%");

        // Configuramos el gráfico de líneas
        LineChartView lineChartView = view.findViewById(R.id.lineChart);
        ArrayList<Integer> data = base.obtenerConcentraciones(id_usuario, 30);
        lineChartView.setDataPoints(data);
    }

}
