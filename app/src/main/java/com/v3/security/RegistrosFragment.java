package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Clases.Ingresos;

import java.util.ArrayList;



public class RegistrosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<Ingresos> lista;
    ProgressDialog progressDialog;
    RecyclerView contenedoringresos;
    Button btnRegistrarIngresos, btnRegistrarSalida;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final int REQUEST_CODE = 1800;

    private OnFragmentInteractionListener mListener;

    public RegistrosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegistrosFragment newInstance(String param1, String param2) {
        RegistrosFragment fragment = new RegistrosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_registros, container, false);
        lista = new ArrayList<>();
        context = getContext();
        btnRegistrarIngresos = vista.findViewById(R.id.idRegistrarIngresante);
        btnRegistrarSalida = vista.findViewById(R.id.idRegistrarEgresos);
        Toolbar toolbar = vista.findViewById(R.id.toolbar3);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        btnRegistrarIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,RegistrarIngresosActivity.class);
                startActivity(intent);
            }
        });
        btnRegistrarSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,RegistrarEgresosActivity.class);
                startActivity(intent);
            }
        });
        return vista;

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
}
