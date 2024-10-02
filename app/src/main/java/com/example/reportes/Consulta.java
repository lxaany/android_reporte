package com.example.reportes;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.DefaultRetryPolicy;

import java.util.HashMap;
import java.util.Map;

public class Consulta extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2/consultas.php";
    private Map<String, String> params;

    private static final String TAG = "Consulta";

    public Consulta(int ci, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();
        Log.d(TAG, "CI: " + ci);
        params.put("ci", String.valueOf(ci));

        DefaultRetryPolicy policy = new DefaultRetryPolicy(
                5000, // 90 seconds timeout
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        this.setRetryPolicy(policy);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
