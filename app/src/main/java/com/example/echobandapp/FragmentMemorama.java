package com.example.echobandapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentMemorama extends Fragment {

    private TextView timeRemainingLabel;
    private GridLayout memoryGrid;
    private ImageView[] imageViews;
    private int[] frontImages = {
            R.drawable.entremosencalorentrenar19, R.drawable.entremosencalorentrenar19,
            R.drawable.entremosencalorentrenar17, R.drawable.entremosencalorentrenar17,
            R.drawable.entremosencalorentrenar11, R.drawable.entremosencalorentrenar11,
            R.drawable.entremosencalorentrenar12, R.drawable.entremosencalorentrenar12,
            R.drawable.entremosencalorentrenar13, R.drawable.entremosencalorentrenar13,
            R.drawable.entremosencalorentrenar16, R.drawable.entremosencalorentrenar16,
            R.drawable.entremosencalorentrenar14, R.drawable.entremosencalorentrenar14,
            R.drawable.entremosencalorentrenar15, R.drawable.entremosencalorentrenar15,
            R.drawable.entremosencalorentrenar18, R.drawable.entremosencalorentrenar18,
            R.drawable.entremosencalorentrenar20, R.drawable.entremosencalorentrenar20,
            R.drawable.entremosencalorentrenar21, R.drawable.entremosencalorentrenar21,
            R.drawable.entremosencalorentrenar22, R.drawable.entremosencalorentrenar22
    };

    private boolean[] flipped;
    private int firstFlippedIndex = -1;
    private int secondFlippedIndex = -1;
    private int timeRemaining = 60;

    private CountDownTimer countdownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_memoramafragment, container, false);

        // Inicializar las vistas
        timeRemainingLabel = view.findViewById(R.id.tv_timer);
        memoryGrid = view.findViewById(R.id.memorama_grid);

        initializeGame(view);
        startCountdown();

        return view;
    }

    private void initializeGame(View view) {
        // Mezclar cartas
        List<Integer> imageList = new ArrayList<>();
        for (int image : frontImages) {
            imageList.add(image);
        }
        Collections.shuffle(imageList);
        frontImages = imageList.stream().mapToInt(i -> i).toArray();

        // Configurar las cartas
        flipped = new boolean[frontImages.length];
        imageViews = new ImageView[frontImages.length];

        // Obtener las dimensiones de pantalla
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int cardSize = metrics.widthPixels / 5 ;

        for (int i = 0; i < frontImages.length; i++) {
            final int index = i;

            // Crear ImageView dinámicamente
            ImageView imageView = new ImageView(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cardSize;
            params.height = cardSize;
            params.setMargins(cardSize/16, 10, cardSize/16, 10);
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(getResources().getColor(R.color.back_card, null));
            imageView.setOnClickListener(view1 -> flipCard(index));

            memoryGrid.addView(imageView);
            imageViews[i] = imageView;
        }

        // Actualizar el diseño del GridLayout
        memoryGrid.invalidate();
        memoryGrid.requestLayout();
    }

    private void startCountdown() {
        countdownTimer = new CountDownTimer(timeRemaining * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining--;
                timeRemainingLabel.setText(timeRemaining + " segundos");
            }

            @Override
            public void onFinish() {
                timeRemainingLabel.setText("Tiempo terminado");
                // Aquí puedes usar una navegación o callback para manejar el "Juego terminado"
                // Ejemplo con un callback al activity contenedor
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new FragmentTerminado1())
                            .commit();
                }
            }
        };
        countdownTimer.start();
    }

    private void flipCard(int index) {
        if (!flipped[index] && secondFlippedIndex == -1) {
            imageViews[index].setImageResource(frontImages[index]);
            flipped[index] = true;

            if (firstFlippedIndex == -1) {
                firstFlippedIndex = index;
            } else {
                secondFlippedIndex = index;
                if (frontImages[firstFlippedIndex] == frontImages[secondFlippedIndex]) {
                    // Son iguales
                    checkGameComplete();
                } else {
                    // Si las cartas no coinciden, se ocultan después de un delay
                    imageViews[index].postDelayed(() -> resetCards(firstFlippedIndex, secondFlippedIndex), 1000);
                }
            }
        }
    }

    private void resetCards(int index1, int index2) {
        imageViews[index1].setBackgroundColor(getResources().getColor(R.color.back_card, null));
        imageViews[index1].setImageDrawable(null);
        imageViews[index2].setBackgroundColor(getResources().getColor(R.color.back_card, null));
        imageViews[index2].setImageDrawable(null);
        flipped[index1] = false;
        flipped[index2] = false;
        firstFlippedIndex = -1;
        secondFlippedIndex = -1;
    }

    private void checkGameComplete() {
        firstFlippedIndex = -1;
        secondFlippedIndex = -1;

        boolean allFlipped = true;
        for (boolean isFlipped : flipped) {
            if (!isFlipped) {
                allFlipped = false;
                break;
            }
        }

        if (allFlipped) {
            countdownTimer.cancel();
            // Aquí puedes navegar al fragmento de "Ganado"
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentGanado1())
                        .commit();
            }
        }
    }
}
