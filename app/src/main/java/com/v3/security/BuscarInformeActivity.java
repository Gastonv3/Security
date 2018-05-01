package com.v3.security;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

public class BuscarInformeActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListenerDesde, dateSetListenerHasta,
            dateSetListenerFecha;
    ImageButton ibdesdeinforme , ibhastainforme, ibbuscarresultadoinforme, ibfechainforme,ibbuscarfechainforme,
            ibsalir1informe, ibsalir2informe;
    TextView tvdesdeinforme , tvhastainforme, tvfechainforme;
    private String desde = null;
    private String hasta= null;
    private String fecha = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_informe);

        ibdesdeinforme = findViewById(R.id.ibBuscarIncioInforme);
        ibhastainforme = findViewById(R.id.ibBuscarFinalInforme);
        ibbuscarresultadoinforme = findViewById(R.id.ibBuscarResultadoInforme);
        ibsalir1informe = findViewById(R.id.ibsalirInforme);
        ibsalir2informe = findViewById(R.id.ibsalirInforme2);
        ibfechainforme = findViewById(R.id.ibFechaUnicaInforme);
        ibbuscarfechainforme = findViewById(R.id.ibBuscarResultadoFechaInforme);
        tvfechainforme = findViewById(R.id.tvFechaUnicaInforme);

        tvdesdeinforme = findViewById(R.id.tvDesdeInforme);
        tvhastainforme = findViewById(R.id.tvHastaInforme);

        ibfechainforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarInformeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerFecha,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListenerFecha = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year , int month, int day) {
                month = month +1;
                String mes = String.valueOf(month);
                String dia = String.valueOf(day);
                if(month<10){
                    mes = "0"+ month;
                }if(day<10){
                    dia = "0" + day;
                }
                fecha = year +"-"+ mes +"-"+dia;

                tvfechainforme.setText(fecha);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibdesdeinforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarInformeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerDesde,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        dateSetListenerDesde = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year , int month, int day) {
                month = month +1;
                String mes = String.valueOf(month);
                String dia = String.valueOf(day);
                if(month<10){
                    mes = "0"+ month;
                }if(day<10){
                    dia = "0" + day;
                }
                desde = year +"-"+ mes +"-"+dia;

                tvdesdeinforme.setText(desde);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibhastainforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarInformeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerHasta,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        dateSetListenerHasta = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year , int month, int day) {
                month = month +1;
                String mes = String.valueOf(month);
                String dia = String.valueOf(day);
                if(month<10){
                    mes = "0"+ month;
                }if(day<10){
                    dia = "0" + day;
                }
                hasta = year +"-"+ mes +"-"+dia;

                tvhastainforme.setText(hasta);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibbuscarresultadoinforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvdesdeinforme.getText().toString().isEmpty()||tvhastainforme.getText().toString().isEmpty()){
                    AlertaError();
                }else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("desde",desde);
                    returnIntent.putExtra("hasta",hasta);
                    setResult(Activity.RESULT_OK,returnIntent); finish();
                }

            }
        });
        ibbuscarfechainforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvfechainforme.getText().toString().isEmpty()){
                    AlertaError2();
                }else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("unica",fecha);
                    setResult(Activity.RESULT_OK,returnIntent); finish();
                }

            }
        });
        ibsalir1informe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibsalir2informe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void AlertaError (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarInformeActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Debe seleccionar 2 fechas");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaError2 (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarInformeActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Debe seleccionar una fecha");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}