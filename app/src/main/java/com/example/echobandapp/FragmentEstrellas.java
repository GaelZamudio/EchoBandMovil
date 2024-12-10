package com.example.echobandapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentEstrellas extends Fragment {
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estrellasfragment, container, false);

        // Configurar listener para el botón
        view.findViewById(R.id.botoon).setOnClickListener(v -> {
            // Reemplazar este fragmento con el FragmentReflejos
            FragmentReflejos fragmentReflejos = new FragmentReflejos();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentReflejos) // Cambia fragment_container por el ID del contenedor de fragmentos
                    .addToBackStack(null) // Permite regresar al FragmentEstrellas
                    .commit();
        });
        requestBluetoothPermission();
        return view;
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
