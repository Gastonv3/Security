package com.v3.security.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v3.security.Clases.Informes;
import com.v3.security.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        holder.lugarinforme.setText(ListaObjetos.get(position).getControl().getLugar().getNombre_lugares());
        String fechastring2 = ListaObjetos.get(position).getControl().getFechaHora();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String b = fmtOut.format(a);
            holder.fechaHorainforme.setText(b);
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