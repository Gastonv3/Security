package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.v3.security.Clases.Control;

import com.v3.security.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterSupervisorControles extends RecyclerView.Adapter<viewHolderSupervisorControles> {
    List<Control> ListaObjetos;

    public AdapterSupervisorControles(List<Control> listaObjetos) {
        ListaObjetos = listaObjetos;

    }

    @Override
    public viewHolderSupervisorControles onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_supervisorcontroles, parent, false);
        return new viewHolderSupervisorControles(vista, ListaObjetos);
    }

    @Override
    public void onBindViewHolder(viewHolderSupervisorControles holder, int position) {
        holder.id.setText(ListaObjetos.get(position).getLugar().getNombre_lugares());
        String fechastring2 = ListaObjetos.get(position).getFechaHora();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String b = fmtOut.format(a);
            holder.hora.setText(b);
            holder.setOnclickListener();

        } catch (ParseException e) {
            e.printStackTrace();
        }






    }


    @Override
    public int getItemCount() {
        return ListaObjetos.size();
    }
}









