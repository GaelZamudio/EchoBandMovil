package com.example.echobandapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class lvAdapterLiga extends BaseAdapter {

    private Context contexto;
    private String[] nombre;
    private int[] foto;
    private LayoutInflater inflater;
    private String[] rango;
    private String[] puntos;
    private String[] lugar;

    private final int MAX_PUNTOS = 5000;

    public lvAdapterLiga(Context contexto, String[] nombre, int[] foto, String[] rango, String[] puntos, String[] lugar) {
        this.contexto = contexto;
        this.nombre = nombre;
        this.foto = foto;
        this.inflater = LayoutInflater.from(contexto);
        this.rango = rango;
        this.puntos = puntos;
        this.lugar = lugar;
    }


    @Override
    public int getCount() {
        return nombre.length;
    }

    @Override
    public Object getItem(int i) {
        return nombre[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.lv_adapter_liga, viewGroup, false);
        }
        TextView nombretx = view.findViewById(R.id.tv_nombre);
        TextView rangotx = view.findViewById(R.id.tv_rango);
        TextView puntostx = view.findViewById(R.id.tv_puntos);
        TextView lugartx = view.findViewById(R.id.tv_lugar);
        ImageView img = view.findViewById(R.id.iv_imagenperfil);
        ProgressBar barra = view.findViewById(R.id.progress_bar);

        nombretx.setText(nombre[i]);
        rangotx.setText(rango[i]);
        puntostx.setText(puntos[i]);
        lugartx.setText(lugar[i]);
        img.setImageResource(foto[i]);

        int puntosActuales = Integer.parseInt(puntos[i]);
        barra.setProgress(puntosActuales);

        return view;
    }
}
