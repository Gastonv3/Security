package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.v3.security.Clases.Guardia;
import com.v3.security.Util.Preferencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{
    EditText etlogin;
    EditText etpass;
    Button btnentrar;
    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    Context context;
    CheckBox cbkrecuerdame;
    int idguardia;
    boolean estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        referencias();
        request = Volley.newRequestQueue(getApplicationContext());
        estado = Preferencias.getBoolean(context,Preferencias.getKeyRecuerdame(),false);
        if (estado == true){
            cbkrecuerdame.setChecked(estado);
            recordar();
        }
        cbkrecuerdame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!cbkrecuerdame.isChecked()){
                    Preferencias.setBoolean(context, Preferencias.getKeyRecuerdame(), false);
                }
            }
        });
        btnentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebServis();
            }
        });
    }

    private void referencias() {
        cbkrecuerdame = findViewById(R.id.cbRecuerdame);
        etlogin = findViewById(R.id.etUsuario);
        etpass = findViewById(R.id.etpass);
        btnentrar = findViewById(R.id.btnEntrar);
    }

    private void cargarWebServis() {
        String url = "http://192.168.0.14/seguridad/login.php?login=" + etlogin.getText() + "&password=" + etpass.getText();
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "Casi pero no", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Casi pero si", Toast.LENGTH_SHORT).show();
        Guardia guardia = null;
        JSONArray json = response.optJSONArray("datos");
        try {
            guardia = new Guardia();
            JSONObject jsonObject = null;
            jsonObject = json.getJSONObject(0);
            guardia.setIdpersona(jsonObject.optInt("idpersona"));
            guardia.setCodigo_guardia(jsonObject.optString("codigo_guardia"));
            guardia.setLogin(jsonObject.getString("login"));
            guardia.setPassword(jsonObject.getString("password"));
            guardia.setEstado(jsonObject.getString("estado"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        idguardia = guardia.getIdpersona();
        Preferencias.setInteger(context, Preferencias.getKeyGuardia(), idguardia);
        Preferencias.setString(context, Preferencias.getKeyUser(), guardia.getLogin());
        Preferencias.setString(context, Preferencias.getKeyPass(), guardia.getPassword());
        Preferencias.setBoolean(context, Preferencias.getKeyRecuerdame(), cbkrecuerdame.isChecked());
        //esto fue para salir de paso
        Preferencias.setString(context,Preferencias.getKeyGuardiaNombre(),guardia.getCodigo_guardia());
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    public void recordar() {
        if (Preferencias.getBoolean(context, Preferencias.getKeyRecuerdame(), true)) {
            etlogin.setText(Preferencias.getString(context, Preferencias.getKeyUser()));
            etpass.setText(Preferencias.getString(context, Preferencias.getKeyPass()));
        }
    }
}
