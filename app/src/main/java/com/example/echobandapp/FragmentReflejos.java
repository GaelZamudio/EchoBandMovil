package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class FragmentReflejos extends Fragment {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private View circulo;
    private Button btnListo, btnPresiona;
    private TextView tvTimer;
    private boolean puedePresionar = false;
    private long tiempoInicio;
    SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    BluetoothHelper bluetoothHelper = new BluetoothHelper(getActivity(), null);

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

        // Verificar si Bluetooth está habilitado
        if (bluetoothHelper.isBluetoothEnabled()) {
            // Intentar conectar al dispositivo directamente
            boolean conectado = bluetoothHelper.connectToDevice("McQueen");  // Asume que "McQueen" es el nombre del dispositivo
            if (conectado) {
                Toast.makeText(getContext(), "Conexión exitosa a EchoBand", Toast.LENGTH_SHORT).show();
                bluetoothHelper.startListening();
            } else {
                Toast.makeText(getContext(), "No se pudo conectar a EchoBand", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Si Bluetooth no está habilitado, mostrar mensaje
            Toast.makeText(getContext(), "Bluetooth no está habilitado. Por favor, actívalo.", Toast.LENGTH_SHORT).show();
        }

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
            editor.putFloat("tiempoReaccion", (float) tiempoReaccion);
            editor.apply();
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

    private void requestBluetoothPermission() {
        // Solicitar permisos
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{android.Manifest.permission.BLUETOOTH_CONNECT},
                REQUEST_BLUETOOTH_PERMISSION
        );
    }
}
