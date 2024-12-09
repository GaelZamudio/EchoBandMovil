package com.example.echobandapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class lvAdapterAmigos extends BaseAdapter {

    private Context contexto;
    private String[] nombre;
    private int[] foto;
    private LayoutInflater inflater;
    private String[] rango;
    private String[] puntos;

    public lvAdapterAmigos(Context contexto, String[] nombre, int[] foto, String[] rango, String[] puntos) {
        this.contexto = contexto;
        this.nombre = nombre;
        this.foto = foto;
        this.inflater = LayoutInflater.from(contexto);
        this.rango = rango;
        this.puntos = puntos;
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
            view = inflater.inflate(R.layout.lv_adapter_amigos, viewGroup, false);
        }
        TextView texto1 = view.findViewById(R.id.tv_nombre);
        TextView texto11 = view.findViewById(R.id.tv_rango);
        TextView texto111 = view.findViewById(R.id.tv_puntos);
        ImageView imagen1 = view.findViewById(R.id.iv_imagenperfil);

        texto1.setText(nombre[i]);
        texto11.setText(rango[i]);
        texto111.setText(puntos[i]);
        imagen1.setImageResource(foto[i]);

        return view;
    }
}
