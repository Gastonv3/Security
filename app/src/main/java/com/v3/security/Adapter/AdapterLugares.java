package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v3.security.Clases.Lugar;
import com.v3.security.R;

import java.util.List;

/**
 * Created by Skylake on 25/3/2018.
 */

public class AdapterLugares extends RecyclerView.Adapter<viewHolderLugares> {
    List<Lugar> ListaObjetos;

    public AdapterLugares(List<Lugar> listaObjetos) {
        ListaObjetos = listaObjetos;

    }

    @Override
    public viewHolderLugares onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_lugares,parent,false);
        return new viewHolderLugares(vista, ListaObjetos);
    }



    @Override
    //recibe el viewonldar y la pocision
    public void onBindViewHolder(viewHolderLugares holder, int position) {
        holder.titulos.setText(ListaObjetos.get(position).getNombre_lugares());
        if (ListaObjetos.get(position).getImagen()!=null){
            holder.fotos.setImageBitmap(ListaObjetos.get(position).getImagen());

        }else{
            holder.fotos.setImageResource(R.drawable.ic_lock_black_24px);
        }

        //holder.fotos.setImageResource(ListaObjetos.get(position).getImagen());
        holder.setOnclickListener();
    }

    @Override
    public int getItemCount() {
        return ListaObjetos.size();
    }
}
