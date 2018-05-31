package com.v3.security.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.v3.security.Clases.IngresosAutorizados;
import com.v3.security.R;
import com.v3.security.SupervisorAutorizadoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterHolderAutorizados extends RecyclerView.Adapter<AdapterHolderAutorizados.viewHolderSupervisorAutorizados> {
    private List<IngresosAutorizados> ListaObjetos;
    private CallbackInterface mCallback;
    private Context mContext;
    public interface CallbackInterface {

        void onHandleSelection(int pos, IngresosAutorizados ingresosAutorizados);
    }

    public AdapterHolderAutorizados(List<IngresosAutorizados> listaObjetos, Context context) {
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
    public viewHolderSupervisorAutorizados onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(mContext).inflate(R.layout.card_autorizados, parent, false);
        return new viewHolderSupervisorAutorizados(vista, ListaObjetos);
    }


    @Override
    public void onBindViewHolder(viewHolderSupervisorAutorizados holder, final int position) {

        holder.AutorizadoIngresos.setText((ListaObjetos.get(position).getPersonalAutorizado().getNombrePersonalAutorizado())+" "+(ListaObjetos.get(position).getPersonalAutorizado().getApellidoPersonalAutorizado()));
        String fechastring2 = ListaObjetos.get(position).getFechaHora();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String b = fmtOut.format(a);
            holder.fechaHoraExternoAutorizado.setText(b);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.btnregistrarsalidaautorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(position,ListaObjetos.get(position));
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
     public int getItemCount() {
        return ListaObjetos.size();
    }

    static class viewHolderSupervisorAutorizados extends RecyclerView.ViewHolder{
        Button btnregistrarsalidaautorizados;
        ImageView fotos;
        TextView AutorizadoIngresos, fechaHoraExternoAutorizado;




        public viewHolderSupervisorAutorizados(View itemView, List<IngresosAutorizados> datos) {
            super(itemView);
            btnregistrarsalidaautorizados = itemView.findViewById(R.id.btnRegistrarSalidasAutorizados);
            fechaHoraExternoAutorizado = itemView.findViewById(R.id.fechaHoraIngresosAutorizados);
            AutorizadoIngresos = itemView.findViewById(R.id.ingresanteExternoAutorizados);


        }

    }
}