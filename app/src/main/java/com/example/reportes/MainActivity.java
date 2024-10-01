package com.example.reportes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private EditText ciEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 确保你的布局文件名是正确的

        ciEditText = findViewById(R.id.ci); // 假设你有一个 EditText 的 ID 是 ci
        submitButton = findViewById(R.id.button); // 假设你有一个 Button 的 ID 是 button

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ci = ciEditText.getText().toString().trim();
                if (!ci.isEmpty()) {
                    try {
                        sendRequest(Integer.parseInt(ci)); // 发送请求
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "CI 格式无效，请输入数字", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "请输入有效的 CI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendRequest(int ci) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.getCache().clear(); // 清除缓存

        Consulta consultaRequest = new Consulta(ci, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response); // 添加日志
                Toast.makeText(MainActivity.this, "成功: " + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "请求错误: " + error.toString()); // 添加日志
                if (error.networkResponse != null) {
                    Log.e("Error", "状态码: " + error.networkResponse.statusCode);
                    Log.e("Error", "响应数据: " + new String(error.networkResponse.data));
                }
                Toast.makeText(MainActivity.this, "请求错误: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 将请求加入队列
        requestQueue.add(consultaRequest);
    }
}
