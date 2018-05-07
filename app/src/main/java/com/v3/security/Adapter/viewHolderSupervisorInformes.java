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
import com.v3.security.R;
import com.v3.security.SupervisorInformesActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class viewHolderSupervisorInformes extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button btnCerrarInforme;
    ImageView fotos;
     TextView lugarinforme, fechaHorainforme;
    List<Informes> list;
    Context context;
    int idguardia;


    public viewHolderSupervisorInformes(View itemView, List<Informes> datos) {
        super(itemView);
        context = itemView.getContext();
        btnCerrarInforme = itemView.findViewById(R.id.btnMasInformacionInforme);
        fechaHorainforme = itemView.findViewById(R.id.fechaHoraInforme);
        lugarinforme = itemView.findViewById(R.id.lugarInforme);
        list = datos;


    }

    void setOnclickListener() {

        btnCerrarInforme.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Informes informes = list.get(position);
        if (view.getId() == btnCerrarInforme.getId()) {
          Bundle bundle = new Bundle();
           Bitmap bitmap = informes.getImagenInforme();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();
            Intent intent = new Intent(context, SupervisorInformesActivity.class);
            bundle.putByteArray("imagenInforme", b);
            bundle.putString("nombre",informes.getControl().getGuardia().getNombre());
            bundle.putString("apellido",informes.getControl().getGuardia().getApellido());
            bundle.putString("lugar",informes.getControl().getLugar().getNombre_lugares());
            bundle.putString("fechaHora",informes.getControl().getFechaHora());
            bundle.putString("informe",informes.getInforme());
            intent.putExtra("suerte",bundle);
            context.startActivity(intent);

        }

        }
    }


