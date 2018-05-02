package com.v3.security;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupervisorAutorizadoActivity extends AppCompatActivity {
    TextView nombreGuardiaAutorizado, personaAutorizada, dniAutorizado, cargoAutorizado, fechaAutorizado;
    EditText prueba;
   /* ImageView imageninforme;
    ImageButton ibrotar;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_autorizado);
        nombreGuardiaAutorizado = findViewById(R.id.nombreGuardiaAutorizado);
        personaAutorizada = findViewById(R.id.personaAutorizada);
        dniAutorizado = findViewById(R.id.dniAutorizado);
        cargoAutorizado = findViewById(R.id.cargoAutorizado);
        fechaAutorizado = findViewById(R.id.fechaAutorizado);
        Bundle extras = getIntent().getBundleExtra("ingresosAutorizados");
        /*ibrotar = findViewById(R.id.ibRotarImagen);
        Bundle extras = getIntent().getBundleExtra("suerte");
        byte[] b = extras.getByteArray("imagenInforme");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageninforme.setImageBitmap(bmp);*/
        personaAutorizada.setText("Ingresante: "+(extras.getString("nombrePersonalAutorizado"))+" "+(extras.getString("apellidoPersonalAutorizado")));
        String fechastring2 = extras.getString("fechaHora");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String c = fmtOut.format(a);
            fechaAutorizado.setText("Fecha y Hora: "+c);
            cargoAutorizado.setText("Cargo: "+(extras.getString("cargo")));
            dniAutorizado.setText("Dni: "+(extras.getString("dni")));
            /*informe.setText(extras.getString("informe"));
            informe.setMovementMethod(new ScrollingMovementMethod());*/
            nombreGuardiaAutorizado.setText("Guardia: "+(extras.getString("nombre"))+" "+(extras.getString("apellido")));

        } catch (ParseException e) {
            e.printStackTrace();
        }





     /*   ibrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageninforme.setRotation(imageninforme.getRotation()+90);
            }
        });*/


    }

}