package com.v3.security;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.v3.security.Clases.Control2;
import com.v3.security.Clases.Informes;
import com.v3.security.R;

import java.io.IOException;

public class SupervisorInformesActivity extends AppCompatActivity {
    TextView nombreguardainforme, nombrelugarinforme, informe, fechaInforme;
    EditText prueba;
    ImageView imageninforme;
    ImageButton ibrotar;
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
        nombrelugarinforme.setText("Lugar: "+extras.getString("lugar"));
        fechaInforme.setText("Fecha y Hora: "+extras.getString("fechaHora"));

        informe.setText(extras.getString("informe"));
        informe.setMovementMethod(new ScrollingMovementMethod());
        ibrotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageninforme.setRotation(imageninforme.getRotation()+90);
            }
        });

        nombreguardainforme.setText("Guardia: "+(extras.getString("nombre"))+" "+(extras.getString("apellido")));

    }

}
