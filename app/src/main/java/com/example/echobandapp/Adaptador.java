package com.example.echobandapp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Adaptador extends ArrayAdapter<String> {

    private final Context context;
    private final int[] imagenes;

    public Adaptador(Context context, String[] titulos, int[] imagenes) {
        super(context, R.layout.fragment_entrenarfragment, titulos);
        this.context = context;
        this.imagenes = imagenes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(16, 16, 16, 16);

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 500);
        params.setMargins(85, 0, 0, 0);
        params.gravity = Gravity.END;
        imageView.setLayoutParams(params);
        imageView.setImageResource(imagenes[position]);

        layout.addView(imageView);

        return layout;
    }
}
