package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Adapter.AdapterHolderIngresos;
import com.v3.security.Clases.Guardia;
import com.v3.security.Clases.Ingresos;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RegistrarEgresosActivity extends AppCompatActivity  implements AdapterHolderIngresos.CallbackInterface {
    Context context;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<Ingresos> lista;
    ProgressDialog progressDialog;
    RecyclerView contenedoringresos;
    int idguardia;
    private static final int MY_REQUEST=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_egresos);
        lista = new ArrayList<>();
        context = this;
        contenedoringresos = (RecyclerView) findViewById(R.id.contenedorPrueba);
        contenedoringresos.setLayoutManager(new LinearLayoutManager(context));
        contenedoringresos.setHasFixedSize(true);
        idguardia = Preferencias.getInteger(context, Preferencias.getKeyGuardia());
        cargarWebservice();
    }

    private void cargarWebservice() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/extraerIngresantes.php?idGuardia=" + idguardia;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Ingresos ingresos = null;

                Guardia guardia = null;
                JSONArray json = response.optJSONArray("ingresos");

                    if (json.length() == 0) {
                    progressDialog.dismiss();
                    //  erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            ingresos = new Ingresos();
                            guardia = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            ingresos.setIdIngresos(jsonObject.optInt("idIngresos"));
                            guardia.setIdPersona(jsonObject.optInt("idGuardia"));
                            ingresos.setNombreIngreso(jsonObject.optString("nombreIngresante"));
                            ingresos.setApellidoIngreso(jsonObject.optString("apellidoIngresante"));
                            ingresos.setDni(jsonObject.optString("dni"));
                            ingresos.setMotivo(jsonObject.optString("motivo"));
                            ingresos.setDato(jsonObject.optString("imagenRegistro"));
                            ingresos.setFechaHoraIngreso(jsonObject.optString("fechaHora"));
                            ingresos.setFechaHoraSalida(jsonObject.optString("fechaHoraSalida"));
                            guardia.setNombre(jsonObject.optString("nombre"));
                            guardia.setApellido(jsonObject.optString("apellido"));
                            ingresos.setGuardia(guardia);

                            lista.add(ingresos);
                        }
                        String tester= lista.get(0).getApellidoIngreso();
                        if (tester.equals("NOBORRAR")){
                            int Eliminador = ((lista.size()) - 1);
                            lista.remove(Eliminador);
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();
                        if (lista.isEmpty()){
                            AlertaSinIngresos();
                        }else {
                            AdapterHolderIngresos adapterIngresos = new AdapterHolderIngresos(lista, context);

                            contenedoringresos.setAdapter(adapterIngresos);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                AlertaError();
            }
        });
        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onHandleSelection(int pos, Ingresos ingresoss) {


        Ingresos ingresos = ingresoss;

            Bundle bundle = new Bundle();
            Bitmap bitmap = ingresos.getImagenIngresos();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();
            Intent intent = new Intent(context, IngresanteSalidaActivity.class);
            bundle.putByteArray("imagenIngresos", b);
            bundle.putInt("idIngreso", ingresos.getIdIngresos());
            bundle.putString("nombreIngreso", ingresos.getNombreIngreso());
            bundle.putString("apellidoIngreso", ingresos.getApellidoIngreso());
            bundle.putString("dni", ingresos.getDni());
            bundle.putString("motivo", ingresos.getMotivo());
            bundle.putString("fechaHoraIngreso", ingresos.getFechaHoraIngreso());
            bundle.putString("nombre", ingresos.getGuardia().getNombre());
            bundle.putString("apellido", ingresos.getGuardia().getApellido());
            intent.putExtra("ingresante", bundle);
            startActivityForResult(intent,MY_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST && resultCode == RESULT_OK) {
            if (data.hasExtra("ok")) {
                lista.clear();
                cargarWebservice();
            }
        }
    }
    private void AlertaError (){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setCancelable(false);
        builder.setTitle("Error");
        builder.setMessage("Comprueba tu conexión");
        builder.setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarWebservice();
            }
        });
        builder.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    private void AlertaSinIngresos (){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setCancelable(false);
        builder.setTitle("Sin Ingresos");
        builder.setMessage("Sin Ingresos en las instalaciones");
        builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               finish();
            }
        });

        builder.show();
    }
}