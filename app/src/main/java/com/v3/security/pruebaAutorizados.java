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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Adapter.AdapterHolderAutorizados;
import com.v3.security.Adapter.AdapterHolderIngresos;
import com.v3.security.Adapter.AdapterSupervisorAutorizado;
import com.v3.security.Clases.Guardia2;
import com.v3.security.Clases.Ingresos;
import com.v3.security.Clases.IngresosAutorizados;
import com.v3.security.Clases.PersonalAutorizado;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class pruebaAutorizados extends AppCompatActivity implements  AdapterHolderAutorizados.CallbackInterface{
    ArrayList<IngresosAutorizados> lista;
    ProgressDialog progressDialog;
    RecyclerView contenedoringresos;
    Context context;
    JsonObjectRequest jsonObjectRequest;
    private static final int MY_REQUEST=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_autorizados);
        lista = new ArrayList<>();
        context = this;
        contenedoringresos = (RecyclerView) findViewById(R.id.contenedorPruebaAutorizados);
        contenedoringresos.setLayoutManager(new LinearLayoutManager(context));
        contenedoringresos.setHasFixedSize(true);
        cargarWebservice();
    }
    private void cargarWebservice() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/extraerAutorizadosEgreso.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PersonalAutorizado personalAutorizado = null;
                IngresosAutorizados ingresosAutorizados = null;
                Guardia2 guardia = null;
                JSONArray json = response.optJSONArray("ingresosautorizados");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            personalAutorizado = new PersonalAutorizado();
                            ingresosAutorizados = new IngresosAutorizados();
                            guardia = new Guardia2();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            ingresosAutorizados.setIdIngresosAutorizados(jsonObject.optInt("idIngresosAutorizados"));
                            personalAutorizado.setNombrePersonalAutorizado(jsonObject.optString("nombrePersonalAutorizado"));
                            personalAutorizado.setApellidoPersonalAutorizado(jsonObject.optString("apellidoPersonalAutorizado"));
                            personalAutorizado.setDni(jsonObject.optString("dni"));
                            personalAutorizado.setCargo(jsonObject.optString("cargo"));
                            personalAutorizado.setCodigo(jsonObject.optString("codigo"));
                            ingresosAutorizados.setFechaHora(jsonObject.optString("fechaHora"));
                            ingresosAutorizados.setFechaHoraSalida(jsonObject.optString("fechaHoraSalida"));
                            guardia.setNombre(jsonObject.optString("nombre"));
                            guardia.setApellido(jsonObject.optString("apellido"));

                            ingresosAutorizados.setPersonalAutorizado(personalAutorizado);
                            ingresosAutorizados.setGuardia(guardia);

                            lista.add(ingresosAutorizados);
                        }
                        int Eliminador = ((lista.size())-1);
                        lista.remove(Eliminador);
                        progressDialog.dismiss();

                        AdapterHolderAutorizados adapterSupervisorAutorizado = new AdapterHolderAutorizados(lista,context);

                        contenedoringresos.setAdapter(adapterSupervisorAutorizado);

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
    private void AlertaError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Comprueba tu conexión");
        builder.setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarWebservice();
            }
        });
        /*builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });*/
        builder.show();
    }
    private void erroSinRegistros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("No se realizaron Ingresos Autorizados");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        /*builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });*/
        builder.show();
    }


    @Override
    public void onHandleSelection(int pos, IngresosAutorizados ingresosAutorizados) {
        Bundle bundle = new Bundle();
            /*Bitmap bitmap = ingresos.getImagenIngresos();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();*/
        Intent intent = new Intent(context, AutorizadosSalidaActivity.class);
        //bundle.putByteArray("imagenIngresos", b);
        bundle.putInt("idIngreso", ingresosAutorizados.getIdIngresosAutorizados());

        bundle.putString("nombrePersonalAutorizado", ingresosAutorizados.getPersonalAutorizado().getNombrePersonalAutorizado());
        bundle.putString("apellidoPersonalAutorizado", ingresosAutorizados.getPersonalAutorizado().getApellidoPersonalAutorizado());
        bundle.putString("dni", ingresosAutorizados.getPersonalAutorizado().getDni());
        bundle.putString("cargo", ingresosAutorizados.getPersonalAutorizado().getCargo());
        bundle.putString("fechaHora", ingresosAutorizados.getFechaHora());
        bundle.putString("nombre", ingresosAutorizados.getGuardia().getNombre());
        bundle.putString("apellido", ingresosAutorizados.getGuardia().getApellido());
        intent.putExtra("autorizados", bundle);
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
}
