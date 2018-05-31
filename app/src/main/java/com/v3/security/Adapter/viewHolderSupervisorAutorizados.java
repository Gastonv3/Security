package com.v3.security.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v3.security.Clases.Ingresos;
import com.v3.security.Clases.IngresosAutorizados;
import com.v3.security.Clases.PersonalAutorizado;
import com.v3.security.R;
import com.v3.security.SupervisorAutorizadoActivity;
import com.v3.security.SupervisorIngresosActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class viewHolderSupervisorAutorizados extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button btnMasInformacionAutorizado;
    ImageView fotos;
    TextView Autorizado, fechaHoraAutorizado;
    List<IngresosAutorizados> list;
    Context context;
    int idguardia;


    public viewHolderSupervisorAutorizados(View itemView, List<IngresosAutorizados> datos) {
        super(itemView);
        context = itemView.getContext();
        btnMasInformacionAutorizado = itemView.findViewById(R.id.btnMasInformacionAutorizado);
        fechaHoraAutorizado = itemView.findViewById(R.id.fechaHoraAutorizado);
        Autorizado = itemView.findViewById(R.id.Autorizado);
        list = datos;


    }

    void setOnclickListener() {

        btnMasInformacionAutorizado.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        IngresosAutorizados ingresosAutorizados = list.get(position);
        if (view.getId() == btnMasInformacionAutorizado.getId()) {
            Bundle bundle = new Bundle();
            /*Bitmap bitmap = ingresos.getImagenIngresos();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();*/
            Intent intent = new Intent(context, SupervisorAutorizadoActivity.class);
            //bundle.putByteArray("imagenIngresos", b);
            bundle.putString("nombrePersonalAutorizado", ingresosAutorizados.getPersonalAutorizado().getNombrePersonalAutorizado());
            bundle.putString("apellidoPersonalAutorizado", ingresosAutorizados.getPersonalAutorizado().getApellidoPersonalAutorizado());
            bundle.putString("dni", ingresosAutorizados.getPersonalAutorizado().getDni());
            bundle.putString("cargo", ingresosAutorizados.getPersonalAutorizado().getCargo());
            bundle.putString("fechaHora", ingresosAutorizados.getFechaHora());
            bundle.putString("fechaHoraSalida", ingresosAutorizados.getFechaHoraSalida());
            bundle.putString("nombre", ingresosAutorizados.getGuardia().getNombre());
            bundle.putString("apellido", ingresosAutorizados.getGuardia().getApellido());
            intent.putExtra("ingresosAutorizados", bundle);
            context.startActivity(intent);

        }


    }
}