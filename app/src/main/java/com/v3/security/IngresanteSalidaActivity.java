package com.v3.security;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import uk.co.senab.photoview.PhotoViewAttacher;

public class IngresanteSalidaActivity extends AppCompatActivity {
    TextView nombreGuardiaIngresante, nombreIngresante, dniIngresante, fechaIngresante, motivoIngresante;
    EditText prueba;
    ImageView ingresoImagenIngresante;
    ImageButton ibrotarIngresante;
    PhotoViewAttacher photoViewAttacher;
    Button btnsalir;
    int idingreso;

    Context context;
    ProgressDialog progressDialog;
    //  RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresante_salida);
        context = this;
        nombreGuardiaIngresante = findViewById(R.id.nombreGuardiaIngresante);
        nombreIngresante = findViewById(R.id.nombreIngreste);
        dniIngresante = findViewById(R.id.dniIngresante);
        fechaIngresante = findViewById(R.id.fechaIngresante);
        motivoIngresante = findViewById(R.id.motivoIngresante);
        ingresoImagenIngresante = findViewById(R.id.ingresanteImagen);
        ibrotarIngresante = findViewById(R.id.ibRotarImagenIngresante);

        btnsalir = findViewById(R.id.btnSalida);
        Bundle extras = getIntent().getBundleExtra("ingresante");
        byte[] b = extras.getByteArray("imagenIngresos");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        ingresoImagenIngresante.setImageBitmap(bmp);
        photoViewAttacher = new PhotoViewAttacher(ingresoImagenIngresante);
        dniIngresante.setText("Dni: " + (extras.getString("dni")));
        nombreIngresante.setText("Ingresante: " + (extras.getString("nombreIngreso")) + " " + (extras.getString("apellidoIngreso")));
        String fechastring2 = extras.getString("fechaHoraIngreso");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String c = fmtOut.format(a);
            fechaIngresante.setText("Fecha y Hora: " + c);

            motivoIngresante.setText("Motivo: " + (extras.getString("motivo")));
            motivoIngresante.setMovementMethod(new ScrollingMovementMethod());
            nombreGuardiaIngresante.setText("Guardia: " + (extras.getString("nombre")) + " " + (extras.getString("apellido")));
            idingreso = extras.getInt("idIngreso");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ibrotarIngresante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresoImagenIngresante.setRotation(ingresoImagenIngresante.getRotation() + 90);
            }
        });
        btnsalir.setOnClickListener(new View.OnClickListener() {
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



    /* @Override
     public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;


     }*/
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
        String url = ip + "/security/registrarSalida.php?idIngresos="+idingreso;
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
               //Toast.makeText(getApplicationContext(), "Se registró hola", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
             //  Toast.makeText(getApplicationContext(), "Se registró correctamente", Toast.LENGTH_SHORT).show();

            }
        });
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}

