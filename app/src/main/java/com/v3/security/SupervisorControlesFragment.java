package com.v3.security;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Adapter.AdapterLugares;
import com.v3.security.Adapter.AdapterSupervisorControles;
import com.v3.security.Clases.Control;
import com.v3.security.Clases.Control;
import com.v3.security.Clases.Guardia;
import com.v3.security.Clases.Lugar;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class SupervisorControlesFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    ArrayList<Control> lista;
    public static final int REQUEST_CODE = 900;
    RecyclerView contenedorsupervisorcontroles;
    // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int idguardia;
    Context context;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String desde = null, hasta = null, unicafecha = null;
    ImageButton imageButton;
    TextView textView, textView2;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private OnFragmentInteractionListener mListener;

    public SupervisorControlesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SupervisorControlesFragment newInstance(String param1, String param2) {
        SupervisorControlesFragment fragment = new SupervisorControlesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_supervisor_controles, container, false);
        setHasOptionsMenu(true);
        context = this.getContext();
        lista = new ArrayList<>();

        contenedorsupervisorcontroles = (RecyclerView) vista.findViewById(R.id.contenedorSupervisorControles);
        contenedorsupervisorcontroles.setLayoutManager(new LinearLayoutManager(context));
        contenedorsupervisorcontroles.setHasFixedSize(true);//indico que el recycler no va a reprensetar variables en lo que es el tamaño
        //   request = Volley.newRequestQueue(context);
        Toolbar toolbar = vista.findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        cargarWebservice();
        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                desde = data.getStringExtra("desde");
                hasta = data.getStringExtra("hasta");
                unicafecha = data.getStringExtra("unica");
                if (desde == null || hasta == null) {
                    lista.clear();
                    cargarPorFecha(unicafecha);
                } else {
                    lista.clear();
                    cargarRangoFecha(desde, hasta);
                }

            }

        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbarsupervisor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buscar:
                Intent intent = new Intent(getActivity(), BuscarControlActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void cargarWebservice() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/extraerControles.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        AlertaError();


    }

    @Override
    public void onResponse(JSONObject response) {
        Control fuenteDatos = null;
        Lugar fuentedeDatos2 = null;
        Guardia fuenteDatos3 = null;
        JSONArray json = response.optJSONArray("controles");
        if (json.length() == 0) {
            progressDialog.dismiss();
            erroSinRegistros();
        } else {
            try {
                for (int i = 0; i < json.length(); i++) {
                    fuenteDatos = new Control();
                    fuentedeDatos2 = new Lugar();
                    fuenteDatos3 = new Guardia();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);

                    fuenteDatos.setIdControles(jsonObject.optInt("idControles"));
                    fuenteDatos3.setNombre(jsonObject.optString("nombre"));
                    fuenteDatos3.setApellido(jsonObject.optString("apellido"));
                    fuentedeDatos2.setNombre_lugares(jsonObject.optString("nombreLugar"));
                    fuenteDatos.setLugar(fuentedeDatos2);
                    fuenteDatos.setGuardia(fuenteDatos3);
                    fuenteDatos.setLatitud(jsonObject.optString("latitud"));
                    fuenteDatos.setLongitud(jsonObject.optString("longitud"));
                    fuenteDatos.setFechaHora(jsonObject.optString("fechaHora"));
                    lista.add(fuenteDatos);
                }
                String tester= lista.get(0).getLugar().getNombre_lugares();
                if (tester.equals("NOBORRAR")){
                    int Eliminador = ((lista.size()) - 1);
                    lista.remove(Eliminador);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
                if (lista.isEmpty()){
                    erroSinRegistros();
                }else {
                    AdapterSupervisorControles adapterSupervisorControles = new AdapterSupervisorControles(lista);

                    contenedorsupervisorcontroles.setAdapter(adapterSupervisorControles);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void cargarRangoFecha(String a, String b) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/buscarControlRango.php?desde=" + a + "%2000:00:00&hasta=" + b + "%2023:59:59";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Control fuenteDatos = null;
                Lugar fuentedeDatos2 = null;
                Guardia fuenteDatos3 = null;
                JSONArray json = response.optJSONArray("controles");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            fuenteDatos = new Control();
                            fuentedeDatos2 = new Lugar();
                            fuenteDatos3 = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            fuenteDatos.setIdControles(jsonObject.optInt("idControles"));
                            fuenteDatos3.setNombre(jsonObject.optString("nombre"));
                            fuenteDatos3.setApellido(jsonObject.optString("apellido"));
                            fuentedeDatos2.setNombre_lugares(jsonObject.optString("nombreLugar"));
                            fuenteDatos.setLugar(fuentedeDatos2);
                            fuenteDatos.setGuardia(fuenteDatos3);
                            fuenteDatos.setLatitud(jsonObject.optString("latitud"));
                            fuenteDatos.setLongitud(jsonObject.optString("longitud"));
                            fuenteDatos.setFechaHora(jsonObject.optString("fechaHora"));
                            lista.add(fuenteDatos);
                        }
                        progressDialog.dismiss();
                        AdapterSupervisorControles adapterSupervisorControles = new AdapterSupervisorControles(lista);

                        contenedorsupervisorcontroles.setAdapter(adapterSupervisorControles);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                errorRango();
                cargarWebservice();
            }
        });
        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void cargarPorFecha(String a) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/buscarControlFecha.php?fecha=" + a + "%";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Control fuenteDatos = null;
                Lugar fuentedeDatos2 = null;
                Guardia fuenteDatos3 = null;
                JSONArray json = response.optJSONArray("controles");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            fuenteDatos = new Control();
                            fuentedeDatos2 = new Lugar();
                            fuenteDatos3 = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            fuenteDatos.setIdControles(jsonObject.optInt("idControles"));
                            fuenteDatos3.setNombre(jsonObject.optString("nombre"));
                            fuenteDatos3.setApellido(jsonObject.optString("apellido"));
                            fuentedeDatos2.setNombre_lugares(jsonObject.optString("nombreLugar"));
                            fuenteDatos.setLugar(fuentedeDatos2);
                            fuenteDatos.setGuardia(fuenteDatos3);
                            fuenteDatos.setLatitud(jsonObject.optString("latitud"));
                            fuenteDatos.setLongitud(jsonObject.optString("longitud"));
                            fuenteDatos.setFechaHora(jsonObject.optString("fechaHora"));
                            lista.add(fuenteDatos);
                        }

                        progressDialog.dismiss();
                        AdapterSupervisorControles adapterSupervisorControles = new AdapterSupervisorControles(lista);

                        contenedorsupervisorcontroles.setAdapter(adapterSupervisorControles);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                errorFecha();
                cargarWebservice();
            }
        });
        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void errorFecha() {
        String fechastring2 = unicafecha;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            String c = fmtOut.format(a);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Alerta");
            builder.setCancelable(false);
            builder.setMessage("No se realizadron controles en esta fecha: " + c);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void errorRango() {
        String fechastring = desde;
        String fechastring2 = hasta;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date a = sdf.parse(fechastring);
            Date b = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            String c = fmtOut.format(a);
            String d = fmtOut.format(b);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Alerta");
            builder.setCancelable(false);
            builder.setMessage("No se realizadron controles entre:" + c + " y " + d);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void AlertaError (){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setCancelable(false);
        builder.setTitle("Error");
        builder.setMessage("Comprueba tu conexión.");
        builder.setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarWebservice();
            }
        });
        builder.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        builder.show();
    }

    private void erroSinRegistros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Alerta");
        builder.setCancelable(false);
        builder.setMessage("No se realizaron Controles.");
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
}
