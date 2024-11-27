package com.example.echobandapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentSimonDice extends Fragment {

    private TextView messageText;
    private ImageButton arrowUp, arrowDown, arrowLeft, arrowRight;
    private List<String> sequence = new ArrayList<>();
    private List<String> userInput = new ArrayList<>();
    private int currentRound = 1;
    private int currentInputIndex = 0;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simondicefragment, container, false);

        messageText = view.findViewById(R.id.tv_mensaje);
        arrowUp = view.findViewById(R.id.ib_arrow_up);
        arrowDown = view.findViewById(R.id.ib_arrow_down);
        arrowLeft = view.findViewById(R.id.ib_arrow_left);
        arrowRight = view.findViewById(R.id.ib_arrow_right);

        arrowUp.setOnClickListener(v -> handleInput("ARRIBA"));
        arrowDown.setOnClickListener(v -> handleInput("ABAJO"));
        arrowLeft.setOnClickListener(v -> handleInput("IZQUIERDA"));
        arrowRight.setOnClickListener(v -> handleInput("DERECHA"));

        startGame();

        return view;
    }

    private void startGame() {
        sequence.clear();
        currentRound = 1;
        nextRound();
    }

    private void nextRound() {
        userInput.clear();
        currentInputIndex = 0;
        messageText.setText("");
        generateSequence();

        // Retraso inicial de 4 segundos antes de mostrar la primera instrucción
        handler.postDelayed(this::showSequence, 4000);
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
                if (currentRound == 10) { // Límite de rondas para "ganar"
                    messageText.setText("¡Correcto! Has completado todas las rondas.");
                    handler.postDelayed(() -> {
                        // Navegar al fragmento de "ganado"
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new FragmentGanado3())
                                    .commit();
                        }
                    }, 3000);
                } else {
                    messageText.setText("¡Correcto! Siguiente ronda.");
                    currentRound++;
                    handler.postDelayed(this::nextRound, 3000);
                }
            }
        }
    }

}
