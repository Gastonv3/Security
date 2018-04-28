package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Adapter.AdapterLugares;
import com.v3.security.Adapter.AdapterSupervisorControles;
import com.v3.security.Clases.Control;
import com.v3.security.Clases.Control2;
import com.v3.security.Clases.Guardia2;
import com.v3.security.Clases.Lugar;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SupervisorControlesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SupervisorControlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorControlesFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Control2> lista;
    private static final String url = "http://192.168.0.14/seguridad/extraer.php";
    RecyclerView contenedorsupervisorcontroles;
    // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    int idguardia;
    Context context;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SupervisorControlesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupervisorControlesFragment.
     */
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

        context = this.getContext();
        lista = new ArrayList<>();

        contenedorsupervisorcontroles = (RecyclerView) vista.findViewById(R.id.contenedorSupervisorControles);
        contenedorsupervisorcontroles.setLayoutManager(new LinearLayoutManager(context));
        contenedorsupervisorcontroles.setHasFixedSize(true);//indico que el recycler no va a reprensetar variables en lo que es el tamaño
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
        String url = ip+"/security/extraerControles.php";
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

        Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Control2 fuenteDatos = null;
        Lugar fuentedeDatos2 = null;
        Guardia2 fuenteDatos3 = null;
        JSONArray json = response.optJSONArray("controles");
        try {
            for (int i = 0; i < json.length(); i++) {
                fuenteDatos = new Control2();
                fuentedeDatos2 = new Lugar();
                fuenteDatos3 = new Guardia2();
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
            progressDialog.hide();
            AdapterSupervisorControles adapterSupervisorControles = new AdapterSupervisorControles(lista);

            contenedorsupervisorcontroles.setAdapter(adapterSupervisorControles);

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
}
