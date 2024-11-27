package com.example.echobandapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    }
}
