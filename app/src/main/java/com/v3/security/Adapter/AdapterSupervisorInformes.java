package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v3.security.Clases.Control2;
import com.v3.security.Clases.Informes;
import com.v3.security.R;

import java.util.List;

public class AdapterSupervisorInformes extends RecyclerView.Adapter<viewHolderSupervisorInformes> {
   private List<Informes> ListaObjetos;

    public AdapterSupervisorInformes(List<Informes> listaObjetos) {
        ListaObjetos = listaObjetos;

    }

    @Override
    public viewHolderSupervisorInformes onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_supervisorinformes, parent, false);
        return new viewHolderSupervisorInformes(vista, ListaObjetos);
    }


    @Override
    public void onBindViewHolder(viewHolderSupervisorInformes holder, int position) {

        holder.lugarinforme.setText(ListaObjetos.get(position).getControl2().getLugar().getNombre_lugares());
        holder.fechaHorainforme.setText(ListaObjetos.get(position).getControl2().getFechaHora());
        holder.setOnclickListener();

    }


    @Override
    public int getItemCount() {
        return ListaObjetos.size();
    }
}