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

import com.v3.security.Clases.Informes;
import com.v3.security.Clases.Ingresos;
import com.v3.security.R;
import com.v3.security.SupervisorInformesActivity;
import com.v3.security.SupervisorIngresosActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class viewHolderSupervisorIngresos extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button btnMasInformacionIngresante;
    ImageView fotos;
    TextView ingresante, fechaHoraingresos;
    List<Ingresos> list;
    Context context;
    int idguardia;


    public viewHolderSupervisorIngresos(View itemView, List<Ingresos> datos) {
        super(itemView);
        context = itemView.getContext();
        btnMasInformacionIngresante = itemView.findViewById(R.id.btnMasInformacionIngresante);
        fechaHoraingresos = itemView.findViewById(R.id.fechaHoraingresos);
        ingresante = itemView.findViewById(R.id.ingresante);
        list = datos;


    }

    void setOnclickListener() {

        btnMasInformacionIngresante.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Ingresos ingresos = list.get(position);
        if (view.getId() == btnMasInformacionIngresante.getId()) {
            Bundle bundle = new Bundle();
            Bitmap bitmap = ingresos.getImagenIngresos();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();
            Intent intent = new Intent(context, SupervisorIngresosActivity.class);
            bundle.putByteArray("imagenIngresos", b);
            bundle.putString("nombreIngreso", ingresos.getNombreIngreso());
            bundle.putString("apellidoIngreso", ingresos.getApellidoIngreso());
            bundle.putString("dni", ingresos.getDni());
            bundle.putString("motivo", ingresos.getMotivo());
            bundle.putString("fechaHoraIngreso", ingresos.getFechaHoraIngreso());
            bundle.putString("nombre", ingresos.getGuardia().getNombre());
            bundle.putString("apellido", ingresos.getGuardia().getApellido());
            intent.putExtra("ingresos", bundle);
            context.startActivity(intent);

        }


    }
}