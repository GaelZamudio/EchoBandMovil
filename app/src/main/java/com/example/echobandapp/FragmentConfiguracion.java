package com.example.echobandapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentConfiguracion extends Fragment {

    private ImageView profilePhoto;
    private EditText nombreInput, correoInput, contrasenaInput;
    //private Switch logoutSwitch, remindersSwitch, notificationsSwitch, themeSwitch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracionfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePhoto = view.findViewById(R.id.profile_photo);
        nombreInput = view.findViewById(R.id.nombre_input);
        correoInput = view.findViewById(R.id.correo_input);
        contrasenaInput = view.findViewById(R.id.contrasenaInput);
        /*
        logoutSwitch = view.findViewById(R.id.logout_switch);
        remindersSwitch = view.findViewById(R.id.training_reminders_switch);
        notificationsSwitch = view.findViewById(R.id.general_notifications_switch);
        themeSwitch = view.findViewById(R.id.light_theme_switch);
        */
        SharedPreferences preferences = requireActivity().getSharedPreferences("EchoBandPrefs", requireActivity().MODE_PRIVATE);
        String savedName = preferences.getString("nombre", "Nombre no disponible");
        String savedEmail = preferences.getString("correo", "Correo no disponible");
        String savedUsername = preferences.getString("contrasena", "Contraseña no disponible");

        nombreInput.setText(savedName);
        correoInput.setText(savedEmail);
        contrasenaInput.setText(savedUsername);


        /*
        logoutSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), isChecked ? "Cerrar sesión activado" : "Cerrar sesión desactivado", Toast.LENGTH_SHORT).show();
        });

        remindersSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), isChecked ? "Recordatorios de entrenamiento activado" : "Recordatorios de entrenamiento desactivado", Toast.LENGTH_SHORT).show();
        });

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), isChecked ? "Notificaciones activadas" : "Notificaciones desactivadas", Toast.LENGTH_SHORT).show();
        });

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), isChecked ? "Tema claro activado" : "Tema claro desactivado", Toast.LENGTH_SHORT).show();
        });
        */
    }
}
