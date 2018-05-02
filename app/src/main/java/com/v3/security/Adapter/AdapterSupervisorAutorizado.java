package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v3.security.Clases.Ingresos;
import com.v3.security.Clases.IngresosAutorizados;
import com.v3.security.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterSupervisorAutorizado extends RecyclerView.Adapter<viewHolderSupervisorAutorizados> {
private List<IngresosAutorizados> ListaObjetos;

public AdapterSupervisorAutorizado(List<IngresosAutorizados> listaObjetos) {
        ListaObjetos = listaObjetos;

        }

@Override
public viewHolderSupervisorAutorizados onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_supervisorautorizados, parent, false);
        return new viewHolderSupervisorAutorizados(vista, ListaObjetos);
        }


@Override
public void onBindViewHolder(viewHolderSupervisorAutorizados holder, int position) {

        holder.Autorizado.setText((ListaObjetos.get(position).getPersonalAutorizado().getNombrePersonalAutorizado())+" "+(ListaObjetos.get(position).getPersonalAutorizado().getApellidoPersonalAutorizado()));
        String fechastring2 = ListaObjetos.get(position).getFechaHora();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
        Date a = sdf.parse(fechastring2);
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String b = fmtOut.format(a);
        holder.fechaHoraAutorizado.setText(b);
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