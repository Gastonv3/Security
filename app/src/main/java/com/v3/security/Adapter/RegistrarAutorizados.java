package com.v3.security.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Clases.PersonalAutorizado;
import com.v3.security.R;
import com.v3.security.ScannerActivity;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarAutorizados extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    Context context;
    public static final int REQUEST_CODE = 800;
    //rivate ZXingScannerView mScannerView;
    TextView tvNombre, tvApellido;
    ImageButton ibQrAutorizar, ibRegistrarPersonalAtuorizado;
    ImageView siautorizado, noatuorizado;
    String codigo, nombre, apellido, completo;
    int idguardia;
    int idPersonalAutorizado;
    String s = null;
    JsonObjectRequest jsonObjectRequest;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_autorizados);
        context= this;
        idguardia = Preferencias.getInteger(context, Preferencias.getKeyGuardia());
        siautorizado = findViewById(R.id.siAutorizado);
        siautorizado.setVisibility(View.INVISIBLE);
        noatuorizado = findViewById(R.id.noAutorizado);
        ibQrAutorizar = findViewById(R.id.btn2);
        tvNombre = findViewById(R.id.tvNombre);
        tvNombre.setText(s);
        codigo = Preferencias.getString(context, Preferencias.getKeyGuardiaNombre());
        ibRegistrarPersonalAtuorizado = findViewById(R.id.ibEnviarRegistroPersonalAutorizado);
        ibQrAutorizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScannerActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ibRegistrarPersonalAtuorizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visible = 4;
                if (tvNombre.getText().toString().isEmpty()){
                    errorNoScan();
                }else{
                    registrarPersonalAutorizado();
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String a = data.getStringExtra("result");
                // tv1.setText(a);
               /* tv1.post(new Runnable() {
                    @Override
                    public void run() {
                        tv1.setText(a);
                    }
                });*/
                extraerPorCodigo(a);
            }
        }
    }

    private void extraerPorCodigo(String a) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/extraerPersonalAutorizado2.php?codigo=" + a;
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(jsonObjectRequest);

    }



    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        errorCodigo();
    }

    @Override
    public void onResponse(JSONObject response) {
        PersonalAutorizado personalAutorizado = null;
        JSONArray json = response.optJSONArray("personalautorizado");
        try {
            personalAutorizado = new PersonalAutorizado();
            JSONObject jsonObject = null;
            jsonObject = json.getJSONObject(0);
            personalAutorizado.setIdPersonalAutorizado(jsonObject.optInt("idPersonalAutorizado"));
            personalAutorizado.setNombrePersonalAutorizado(jsonObject.optString("nombrePersonalAutorizado"));
            personalAutorizado.setApellidoPersonalAutorizado(jsonObject.getString("apellidoPersonalAutorizado"));
            personalAutorizado.setDni(jsonObject.getString("dni"));
            personalAutorizado.setCargo(jsonObject.getString("cargo"));
            personalAutorizado.setCodigo(jsonObject.getString("codigo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        idPersonalAutorizado = personalAutorizado.getIdPersonalAutorizado();
        nombre = personalAutorizado.getNombrePersonalAutorizado();
        apellido= personalAutorizado.getApellidoPersonalAutorizado();
        completo= nombre+" "+apellido;
        tvNombre.setText(completo);
        noatuorizado.setVisibility(View.INVISIBLE);
        siautorizado.setVisibility(View.VISIBLE);
        progressDialog.hide();
    }
    private void registrarPersonalAutorizado(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/registrarPersonalAutorizado.php?idPersonalAutorizado="+idPersonalAutorizado+"&idGuardia="+idguardia;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                noatuorizado.setVisibility(View.VISIBLE);
                siautorizado.setVisibility(View.INVISIBLE);
                Toast.makeText(context,"Se registró correctamente",Toast.LENGTH_SHORT).show();
                tvNombre.setText(s);
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noatuorizado.setVisibility(View.VISIBLE);
                siautorizado.setVisibility(View.INVISIBLE);
                progressDialog.hide();
                AlertaError();
            }
        });
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(jsonObjectRequest);
    }
    private boolean imagensi(){
        noatuorizado.setVisibility(View.VISIBLE);
        return true;
    }
    private void AlertaError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Error al registrar, comprueba tu conexión");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
    private void errorCodigo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Código incorrecto y/o error de conexión");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
    private void errorNoScan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Debe Scannear un código");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
}
