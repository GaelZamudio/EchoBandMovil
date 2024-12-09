package com.example.echobandapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelper {
    private static final String TAG = "BluetoothHelper";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private ArrayList<Integer> roundedDataList = new ArrayList<>(); // Lista para datos redondeados
    private boolean isConnected = false;

    public BluetoothHelper() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            throw new UnsupportedOperationException("Bluetooth no soportado en este dispositivo");
        }
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void connectToDevice(String deviceName) throws IOException {
        if (!isBluetoothEnabled()) {
            throw new IllegalStateException("Bluetooth no está habilitado");
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(deviceName)) {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                isConnected = true;
                Log.i(TAG, "Conexión exitosa con " + deviceName);
                return;
            }
        }
        throw new IOException("Dispositivo " + deviceName + " no encontrado");
    }

    public void startListening() throws IOException {
        if (!isConnected || inputStream == null) {
            throw new IllegalStateException("No hay conexión activa para escuchar datos");
        }

        new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;

            while (isConnected) {
                try {
                    bytes = inputStream.read(buffer);
                    String receivedData = new String(buffer, 0, bytes).trim();

                    if (!receivedData.isEmpty()) {
                        try {
                            int roundedValue = (int) Math.round(Double.parseDouble(receivedData));
                            synchronized (roundedDataList) {
                                roundedDataList.add(roundedValue);
                            }
                            Log.i(TAG, "Dato recibido y redondeado: " + roundedValue);
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Dato no válido recibido: " + receivedData);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error al leer datos", e);
                    break;
                }
            }
        }).start();
    }

    public void closeConnection() throws IOException {
        if (bluetoothSocket != null) {
            bluetoothSocket.close();
            bluetoothSocket = null;
            inputStream = null;
            isConnected = false;
            Log.i(TAG, "Conexión cerrada");
        }
    }

    public ArrayList<Integer> getRoundedDataList() {
        synchronized (roundedDataList) {
            return new ArrayList<>(roundedDataList);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
