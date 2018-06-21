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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BuscarAutorizadosActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListenerDesde, dateSetListenerHasta,
            dateSetListenerFecha;
    ImageButton ibBuscarIncioAutorizados , ibBuscarFinalAutorizados, ibBuscarResultadoAutorizados,
            ibFechaUnicaAutorizados,ibBuscarResultadoFechaAutorizados,
            ibsalirAutorizados, ibsalirAutorizados2, ibsalirAutorizados3 ,ibBuscarDniAutorizados;

    TextView tvDesdeAutorizados , tvHastaAutorizados, tvFechaUnicaAutorizados,tvDniAutorizados;
    private String dni = null;
    private String desde = null;
    private String hasta= null;
    private String fecha = null;
    private String desdeMostrar = null;
    private String hastaMostrar= null;
    private String fechaMostrar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_autorizados);

        ibBuscarIncioAutorizados = findViewById(R.id.ibBuscarIncioAutorizados);
        ibBuscarFinalAutorizados = findViewById(R.id.ibBuscarFinalAutorizados);
        ibBuscarResultadoAutorizados = findViewById(R.id.ibBuscarResultadoAutorizados);

        ibsalirAutorizados = findViewById(R.id.ibsalirAutorizados);
        ibsalirAutorizados2 = findViewById(R.id.ibsalirAutorizados2);
        //ibsalirAutorizados3 = findViewById(R.id.ibsalirAutorizados3);

        ibFechaUnicaAutorizados = findViewById(R.id.ibFechaUnicaAutorizados);
        ibBuscarResultadoFechaAutorizados = findViewById(R.id.ibBuscarResultadoFechaAutorizados);

        //ibBuscarDniAutorizados = findViewById(R.id.ibBuscarDniAutorizados);
        tvFechaUnicaAutorizados = findViewById(R.id.tvFechaUnicaAutorizados);
       // tvDniAutorizados = findViewById(R.id.tvDniAutorizados);
        tvDesdeAutorizados = findViewById(R.id.tvDesdeAutorizados);
        tvHastaAutorizados = findViewById(R.id.tvHastaAutorizados);

        ibFechaUnicaAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarAutorizadosActivity.this,
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
                fechaMostrar = dia +"-"+ mes +"-"+year;
                tvFechaUnicaAutorizados.setText(fechaMostrar);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibBuscarIncioAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarAutorizadosActivity.this,
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
                desdeMostrar = dia +"-"+ mes +"-"+year;
                tvDesdeAutorizados.setText(desdeMostrar);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibBuscarFinalAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarAutorizadosActivity.this,
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
                hastaMostrar = dia +"-"+ mes +"-"+year;
                tvHastaAutorizados.setText(hastaMostrar);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibBuscarResultadoAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvDesdeAutorizados.getText().toString().isEmpty()||tvHastaAutorizados.getText().toString().isEmpty()) {
                    AlertaError();
                }else {
                    String incio = desde;
                    String Final = hasta;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date a = sdf.parse(incio);
                        Date                                                                                                                                                                                                                                                                         b = sdf.parse(Final);
                        if(tvDesdeAutorizados.getText().toString().isEmpty()||tvHastaAutorizados.getText().toString().isEmpty()){
                            AlertaError();
                        }if(a.after(b)) {
                            AlertaError3();
                        }else if(b.before(a)) {
                            AlertaError4();
                        }else {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("desde",desde);
                            returnIntent.putExtra("hasta",hasta);
                            setResult(Activity.RESULT_OK,returnIntent); finish();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ibBuscarResultadoFechaAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvFechaUnicaAutorizados.getText().toString().isEmpty()){
                    AlertaError2();
                }else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("unica",fecha);
                    setResult(Activity.RESULT_OK,returnIntent); finish();
                }

            }
        });
        ibsalirAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibsalirAutorizados2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /*ibsalirAutorizados3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
       /* ibBuscarDniAutorizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvDniAutorizados.getText().toString().isEmpty()){
                    AlertaErrorDni();
                }else {
                    dni = String.valueOf(tvDniAutorizados.getText());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("dni",dni);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });*/


    }
    private void AlertaError (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarAutorizadosActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Debe seleccionar 2 fechas.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaError2 (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarAutorizadosActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Debe seleccionar una fecha.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaError3 (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarAutorizadosActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("La Fecha Final es anterior a la Fecha Inicial.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaError4 (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarAutorizadosActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("La fecha final es menor que la incial.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaErrorDni (){
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarAutorizadosActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Debe ingresas un DNI.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}