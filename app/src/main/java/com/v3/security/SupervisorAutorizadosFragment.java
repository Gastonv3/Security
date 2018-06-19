package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Adapter.AdapterSupervisorAutorizado;
import com.v3.security.Adapter.AdapterSupervisorIngresos;
import com.v3.security.Clases.Guardia;
import com.v3.security.Clases.Ingresos;
import com.v3.security.Clases.IngresosAutorizados;
import com.v3.security.Clases.PersonalAutorizado;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SupervisorAutorizadosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SupervisorAutorizadosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorAutorizadosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<IngresosAutorizados> lista;
    public static final int REQUEST_CODE = 950;
    RecyclerView contenedorSupervisorAutorizados;
    // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int idguardia;
    Context context;
    ProgressDialog progressDialog;
    String desde = null, hasta = null, unicafecha = null, dni = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SupervisorAutorizadosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SupervisorAutorizadosFragment newInstance(String param1, String param2) {
        SupervisorAutorizadosFragment fragment = new SupervisorAutorizadosFragment();
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
        View view = inflater.inflate(R.layout.fragment_supervisor_autorizados, container, false);
        setHasOptionsMenu(true);
        context = this.getContext();
        lista = new ArrayList<>();

        contenedorSupervisorAutorizados = (RecyclerView) view.findViewById(R.id.contenedorSupervisorAutorizados);
        contenedorSupervisorAutorizados.setLayoutManager(new LinearLayoutManager(context));
        contenedorSupervisorAutorizados.setHasFixedSize(true);//indico que el recycler no va a reprensetar variables en lo que es el tamaño
        //   request = Volley.newRequestQueue(context);
        Toolbar toolbar = view.findViewById(R.id.toolbarAutorizados);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        cargarWebservice();

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                desde = data.getStringExtra("desde");
                hasta = data.getStringExtra("hasta");
                unicafecha = data.getStringExtra("unica");
                dni = data.getStringExtra("dni");
                if (desde == null && hasta == null && dni == null) {
                    lista.clear();
                    cargarPorFecha(unicafecha);
                } else if (dni == null && unicafecha == null) {
                    lista.clear();
                    cargarRangoFecha(desde, hasta);
                } else {
                    lista.clear();
                    cargarPorDni(dni);
                }


            }

        }


    }

    private void cargarPorDni(String a) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/buscarAutorizadosDni.php?dni=" + a;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PersonalAutorizado personalAutorizado = null;
                IngresosAutorizados ingresosAutorizados = null;
                Guardia guardia = null;
                JSONArray json = response.optJSONArray("ingresosautorizados");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            personalAutorizado = new PersonalAutorizado();
                            ingresosAutorizados = new IngresosAutorizados();
                            guardia = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            ingresosAutorizados.setIdIngresosAutorizados(jsonObject.optInt("idIngresosAutorizados"));
                            personalAutorizado.setNombrePersonalAutorizado(jsonObject.optString("nombrePersonalAutorizado"));
                            personalAutorizado.setApellidoPersonalAutorizado(jsonObject.optString("apellidoPersonalAutorizado"));
                            personalAutorizado.setDni(jsonObject.optString("dni"));
                            personalAutorizado.setCargo(jsonObject.optString("cargo"));
                            personalAutorizado.setCodigo(jsonObject.optString("codigo"));
                            guardia.setNombre(jsonObject.optString("nombre"));
                            guardia.setApellido(jsonObject.optString("apellido"));
                            ingresosAutorizados.setFechaHora(jsonObject.optString("fechaHora"));
                            ingresosAutorizados.setFechaHoraSalida(jsonObject.optString("fechaHoraSalida"));
                            ingresosAutorizados.setPersonalAutorizado(personalAutorizado);
                            ingresosAutorizados.setGuardia(guardia);

                            lista.add(ingresosAutorizados);
                        }

                        progressDialog.dismiss();

                        AdapterSupervisorAutorizado adapterSupervisorAutorizado = new AdapterSupervisorAutorizado(lista);

                        contenedorSupervisorAutorizados.setAdapter(adapterSupervisorAutorizado);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                errorDni();
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
        String url = ip + "/security/BuscarAutorizadosFecha.php?fecha=" + a + "%";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PersonalAutorizado personalAutorizado = null;
                IngresosAutorizados ingresosAutorizados = null;
                Guardia guardia = null;
                JSONArray json = response.optJSONArray("ingresosautorizados");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            personalAutorizado = new PersonalAutorizado();
                            ingresosAutorizados = new IngresosAutorizados();
                            guardia = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            ingresosAutorizados.setIdIngresosAutorizados(jsonObject.optInt("idIngresosAutorizados"));
                            personalAutorizado.setNombrePersonalAutorizado(jsonObject.optString("nombrePersonalAutorizado"));
                            personalAutorizado.setApellidoPersonalAutorizado(jsonObject.optString("apellidoPersonalAutorizado"));
                            personalAutorizado.setDni(jsonObject.optString("dni"));
                            personalAutorizado.setCargo(jsonObject.optString("cargo"));
                            personalAutorizado.setCodigo(jsonObject.optString("codigo"));
                            guardia.setNombre(jsonObject.optString("nombre"));
                            guardia.setApellido(jsonObject.optString("apellido"));
                            ingresosAutorizados.setFechaHora(jsonObject.optString("fechaHora"));
                            ingresosAutorizados.setFechaHoraSalida(jsonObject.optString("fechaHoraSalida"));

                            ingresosAutorizados.setPersonalAutorizado(personalAutorizado);
                            ingresosAutorizados.setGuardia(guardia);

                            lista.add(ingresosAutorizados);
                        }

                        progressDialog.dismiss();

                        AdapterSupervisorAutorizado adapterSupervisorAutorizado = new AdapterSupervisorAutorizado(lista);

                        contenedorSupervisorAutorizados.setAdapter(adapterSupervisorAutorizado);

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

    private void cargarRangoFecha(String a, String b) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/buscarAutorizadosRango.php?desde=" + a + "%2000:00:00&hasta=" + b + "%2023:59:59";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PersonalAutorizado personalAutorizado = null;
                IngresosAutorizados ingresosAutorizados = null;
                Guardia guardia = null;
                JSONArray json = response.optJSONArray("ingresosautorizados");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            personalAutorizado = new PersonalAutorizado();
                            ingresosAutorizados = new IngresosAutorizados();
                            guardia = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            ingresosAutorizados.setIdIngresosAutorizados(jsonObject.optInt("idIngresosAutorizados"));
                            personalAutorizado.setNombrePersonalAutorizado(jsonObject.optString("nombrePersonalAutorizado"));
                            personalAutorizado.setApellidoPersonalAutorizado(jsonObject.optString("apellidoPersonalAutorizado"));
                            personalAutorizado.setDni(jsonObject.optString("dni"));
                            personalAutorizado.setCargo(jsonObject.optString("cargo"));
                            personalAutorizado.setCodigo(jsonObject.optString("codigo"));
                            guardia.setNombre(jsonObject.optString("nombre"));
                            guardia.setApellido(jsonObject.optString("apellido"));
                            ingresosAutorizados.setFechaHora(jsonObject.optString("fechaHora"));
                            ingresosAutorizados.setFechaHoraSalida(jsonObject.optString("fechaHoraSalida"));
                            ingresosAutorizados.setPersonalAutorizado(personalAutorizado);
                            ingresosAutorizados.setGuardia(guardia);

                            lista.add(ingresosAutorizados);
                        }

                        progressDialog.dismiss();

                        AdapterSupervisorAutorizado adapterSupervisorAutorizado = new AdapterSupervisorAutorizado(lista);

                        contenedorSupervisorAutorizados.setAdapter(adapterSupervisorAutorizado);

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

    private void cargarWebservice() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/extraerIngresosAutorizados.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PersonalAutorizado personalAutorizado = null;
                IngresosAutorizados ingresosAutorizados = null;
                Guardia guardia = null;
                JSONArray json = response.optJSONArray("ingresosautorizados");
                if (json.length() == 0) {
                    progressDialog.dismiss();
                    erroSinRegistros();
                } else {
                    try {
                        for (int i = 0; i < json.length(); i++) {
                            personalAutorizado = new PersonalAutorizado();
                            ingresosAutorizados = new IngresosAutorizados();
                            guardia = new Guardia();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            ingresosAutorizados.setIdIngresosAutorizados(jsonObject.optInt("idIngresosAutorizados"));
                            personalAutorizado.setNombrePersonalAutorizado(jsonObject.optString("nombrePersonalAutorizado"));
                            personalAutorizado.setApellidoPersonalAutorizado(jsonObject.optString("apellidoPersonalAutorizado"));
                            personalAutorizado.setDni(jsonObject.optString("dni"));
                            personalAutorizado.setCargo(jsonObject.optString("cargo"));
                            personalAutorizado.setCodigo(jsonObject.optString("codigo"));
                            guardia.setNombre(jsonObject.optString("nombre"));
                            guardia.setApellido(jsonObject.optString("apellido"));
                            ingresosAutorizados.setFechaHora(jsonObject.optString("fechaHora"));
                            ingresosAutorizados.setFechaHoraSalida(jsonObject.optString("fechaHoraSalida"));
                            ingresosAutorizados.setPersonalAutorizado(personalAutorizado);
                            ingresosAutorizados.setGuardia(guardia);

                            lista.add(ingresosAutorizados);
                        }
                        int Eliminador = ((lista.size()) - 1);
                        lista.remove(Eliminador);
                        progressDialog.dismiss();

                        AdapterSupervisorAutorizado adapterSupervisorAutorizado = new AdapterSupervisorAutorizado(lista);

                        contenedorSupervisorAutorizados.setAdapter(adapterSupervisorAutorizado);

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            builder.setMessage("No se registraron Ingresos Autorizados en esta fecha: " + c);
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
            builder.setMessage("No se registraron Ingresos Autorizados entre:" + c + " y " + d);
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

    private void errorDni() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Alerta");
        builder.setCancelable(false);
        builder.setMessage("No se existen resultados con DNI: " + dni);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbarsupervisor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buscar:
                Intent intent = new Intent(getActivity(), BuscarIngresosActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void erroSinRegistros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Alerta");
        builder.setCancelable(false);
        builder.setMessage("No se registraron Ingresos Autorizados.");
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


