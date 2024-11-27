package com.example.echobandapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class FragmentReflejos extends Fragment {

    private View circulo;
    private Button btnListo, btnPresiona;
    private TextView tvTimer;
    private boolean puedePresionar = false;
    private long tiempoInicio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_reflejosfragment, container, false);

        // Inicializar vistas
        circulo = rootView.findViewById(R.id.v_circulo);
        btnListo = rootView.findViewById(R.id.btn_listo);
        btnPresiona = rootView.findViewById(R.id.btn_presiona);
        tvTimer = rootView.findViewById(R.id.tv_timer);

        // Configurar listeners
        btnListo.setOnClickListener(view -> iniciarJuego());
        btnPresiona.setOnClickListener(view -> calcularTiempo());

        return rootView;
    }

    private void iniciarJuego() {
        puedePresionar = false;
        tvTimer.setText(" ");
        circulo.setBackgroundColor(Color.GRAY);

        // Retraso aleatorio para empezar al presionar el botón azul
        int retraso = new Random().nextInt(3000) + 2000; // Entre 2 y 5 segundos
        new Handler().postDelayed(() -> {
            circulo.setBackgroundColor(Color.GREEN);
            tiempoInicio = System.currentTimeMillis();
            puedePresionar = true;
        }, retraso);
    }

    private void calcularTiempo() {
        if (puedePresionar) {
            long tiempoFinal = System.currentTimeMillis();
            double tiempoReaccion = (tiempoFinal - tiempoInicio) / 1000.0;
            tvTimer.setText(String.format("%.3f segundos", tiempoReaccion));
            puedePresionar = false;

            // Deshabilitar el botón azul
            btnListo.setEnabled(false);

            // Determinar el resultado
            new Handler().postDelayed(() -> {
                if (getActivity() != null) {
                    if (tiempoReaccion <= 1.0) { // Si el tiempo es menor o igual a 1 segundo, ganó
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentGanado2())
                                .commit();
                    } else { // Si el tiempo es mayor, perdió
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentTerminado2())
                                .commit();
                    }
                }
            }, 4000);

        } else {
            tvTimer.setText("¡Espera la luz verde!"); // Advertencia si el jugador se adelanta
        }
    }

}
