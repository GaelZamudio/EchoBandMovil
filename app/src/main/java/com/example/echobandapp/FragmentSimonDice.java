package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentSimonDice extends Fragment {

    private TextView messageText, rondaText;
    private ImageButton arrowUp, arrowDown, arrowLeft, arrowRight;
    private List<String> sequence = new ArrayList<>();
    private List<String> userInput = new ArrayList<>();
    private int currentRound = 1;
    private int currentInputIndex = 0;
    private Handler handler = new Handler();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    BluetoothHelper bluetoothHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simondicefragment, container, false);

        messageText = view.findViewById(R.id.tv_mensaje);
        rondaText = view.findViewById(R.id.tv_ronda);
        arrowUp = view.findViewById(R.id.ib_arrow_up);
        arrowDown = view.findViewById(R.id.ib_arrow_down);
        arrowLeft = view.findViewById(R.id.ib_arrow_left);
        arrowRight = view.findViewById(R.id.ib_arrow_right);

        arrowUp.setOnClickListener(v -> handleInput("ARRIBA"));
        arrowDown.setOnClickListener(v -> handleInput("ABAJO"));
        arrowLeft.setOnClickListener(v -> handleInput("IZQUIERDA"));
        arrowRight.setOnClickListener(v -> handleInput("DERECHA"));

        preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
        bluetoothHelper = new BluetoothHelper(getActivity(), null);
        editor = preferences.edit();

        // Verificar si Bluetooth está habilitado
        if (bluetoothHelper.isBluetoothEnabled()) {
            // Intentar conectar al dispositivo directamente
            boolean conectado = bluetoothHelper.connectToDevice("McQueen");  // Asume que "McQueen" es el nombre del dispositivo
            if (conectado) {
                Toast.makeText(getContext(), "Conexión exitosa a EchoBand", Toast.LENGTH_SHORT).show();
                bluetoothHelper.startListening();
            } else {
                Toast.makeText(getContext(), "Por favor, vincula tu EchoBand", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentConCalma())
                        .commit();
            }
        } else {
            // Si Bluetooth no está habilitado, mostrar mensaje
            Toast.makeText(getContext(), "Bluetooth no está habilitado. Por favor, actívalo.", Toast.LENGTH_SHORT).show();
        }

        startGame();

        return view;
    }

    private void startGame() {
        sequence.clear();
        currentRound = 1;
        rondaText.setText("1");
        nextRound();
    }

    private void nextRound() {
        userInput.clear();
        currentInputIndex = 0;
        messageText.setText("");
        rondaText.setText("Ronda: " + currentRound);
        generateSequence();

        // Retraso inicial de 4 segundos antes de mostrar la primera instrucción
        handler.postDelayed(this::showSequence, 2000);
    }

    private void generateSequence() {
        String[] directions = {"ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"};
        Random random = new Random();
        for (int i = 0; i < currentRound; i++) {
            sequence.add(directions[random.nextInt(directions.length)]);
        }
    }

    private void showSequence() {
        messageText.setText("");
        int delay = 0; // Tiempo inicial para las instrucciones

        for (int i = 0; i < sequence.size(); i++) {
            String direction = sequence.get(i);

            // Mostrar cada instrucción con un retraso
            handler.postDelayed(() -> messageText.setText(direction), delay);
            delay += 1500; // Mostrar cada instrucción por 1.5 segundos

            // Borrar la instrucción después de mostrarla
            handler.postDelayed(() -> messageText.setText(""), delay - 500); // 500 ms de margen
        }

        // Después de mostrar la secuencia, permitir al usuario ingresar
        handler.postDelayed(() -> messageText.setText("Ingresa la secuencia"), delay);
    }

    private void gameOver() {
        messageText.setText("¡Incorrecto! Fin del juego.");
        handler.postDelayed(() -> {
            // Navegar al fragmento de "perdido"
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentTerminado3())
                        .commit();
            }
        }, 2000);
    }

    private void handleInput(String direction) {
        if (currentInputIndex < sequence.size()) {
            userInput.add(direction);
            if (!userInput.get(currentInputIndex).equals(sequence.get(currentInputIndex))) {
                gameOver();
                return;
            }
            currentInputIndex++;

            if (currentInputIndex == sequence.size()) {
                if (currentRound == 5) { // Límite de rondas para "ganar"
                    messageText.setText("¡Correcto! Has completado todas las rondas.");
                    handler.postDelayed(() -> {
                        // Navegar al fragmento de "ganado"
                        if (getActivity() != null) {
                            ArrayList<Integer> processedData = bluetoothHelper.getProcessedData();
                            int maximo = processedData.get(0);
                            int minimo = processedData.get(0);
                            int suma = 0;
                            for (int dato : processedData) {
                                if (dato > maximo) {
                                    maximo = dato;
                                }

                                if (dato < minimo) {
                                    minimo = dato;
                                }
                                suma += dato;
                            }

                            float promedio = suma / processedData.size();
                            int promedioRedondeado = Math.round(promedio);

                            SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("minimo", minimo);
                            editor.putInt("maximo", maximo);
                            editor.putInt("promedio", promedioRedondeado);
                            editor.apply();
                            bluetoothHelper.disconnect();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new FragmentGanado3())
                                    .commit();
                        }
                    }, 3000);
                } else {
                    messageText.setText("¡Correcto! Siguiente ronda.");
                    currentRound++;
                    handler.postDelayed(this::nextRound, 2000);
                }
            }
        }
    }

}
