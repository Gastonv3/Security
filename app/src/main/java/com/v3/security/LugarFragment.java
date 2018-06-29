package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.v3.security.Adapter.AdapterLugares;
import com.v3.security.Clases.Lugar;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LugarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LugarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LugarFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Lugar> lista;
    private static final String url = "http://192.168.0.14/seguridad/extraer.php";
    RecyclerView contenedor;
   // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int idguardia;
    Context context;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LugarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LugarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LugarFragment newInstance(String param1, String param2) {
        LugarFragment fragment = new LugarFragment();
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
        View vista = inflater.inflate(R.layout.fragment_lugar, container, false);
        context = this.getContext();
        lista = new ArrayList<>();

        contenedor = (RecyclerView) vista.findViewById(R.id.contenedor);
        contenedor.setLayoutManager(new LinearLayoutManager(context));
        contenedor.setHasFixedSize(true);//indico que el recycler no va a reprensetar variables en lo que es el tamaño
     //   request = Volley.newRequestQueue(context);

        cargarWebservice();
        return vista;
    }

    private void cargarWebservice() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
       progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip+"/security/extraerLugares.php";
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
    progressDialog.hide();
        AlertaError();
    }

    @Override
    public void onResponse(JSONObject response) {
        Lugar fuenteDatos = null;
        JSONArray json = response.optJSONArray("lugares");
        try {
            for (int i = 0; i < json.length(); i++) {
                fuenteDatos = new Lugar();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                fuenteDatos.setIdLugares(jsonObject.optInt("idLugares"));
                fuenteDatos.setNombre_lugares(jsonObject.optString("nombreLugar"));
                fuenteDatos.setUbicacion(jsonObject.optString("ubicacion"));
                fuenteDatos.setEmails(jsonObject.optString("emails"));
                fuenteDatos.setDato(jsonObject.optString("imagenLugar"));
                fuenteDatos.setEstado(jsonObject.optInt("estado"));
                lista.add(fuenteDatos);
            }
            progressDialog.dismiss();
            AdapterLugares adapter = new AdapterLugares(lista);
            contenedor.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                getActivity().finish();
            }
        });
        builder.show();
    }

}
