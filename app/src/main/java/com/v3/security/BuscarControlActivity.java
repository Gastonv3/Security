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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BuscarControlActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateSetListenerDesde, dateSetListenerHasta, dateSetListenerFecha;
    ImageButton ibdesde, ibhasta, ibbuscarresultado, ibfecha, ibbuscarfecha, ibsalir1, ibsalir2;
    TextView tvdesde, tvhasta, tvfecha;
    private String desde = null;
    private String hasta = null;
    private String fecha = null;

    private String desdeMostrar = null;
    private String hastaMostrar = null;
    private String fechaMostrar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_control);
        ibdesde = findViewById(R.id.ibBuscarIncio);
        ibhasta = findViewById(R.id.ibBuscarFinal);
        ibbuscarresultado = findViewById(R.id.ibBuscarResultado);
        ibsalir1 = findViewById(R.id.ibsalir1);
        ibsalir2 = findViewById(R.id.ibsalir2);
        ibfecha = findViewById(R.id.ibFechaUnica);
        ibbuscarfecha = findViewById(R.id.ibBuscarResultadoFecha);
        tvfecha = findViewById(R.id.tvFechaUnica);

        tvdesde = findViewById(R.id.tvDesde);
        tvhasta = findViewById(R.id.tvHasta);
        ibfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarControlActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerFecha,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListenerFecha = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String mes = String.valueOf(month);
                String dia = String.valueOf(day);
                if (month < 10) {
                    mes = "0" + month;
                }
                if (day < 10) {
                    dia = "0" + day;
                }
                fecha = year + "-" + mes + "-" + dia;
                fechaMostrar = dia + "-" + mes + "-" + year;
                tvfecha.setText(fechaMostrar);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibdesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarControlActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerDesde,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        dateSetListenerDesde = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String mes = String.valueOf(month);
                String dia = String.valueOf(day);
                if (month < 10) {
                    mes = "0" + month;
                }
                if (day < 10) {
                    dia = "0" + day;
                }
                desde = year + "-" + mes + "-" + dia;
                desdeMostrar = dia + "-" + mes + "-" + year;
                tvdesde.setText(desdeMostrar);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibhasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuscarControlActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerHasta,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        dateSetListenerHasta = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String mes = String.valueOf(month);
                String dia = String.valueOf(day);
                if (month < 10) {
                    mes = "0" + month;
                }
                if (day < 10) {
                    dia = "0" + day;
                }
                hasta = year + "-" + mes + "-" + dia;
                hastaMostrar = dia + "-" + mes + "-" + year;
                tvhasta.setText(hastaMostrar);


                //lista.clear();
                // cargarWebservice();
            }
        };
        ibbuscarresultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvdesde.getText().toString().isEmpty() || tvhasta.getText().toString().isEmpty()) {
                    AlertaError();
                } else {
                    String incio = desde;
                    String Final = hasta;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date a = sdf.parse(incio);
                        Date b = sdf.parse(Final);
                        if (tvdesde.getText().toString().isEmpty() || tvhasta.getText().toString().isEmpty()) {
                            AlertaError();
                        }
                        if (a.after(b)) {
                            AlertaError3();
                        } else if (b.before(a)) {
                            AlertaError4();
                        } else {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("desde", desde);
                            returnIntent.putExtra("hasta", hasta);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        ibbuscarfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvfecha.getText().toString().isEmpty()) {
                    AlertaError2();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("unica", fecha);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
        ibsalir1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibsalir2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AlertaError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarControlActivity.this, R.style.AlertDialogCustom);
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

    private void AlertaError2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarControlActivity.this, R.style.AlertDialogCustom);
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

    private void AlertaError3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarControlActivity.this, R.style.AlertDialogCustom);
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

    private void AlertaError4() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarControlActivity.this, R.style.AlertDialogCustom);
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
}


