package com.example.reportes;

import static android.content.ContentValues.TAG;

import android.content.Intent; // Asegúrate de que esta línea esté presente

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCI;
    private Button buttonEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar las vistas
        editTextCI = findViewById(R.id.ci);
        buttonEnviar = findViewById(R.id.button);

        // Configurar el botón para enviar la petición
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ci = editTextCI.getText().toString();  // Obtener el texto del EditText

                // Hacer la petición en un nuevo hilo
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = enviarPeticion(ci);  // Ejecuta el envío en segundo plano

                        // Actualizar la interfaz de usuario en el hilo principal
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                procesarRespuesta(result);
                                // Mostrar el resultado en el TextView
                            }
                        });
                    }
                }).start();
            }
        });
    }

    // Método para enviar la petición POST y recibir la respuesta
    private String enviarPeticion(String ci_value) {
        try {
            URL url = new URL("http://10.0.2.2/consultas.php");  // Cambia la IP según tu servidor

            // Configurar la conexión
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // Escribir los parámetros en la solicitud POST
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("ci", "UTF-8") + "=" + URLEncoder.encode(ci_value, "UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();

            // Leer la respuesta del servidor
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();  // Devolver la respuesta como cadena

        } catch (Exception e) {
            Log.d(TAG, "Error :(");
            return "Error: " + e.getMessage();
        }
    }

        private void procesarRespuesta(String json) {
            try {
                Log.d("Respuesta Completa", json);

                // Crear el objeto JSON a partir de la respuesta
                JSONObject jsonResponse = new JSONObject(json);

                // Verifica si la respuesta fue exitosa
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    JSONArray data = jsonResponse.getJSONArray("data");
                    ArrayList<String> reportes = new ArrayList<>();

                    // Itera sobre cada objeto del array 'data'
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject reporte = data.getJSONObject(i);
                        String horario = reporte.getString("horario");
                        String fecha = reporte.getString("fecha");
                        String nombreMateria = reporte.getString("nombre_materia");
                        String rasgoConductual = reporte.getString("rasgo_conductual");

                        // Formatear los datos como un String y agregarlo a la lista
                        String reporteStr =
                                horario + "\n" +
                                fecha + "\n" +
                                nombreMateria + "\n" +
                                rasgoConductual;
                        reportes.add(reporteStr);
                    }
                    JSONObject reporte = data.getJSONObject(0);
                    // Crear un Intent para pasar los datos a la actividad VerActivity
                    Intent intent = new Intent(MainActivity.this, Ver.class);

                    intent.putExtra("nombre", reporte.getString("nombre_alumno"));
                    intent.putExtra("especialidad", reporte.getString("especialidad"));
                    intent.putStringArrayListExtra("reportes", reportes);

                    // Iniciar la actividad
                    startActivity(intent);

                    startActivity(intent);
                } else {
                    Log.d(TAG, "No se encontraron registros.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Error al procesar la respuesta.");
            }
        }

    }