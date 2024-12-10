package com.example.echobandapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelper {

    private static final String TAG = "McQueen";
    private static final UUID HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // UUID estándar para HC-05

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private boolean isConnected;
    private Context context;

    private ArrayList<Integer> processedData; // Lista para almacenar los valores procesados

    public interface PermissionCallback {
        void onPermissionNeeded();
    }

    private PermissionCallback permissionCallback;

    public BluetoothHelper(Context context, PermissionCallback permissionCallback) {
        this.context = context;
        this.permissionCallback = permissionCallback;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new IllegalStateException("Bluetooth no está disponible en este dispositivo");
        }
        processedData = new ArrayList<>(); // Inicializamos la lista
    }

    public boolean connectToDevice(String deviceName) {
        try {
            if (!bluetoothAdapter.isEnabled()) {
                throw new IllegalStateException("Bluetooth no está habilitado");
            }

            // Verificar permisos en Android 12+
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permiso BLUETOOTH_CONNECT no otorgado.");
                if (permissionCallback != null) {
                    permissionCallback.onPermissionNeeded();
                }
                return false;
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(deviceName)) {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(HC05_UUID);
                    bluetoothSocket.connect();
                    inputStream = bluetoothSocket.getInputStream();
                    isConnected = true;
                    Log.i(TAG, "Conexión exitosa con " + deviceName);
                    return true;
                }
            }
            Log.e(TAG, "Dispositivo " + deviceName + " no encontrado entre los emparejados");
        } catch (IOException e) {
            Log.e(TAG, "Error al conectar con " + deviceName, e);
        }
        return false;
    }

    public void startListening() throws IllegalStateException {
        if (!isConnected || inputStream == null) {
            throw new IllegalStateException("No hay conexión activa para escuchar datos");
        }

        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                int bytes;
                while (isConnected && (bytes = inputStream.read(buffer)) > 0) {
                    String receivedData = new String(buffer, 0, bytes);
                    Log.d(TAG, "Datos recibidos: " + receivedData);

                    // Convertir los datos recibidos a un ArrayList<Integer> después de procesarlos
                    ArrayList<Integer> processedDataFromBluetooth = processReceivedData(receivedData);

                    // Agregar los nuevos datos procesados a la lista
                    processedData.addAll(processedDataFromBluetooth);

                    // Puedes realizar acciones adicionales con los datos procesados
                    // Por ejemplo, notificar a la UI o hacer algún otro tipo de procesamiento
                }
            } catch (IOException e) {
                Log.e(TAG, "Error al leer datos", e);
            }
        }).start();
    }

    private ArrayList<Integer> processReceivedData(String receivedData) {
        // Supongamos que los datos recibidos son números separados por comas
        String[] values = receivedData.split(",");
        float[] floatValues = new float[values.length];

        // Convertir los valores recibidos en floats
        for (int i = 0; i < values.length; i++) {
            // Verificar si el valor es vacío y no procesarlo
            if (values[i].isEmpty() || values[i].equals(".00")) {
                continue; // Si está vacío, pasamos al siguiente valor
            }

            try {
                floatValues[i] = Float.parseFloat(values[i]);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error al parsear el valor: " + values[i], e);
            }
        }

        // Redondear los valores y convertir a int
        return roundAndCastToInt(floatValues);
    }

    private ArrayList<Integer> roundAndCastToInt(float[] values) {
        ArrayList<Integer> roundedValues = new ArrayList<>();

        for (float value : values) {
            if (value>2 && value<100){
                // Redondear y hacer el cast a int
                roundedValues.add(Math.round(value));
            }

        }

        return roundedValues;
    }

    // Método para obtener los datos procesados
    public ArrayList<Integer> getProcessedData() {
        return new ArrayList<>(processedData); // Devolvemos una copia para evitar modificaciones externas
    }

    public void disconnect() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            isConnected = false;
            Log.i(TAG, "Conexión cerrada");
        } catch (IOException e) {
            Log.e(TAG, "Error al cerrar la conexión", e);
        }
    }

    public boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
}