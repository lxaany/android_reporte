package com.example.reportes;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.DefaultRetryPolicy;

import java.util.HashMap;
import java.util.Map;

public class Consulta extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2/Reportes/consultas.php";
    private Map<String, String> params;

    // 定义日志标记
    private static final String TAG = "Consulta";

    public Consulta(int ci, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);

        // 设置参数并打印日志
        params = new HashMap<>();
        Log.d(TAG, "CI 参数值: " + ci);  // 使用 Log.d() 打印日志
        params.put("ci", String.valueOf(ci)); // 只发送 ci 参数

        // 设置重试策略
        DefaultRetryPolicy policy = new DefaultRetryPolicy(
                90000, // 90 seconds timeout
                3, // 最大重试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        this.setRetryPolicy(policy);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
