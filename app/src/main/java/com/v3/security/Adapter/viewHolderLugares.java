package com.v3.security.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.v3.security.Clases.Lugar;
import com.v3.security.ControlActivity;
import com.v3.security.LoginActivity;
import com.v3.security.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Skylake on 25/3/2018.
 */

public class viewHolderLugares extends RecyclerView.ViewHolder implements View.OnClickListener {
    Button btnCerrar;
    ImageView fotos;
    TextView titulos;
    List<Lugar> list;
    Context context;
    int idguardia;


    public viewHolderLugares(View itemView, List<Lugar> datos) {
        super(itemView);
        context = itemView.getContext();
        btnCerrar = itemView.findViewById(R.id.btnCerrar);
        fotos = (ImageView) itemView.findViewById(R.id.coverImageView);
        titulos = (TextView) itemView.findViewById(R.id.titleTextView);
        list = datos;


    }

    void setOnclickListener() {
        btnCerrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        Lugar lugar = list.get(position);
        if (view.getId() == btnCerrar.getId()) {
            Bundle bundle = new Bundle();
            Bitmap bitmap = lugar.getImagen();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            Intent intent = new Intent(context, ControlActivity.class);
            bundle.putByteArray("imagen", b);
            bundle.putInt("idLugar", lugar.getIdLugares());
            bundle.putString("nombre", lugar.getNombre_lugares());
            intent.putExtra("picture", bundle);
            context.startActivity(intent);

        }

    }
}
