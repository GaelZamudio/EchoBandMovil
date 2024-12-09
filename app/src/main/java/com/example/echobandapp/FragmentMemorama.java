package com.example.echobandapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentMemorama extends Fragment {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private TextView timeRemainingLabel;
    private GridLayout memoryGrid;
    private ImageView[] imageViews;
    private BluetoothHelper bluetoothHelper;
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

        // Inicializar el BluetoothHelper
        bluetoothHelper = new BluetoothHelper(getContext(), null);

        // Inicializar las vistas
        timeRemainingLabel = view.findViewById(R.id.tv_timer);
        memoryGrid = view.findViewById(R.id.memorama_grid);

        // Verificar el permiso de Bluetooth directamente en onCreateView
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene el permiso, solicitamos el permiso
            requestBluetoothPermission();
        } else {
            // Si el permiso ya está concedido, continuar con la inicialización
            initializeGameAndStartBluetoothConnection(view);
        }

        return view;
    }

    private void initializeGameAndStartBluetoothConnection(View view) {
        try {
            // Inicializamos el juego
            initializeGame(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startCountdown();

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
    }

    private void initializeGame(View view) throws IOException {

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
                ArrayList<Integer> processedData = bluetoothHelper.getProcessedData();
                int maximo = processedData.get(0);
                int minimo = processedData.get(0);
                int suma = 0;
                for (int dato : processedData){
                    if (dato>maximo){
                        maximo = dato;
                    }

                    if (dato<minimo){
                        minimo = dato;
                    }

                    suma+=dato;
                }

                float promedio = suma/processedData.size();
                int promedioRedondeado = Math.round(promedio);

                SharedPreferences preferences = getActivity().getSharedPreferences("EchoBandPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("minimo", minimo);
                editor.putInt("maximo", maximo);
                editor.putInt("promedio", promedioRedondeado);
                editor.apply();
                bluetoothHelper.disconnect();
                Toast.makeText(getContext(), String.valueOf(processedData.get(1)), Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentGanado1())
                        .commit();
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso fue otorgado, proceder con la inicialización y conexión Bluetooth
                initializeGameAndStartBluetoothConnection(getView());
            } else {
                // Si el permiso fue denegado, informar al usuario
                Toast.makeText(getContext(), "Permiso Bluetooth necesario para continuar", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
