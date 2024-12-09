package com.example.echobandapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class LineChartView extends View {

    private Paint linePaint;
    private Paint pointPaint;
    private ArrayList<Integer> dataPoints = new ArrayList<>();
    private float minValue = 0; // Valor mínimo del eje Y
    private float maxValue = 100; // Valor máximo del eje Y

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Configuración de la línea
        linePaint = new Paint();
        linePaint.setColor(0xFF3e9fc8); // Color azul
        linePaint.setStrokeWidth(8);
        linePaint.setStyle(Paint.Style.STROKE);

        // Configuración de los puntos
        pointPaint = new Paint();
        pointPaint.setColor(0xFF436cda); // Color morado
        pointPaint.setStrokeWidth(12);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.size() < 2) return;

        float width = getWidth();
        float height = getHeight();
        float padding = 50;

        float xStep = (width - 2 * padding) / (dataPoints.size() - 1);
        float yScale = (height - 2 * padding) / (maxValue - minValue);

        // Dibujar el eje Y con marcas
        Paint textPaint = new Paint();
        textPaint.setColor(0xFF888888); // Gris
        textPaint.setTextSize(30);

        Paint linePaintGrid = new Paint();
        linePaintGrid.setColor(0xFFDDDDDD); // Líneas de guía en gris claro
        linePaintGrid.setStrokeWidth(2);

        // Número de divisiones del eje Y (10 unidades por división)
        int divisions = 10;
        float stepValue = maxValue / divisions;

        for (int i = 0; i <= divisions; i++) {
            float value = i * stepValue;
            float yPosition = height - padding - ((value - minValue) * yScale);

            // Dibujar línea de guía horizontal
            canvas.drawLine(padding, yPosition, width - padding, yPosition, linePaintGrid);

            // Dibujar la etiqueta del eje Y
            String label = String.valueOf((int) value);
            canvas.drawText(label, padding / 2, yPosition + 10, textPaint);
        }

        // Dibujar líneas entre los puntos
        for (int i = 0; i < dataPoints.size() - 1; i++) {
            float startX = padding + i * xStep;
            float startY = height - padding - ((dataPoints.get(i) - minValue) * yScale);
            float endX = padding + (i + 1) * xStep;
            float endY = height - padding - ((dataPoints.get(i+1) - minValue) * yScale);

            canvas.drawLine(startX, startY, endX, endY, linePaint);
        }

        // Dibujar puntos
        for (int i = 0; i < dataPoints.size(); i++) {
            float x = padding + i * xStep;
            float y = height - padding - ((dataPoints.get(i) - minValue) * yScale);
            canvas.drawCircle(x, y, 10, pointPaint);
        }
    }


    public void setDataPoints(ArrayList<Integer> dataPoints) {
        this.dataPoints = dataPoints;
        invalidate(); // Redibujar la vista con los nuevos datos
    }

    public void setVerticalRange(float minValue, float maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        Canvas canvas = new Canvas();
        draw(canvas); // Redibujar la vista con el nuevo rango
    }
}
