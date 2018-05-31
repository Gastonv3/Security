package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AutorizadosSalidaActivity extends AppCompatActivity {
    TextView GuardiaAutorizado, personaingresanteAutorizada, dniingresanteAutorizado, cargoingresanteAutorizado, fechaingresanteAutorizado;
    Button btnsalirautorizado;
    private int idingreso;
    Context context;
    ProgressDialog progressDialog;
    //  RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorizados_salida);
        context = this;
        GuardiaAutorizado = findViewById(R.id.GuardiaAutorizado);
        personaingresanteAutorizada = findViewById(R.id.ingresanteAutorizada);
        dniingresanteAutorizado = findViewById(R.id.dniIngresanteAutorizado);
        cargoingresanteAutorizado = findViewById(R.id.cargoIngresanteAutorizado);
        fechaingresanteAutorizado = findViewById(R.id.fechaIngresanteAutorizado);
        btnsalirautorizado = findViewById(R.id.btnSalidaAutorizado);

        Bundle extras = getIntent().getBundleExtra("autorizados");
        /*ibrotar = findViewById(R.id.ibRotarImagen);
        Bundle extras = getIntent().getBundleExtra("suerte");
        byte[] b = extras.getByteArray("imagenInforme");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageninforme.setImageBitmap(bmp);*/

        personaingresanteAutorizada.setText("Ingresante: " + (extras.getString("nombrePersonalAutorizado")) + " " + (extras.getString("apellidoPersonalAutorizado")));
        String fechastring2 = extras.getString("fechaHora");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String c = fmtOut.format(a);
            fechaingresanteAutorizado.setText("Fecha y Hora: " + c);
            cargoingresanteAutorizado.setText("Cargo: " + (extras.getString("cargo")));
            dniingresanteAutorizado.setText("Dni: " + (extras.getString("dni")));
            /*informe.setText(extras.getString("informe"));
            informe.setMovementMethod(new ScrollingMovementMethod());*/
            GuardiaAutorizado.setText("Guardia: " + (extras.getString("nombre")) + " " + (extras.getString("apellido")));
            idingreso = extras.getInt("idIngreso");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        btnsalirautorizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebservice2();
                Intent i = new Intent();
                int ok2 = 1;
                i.putExtra("ok",ok2);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }
    private void AlertaError() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Error al registrar, comprueba tu conexión");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
    public void cargarWebservice2() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        String ip = getString(R.string.ip_bd);
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = ip + "/security/registrarSalidaAutorizada.php?idIngresosAutorizados="+idingreso;
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "Se registró correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "Se registró correctamente", Toast.LENGTH_SHORT).show();

            }
        });
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}

