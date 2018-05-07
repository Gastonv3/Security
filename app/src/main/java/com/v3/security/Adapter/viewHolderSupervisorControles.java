package com.v3.security.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v3.security.Clases.Control;
import com.v3.security.R;
import com.v3.security.SupervisorControlActivity;

import java.util.List;

public class viewHolderSupervisorControles extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button btnCerrar;
    ImageView fotos;
    TextView id, hora;
    List<Control> list;
    Context context;
    int idguardia;


    public viewHolderSupervisorControles(View itemView, List<Control> datos) {
        super(itemView);
        context = itemView.getContext();
        btnCerrar = itemView.findViewById(R.id.btnMasInformacion);
        hora = itemView.findViewById(R.id.fechaHora);
        id = itemView.findViewById(R.id.lugarControl);
        list = datos;


    }

    void setOnclickListener() {

        btnCerrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Control control = list.get(position);
        if (view.getId() == btnCerrar.getId()) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, SupervisorControlActivity.class);
            bundle.putSerializable("control",control);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }

    }

}

