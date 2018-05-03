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

import uk.co.senab.photoview.PhotoViewAttacher;

public class SupervisorIngresosActivity extends AppCompatActivity {
    TextView nombreGuardiaIngresos, nombreIngreso, dniIngreso, fechaIngreso, motivoIngreso;
    EditText prueba;
    ImageView ingresoImagen;
    ImageButton ibrotar;
    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_ingresos);
        nombreGuardiaIngresos = findViewById(R.id.nombreGuardiaIngresos);
        nombreIngreso = findViewById(R.id.nombreIngreso);
        dniIngreso = findViewById(R.id.dniIngreso);
        fechaIngreso = findViewById(R.id.fechaIngreso);
        motivoIngreso = findViewById(R.id.motivoIngreso);
        ingresoImagen = findViewById(R.id.ingresoImagen);
        ibrotar = findViewById(R.id.ibRotarImagen);
        Bundle extras = getIntent().getBundleExtra("ingresos");
        byte[] b = extras.getByteArray("imagenIngresos");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        ingresoImagen.setImageBitmap(bmp);
        photoViewAttacher = new PhotoViewAttacher(ingresoImagen);
        dniIngreso.setText("Dni: "+(extras.getString("dni")));
        nombreIngreso.setText("Ingresante: "+(extras.getString("nombreIngreso"))+" "+(extras.getString("apellidoIngreso")));
        String fechastring2 = extras.getString("fechaHoraIngreso");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String c = fmtOut.format(a);
            fechaIngreso.setText("Fecha y Hora: "+c);

            motivoIngreso.setText("Motivo: "+(extras.getString("motivo")));
            motivoIngreso.setMovementMethod(new ScrollingMovementMethod());
            nombreGuardiaIngresos.setText("Guardia: "+(extras.getString("nombre"))+" "+(extras.getString("apellido")));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        ibrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresoImagen.setRotation(ingresoImagen.getRotation()+90);
            }
        });

    }
}
