package com.v3.security;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Clases.Control;
import com.v3.security.Clases.Guardia;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InformesActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    EditText etinforme;
    Context context;
    JsonObjectRequest jsonObjectRequest;
    Button btnInsertarInforme;
    int idControles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        context = this;
        etinforme = findViewById(R.id.etInforme);
        btnInsertarInforme = findViewById(R.id.btnInsertarInforme);
        extraerId();
        btnInsertarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarInforme();
            }
        });
    }
    private void extraerId (){

        String url = "http://192.168.0.14/seguridad/extraerMayorId.php";
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    public void cargarInforme(){
        String url = "http://192.168.0.14/seguridad/insertarInforme.php?idControles="+idControles+"&informe="+etinforme.getText();
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Casi pero si", Toast.LENGTH_SHORT).show();

            }
        }, this);
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Control control = null;
        JSONArray json = response.optJSONArray("datos");
        try {
            control = new Control();
            JSONObject jsonObject = null;
            jsonObject = json.getJSONObject(0);
            control.setIdControles(jsonObject.optInt("idControles"));
            control.setIdGuardia(jsonObject.optInt("idGuardia"));
            control.setIdLugares(jsonObject.getInt("idLugares"));
            control.setCoordenadas(jsonObject.getString("coordenadas"));
            control.setEstado(jsonObject.getInt("Estado"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        idControles = control.getIdControles();
    }
}
