package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentConfiguracion extends Fragment implements View.OnClickListener {

    private ImageView profilePhoto;
    private EditText nombreInput, correoInput, contrasenaInput;
    Button btnGuardarCambios;
    TextView tvNombre, tvCorreo;
    //private Switch logoutSwitch, remindersSwitch, notificationsSwitch, themeSwitch;

    // Interfaz para poder comunicarnos con Principal
    public interface OnPerfilUpdatedListener {
        void onPerfilUpdated(String nuevoNombre, String nuevoCorreo);
    }

    private OnPerfilUpdatedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPerfilUpdatedListener) {
            listener = (OnPerfilUpdatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPerfilUpdatedListener");
        }
    }

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
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambios);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvCorreo = view.findViewById(R.id.tvCorreo);

        btnGuardarCambios.setOnClickListener(this);
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
        tvNombre.setText(savedName);
        tvCorreo.setText(savedEmail);


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

    @Override
    public void onClick(View v) {
        // OBTENEMOS LAS PREFS
        SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
        int id_usuario = preferences.getInt("id_usuario", 0);

        if (v.getId() == R.id.btnGuardarCambios) {
            String nuevoNombre = nombreInput.getText().toString().trim();
            String nuevoCorreo = correoInput.getText().toString().trim();
            String nuevaContrasena = contrasenaInput.getText().toString().trim();

            // Verificamos que los campos no estén vacíos
            if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty() || nuevaContrasena.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación del nombre
            if (!esNombreValido(nuevoNombre)) {
                Toast.makeText(getActivity(), "Nombre no válido, prueba con otro", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación del correo
            if (!esCorreoValido(nuevoCorreo)) {
                Toast.makeText(getActivity(), "Por favor, ingresa un correo válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación de la contraseña
            if (!esContrasenaValida(nuevaContrasena)) {
                Toast.makeText(getActivity(), "Por favor, ingresa una contraseña válida", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el nombre ya existe
            if (existeUsuario(nuevoNombre) && !nuevoNombre.equals(preferences.getString("nombre", ""))) {
                Toast.makeText(getActivity(), "Este nombre está ocupado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el correo ya está registrado
            if (estaRegistradoCorreo(nuevoCorreo) && !nuevoCorreo.equals(preferences.getString("correo", ""))) {
                Toast.makeText(getActivity(), "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar datos en la base de datos
            Base base = new Base(getContext(), "EchoBandDB", null, 1);
            base.actualizarUsuario(id_usuario, nuevoNombre, nuevoCorreo, nuevaContrasena);

            // Actualizar preferencias
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("nombre", nuevoNombre);
            editor.putString("correo", nuevoCorreo);
            editor.putString("contrasena", nuevaContrasena);
            editor.putInt("id_usuario", id_usuario);
            editor.apply();

            // Actualizar la UI
            tvCorreo.setText(nuevoCorreo);
            tvNombre.setText(nuevoNombre);

            if (listener != null) {
                listener.onPerfilUpdated(nuevoNombre, nuevoCorreo);
            }

            Toast.makeText(getActivity(), "Se actualizó el perfil", Toast.LENGTH_SHORT).show();
        }
    }

    // Métodos de validación (igual que en el SignUp)
    private boolean esCorreoValido(String correo) {
        String patronCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";
        return correo.matches(patronCorreo);
    }

    private boolean esNombreValido(String nombre) {
        return nombre.matches("^[a-zA-Z0-9]+$");
    }

    private boolean esContrasenaValida(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$");
    }

    private boolean existeUsuario(String nombre){
        Base admin = new Base(getContext(), "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM Usuario WHERE nombre = ?", new String[]{nombre});

        if (cursor.moveToFirst()){
            base.close();
            cursor.close();
            return true;
        } else {
            base.close();
            cursor.close();
            return false;
        }
    }

    private boolean estaRegistradoCorreo(String correo){
        Base admin = new Base(getContext(), "EchoBandDB", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        Cursor cursor = base.rawQuery("SELECT * FROM Usuario WHERE correo = ?", new String[]{correo});

        if (cursor.moveToFirst()){
            base.close();
            cursor.close();
            return true;
        } else {
            base.close();
            cursor.close();
            return false;
        }
    }

}
