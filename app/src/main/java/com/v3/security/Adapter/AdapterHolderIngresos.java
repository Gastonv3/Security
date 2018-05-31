package com.v3.security.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v3.security.Clases.Ingresos;
import com.v3.security.IngresanteSalidaActivity;
import com.v3.security.R;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterHolderIngresos extends RecyclerView.Adapter<AdapterHolderIngresos.viewHolderIngresos> {
    private List<Ingresos> ListaObjetos;
    private CallbackInterface mCallback;
    private Context mContext;
    public interface CallbackInterface {

        void onHandleSelection(int pos, Ingresos ingresos);
    }


    public AdapterHolderIngresos(List<Ingresos> listaObjetos, Context context) {
        ListaObjetos = listaObjetos;
        mContext = context;
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }
    }

    @Override
    public viewHolderIngresos onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(mContext).inflate(R.layout.card_ingresos, parent, false);
        return new viewHolderIngresos(vista, ListaObjetos);
    }


    @Override
    public void onBindViewHolder(viewHolderIngresos holder, final int position) {

        holder.ingresanteExterno.setText((ListaObjetos.get(position).getNombreIngreso()) + " " + (ListaObjetos.get(position).getApellidoIngreso()));
        String fechastring2 = ListaObjetos.get(position).getFechaHoraIngreso();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String b = fmtOut.format(a);
            holder.fechaHoraingresosExternos.setText(b);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.btnRegistrarSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(position,ListaObjetos.get(position));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return ListaObjetos.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }



    static class viewHolderIngresos extends RecyclerView.ViewHolder {
        Button btnRegistrarSalida;

        TextView ingresanteExterno, fechaHoraingresosExternos;
        //  List<Ingresos> list;
        //  Context context;


        public viewHolderIngresos(View itemView, List<Ingresos> datos) {
            super(itemView);
            //  context = itemView.getContext();
            btnRegistrarSalida = itemView.findViewById(R.id.btnRegistrarSalidas);
            fechaHoraingresosExternos = itemView.findViewById(R.id.fechaHoraIngresosExternos);
            ingresanteExterno = itemView.findViewById(R.id.ingresanteExterno);
            // list = datos;


        }
    }
}