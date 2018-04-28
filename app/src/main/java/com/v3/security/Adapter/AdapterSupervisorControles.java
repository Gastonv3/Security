package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v3.security.Clases.Control;
import com.v3.security.Clases.Control2;
import com.v3.security.Clases.Lugar;
import com.v3.security.R;

import java.util.List;

public class AdapterSupervisorControles extends RecyclerView.Adapter<viewHolderSupervisorControles> {
    List<Control2> ListaObjetos;

    public AdapterSupervisorControles(List<Control2> listaObjetos) {
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
        holder.hora.setText(ListaObjetos.get(position).getFechaHora());
        holder.setOnclickListener();

    }


    @Override
    public int getItemCount() {
        return ListaObjetos.size();
    }
}









