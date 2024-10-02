package com.example.reportes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;


public class Ver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        // Recuperar los datos del Intent
        ArrayList<String> reportes = getIntent().getStringArrayListExtra("reportes");
        String nombre = getIntent().getStringExtra("nombre");
        String especialidad = getIntent().getStringExtra("especialidad");
        // Referencia al TableLayout
        TableLayout tableLayout = findViewById(R.id.tableLayoutReportes);
        TextView alumno = findViewById(R.id.alumno);
        // Verificar que se recibieron los datos
        alumno.setText(nombre + " = " + especialidad);
        if (reportes != null) {
            // Para cada reporte, crear una fila en la tabla
            for (String reporte : reportes) {
                // Crear un TableRow
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                // Dividir el reporte en partes (en este caso, asumo que tienes datos por separado)
                String[] partes = reporte.split("\n");

                // Crear TextViews para cada parte del reporte
                for (String parte : partes) {
                    TextView textView = new TextView(this);
                    textView.setText(parte);
                    textView.setPadding(8, 8, 8, 8);

                    // Configurar el TextView para que el texto largo se ajuste automáticamente (salto de línea)
                    textView.setMaxLines(5);  // Opcional: Puedes ajustar el número de líneas según necesites
                    textView.setEllipsize(TextUtils.TruncateAt.END);  // Para truncar si es necesario
                    textView.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    // Asegurarse que el TextView sea responsivo
                    textView.setLayoutParams(new TableRow.LayoutParams(
                            0, TableRow.LayoutParams.WRAP_CONTENT, 1f));  // Se ajusta a la proporción disponible

                    tableRow.addView(textView);
                }

                // Agregar el TableRow a la tabla
                tableLayout.addView(tableRow);
            }
        } else {
            Log.d("Error", "No se recibieron datos.");
        }
    }
}