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

public class SupervisorInformesActivity extends AppCompatActivity {
    TextView nombreguardainforme, nombrelugarinforme, informe, fechaInforme;
    EditText prueba;
    ImageView imageninforme;
    ImageButton ibrotar;
    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_informes);
        nombreguardainforme = findViewById(R.id.nombreGuardiaInforme);
        nombrelugarinforme = findViewById(R.id.nombreLugarInforme);
        informe = findViewById(R.id.informe);
        fechaInforme = findViewById(R.id.fechaInforme);
        imageninforme = findViewById(R.id.informeImagen);
        ibrotar = findViewById(R.id.ibRotarImagen);
       Bundle extras = getIntent().getBundleExtra("suerte");
        byte[] b = extras.getByteArray("imagenInforme");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageninforme.setImageBitmap(bmp);
        photoViewAttacher = new PhotoViewAttacher(imageninforme);
        nombrelugarinforme.setText("Lugar: "+extras.getString("lugar"));
        String fechastring2 = extras.getString("fechaHora");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date a = sdf.parse(fechastring2);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String c = fmtOut.format(a);
            fechaInforme.setText("Fecha y Hora: "+c);

            informe.setText("Informe: "+(extras.getString("informe")));
            informe.setMovementMethod(new ScrollingMovementMethod());
            nombreguardainforme.setText("Guardia: "+(extras.getString("nombre"))+" "+(extras.getString("apellido")));

        } catch (ParseException e) {
            e.printStackTrace();
        }





        ibrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageninforme.setRotation(imageninforme.getRotation()+90);
            }
        });


    }

}
