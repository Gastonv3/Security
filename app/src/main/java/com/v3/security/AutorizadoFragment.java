package com.v3.security;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.BarcodeFormat;
import com.v3.security.Clases.PersonalAutorizado;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AutorizadoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AutorizadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutorizadoFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int REQUEST_CODE = 800;
    //rivate ZXingScannerView mScannerView;
    TextView tvNombre, tvApellido;
    Context context;
    ImageButton ibQrAutorizar, ibRegistrarPersonalAtuorizado;
    ImageView siautorizado, noatuorizado;
    String codigo, nombre, apellido, completo;
    int idguardia;
    int idPersonalAutorizado;
    String s = null;
    JsonObjectRequest jsonObjectRequest;


    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AutorizadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AutorizadoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AutorizadoFragment newInstance(String param1, String param2) {
        AutorizadoFragment fragment = new AutorizadoFragment();
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

        context = this.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_autorizado, container, false);
        idguardia = Preferencias.getInteger(context, Preferencias.getKeyGuardia());
        siautorizado = view.findViewById(R.id.siAutorizado);
        siautorizado.setVisibility(View.INVISIBLE);
        noatuorizado = view.findViewById(R.id.noAutorizado);
        ibQrAutorizar = view.findViewById(R.id.btn2);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvNombre.setText(s);
        codigo = Preferencias.getString(context, Preferencias.getKeyGuardiaNombre());
        ibRegistrarPersonalAtuorizado = view.findViewById(R.id.ibEnviarRegistroPersonalAutorizado);
        ibQrAutorizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
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
        return view;

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
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }
    private boolean imagensi(){
        noatuorizado.setVisibility(View.VISIBLE);
        return true;
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
