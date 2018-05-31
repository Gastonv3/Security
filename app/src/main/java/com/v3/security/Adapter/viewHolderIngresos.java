package com.v3.security.Adapter;

import android.app.Activity;
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
import com.v3.security.IngresanteSalidaActivity;
import com.v3.security.R;
import com.v3.security.SupervisorIngresosActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class viewHolderIngresos extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button btnRegistrarSalida;
    ImageView fotos;
    TextView ingresanteExterno, fechaHoraingresosExternos;
    List<Ingresos> list;
    Context context;
    int idguardia;

    public static final int REQUEST_FOR_ACTIVITY_CODE = 758;
    public viewHolderIngresos(View itemView, List<Ingresos> datos) {
        super(itemView);
        context = itemView.getContext();
        btnRegistrarSalida = itemView.findViewById(R.id.btnRegistrarSalidas);
        fechaHoraingresosExternos = itemView.findViewById(R.id.fechaHoraIngresosExternos);
        ingresanteExterno = itemView.findViewById(R.id.ingresanteExterno);
        list = datos;


    }

    void setOnclickListener() {

        btnRegistrarSalida.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Ingresos ingresos = list.get(position);
        if (view.getId() == btnRegistrarSalida.getId()) {
            Bundle bundle = new Bundle();
            Bitmap bitmap = ingresos.getImagenIngresos();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();
            Intent intent = new Intent(context, IngresanteSalidaActivity.class);
            bundle.putByteArray("imagenIngresos", b);
            bundle.putInt("idIngreso",ingresos.getIdIngresos());
            bundle.putString("nombreIngreso", ingresos.getNombreIngreso());
            bundle.putString("apellidoIngreso", ingresos.getApellidoIngreso());
            bundle.putString("dni", ingresos.getDni());
            bundle.putString("motivo", ingresos.getMotivo());
            bundle.putString("fechaHoraIngreso", ingresos.getFechaHoraIngreso());
            bundle.putString("nombre", ingresos.getGuardia().getNombre());
            bundle.putString("apellido", ingresos.getGuardia().getApellido());
            intent.putExtra("ingresante", bundle);
           // context.startActivity(intent);
            ((Activity) context).startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);

        }


    }
}