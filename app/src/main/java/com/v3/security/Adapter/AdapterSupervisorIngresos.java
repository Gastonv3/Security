package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v3.security.Clases.Informes;
import com.v3.security.Clases.Ingresos;
import com.v3.security.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterSupervisorIngresos extends RecyclerView.Adapter<viewHolderSupervisorIngresos> {
    private List<Ingresos> ListaObjetos;

    public AdapterSupervisorIngresos(List<Ingresos> listaObjetos) {
        ListaObjetos = listaObjetos;

    }

    @Override
    public viewHolderSupervisorIngresos onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_supervisoringresos, parent, false);
        return new viewHolderSupervisorIngresos(vista, ListaObjetos);
    }


    @Override
    public void onBindViewHolder(viewHolderSupervisorIngresos holder, int position) {

        holder.ingresante.setText((ListaObjetos.get(position).getNombreIngreso())+" "+(ListaObjetos.get(position).getApellidoIngreso()));
        String fechastring2 = ListaObjetos.get(position).getFechaHoraIngreso();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String b = fmtOut.format(a);
            holder.fechaHoraingresos.setText(b);
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