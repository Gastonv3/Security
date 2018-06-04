package com.v3.security;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrarIngresosActivity extends AppCompatActivity {
    EditText etNombre, etApellido, etMotivo, etDni;
    ImageView ivFotoRegistro;
    float rotador;
    Context context;
    Bitmap bitmap;
    String path;
    ImageButton ibFotoRegistro, ibEnviarRegistro;
    private final String CARPETA_RAIZ = "misImagenesPueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    final int CODIGO_FOTO = 30;
    final int CODIGO_SELECCIONA = 40;
    int idguardia;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_ingresos);
        context = this;
        etNombre = findViewById(R.id.etNombreIngresante);
        etApellido = findViewById(R.id.etApellidoIngresante);
        etMotivo =findViewById(R.id.etMotivoIngresante);
        etDni = findViewById(R.id.etDniIngresante);
        idguardia = Preferencias.getInteger(context, Preferencias.getKeyGuardia());

        ivFotoRegistro = findViewById(R.id.ivFotoRegistroIngresante);

        ibFotoRegistro = findViewById(R.id.ibFotoRegistroIngresante);
        ibEnviarRegistro = findViewById(R.id.ibEnviarRegistroIngresante);

        ibFotoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });
        ibEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNombre.getText().toString().trim().isEmpty()) {
                    camposVacios();
                } else if (etApellido.getText().toString().trim().isEmpty()) {
                    camposVacios();
                } else if (etDni.getText().toString().trim().isEmpty()) {
                    camposVacios();
                } else if (etMotivo.getText().toString().trim().isEmpty()) {
                    camposVacios();
                } else {

                    ingresarVisitas();


                }


            }
        });

    /*    btnFotoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });
        btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarVisitas();
            }
        });*/


    }

 /*   @Override
    public void finish() {
        Intent i = new Intent();
        int ok2 = 1;
        i.putExtra("ok",ok2);
        setResult(RESULT_OK, i);
        super.finish();
    }*/

    private void ingresarVisitas() {
        progressDialog = new ProgressDialog(context);
       progressDialog.setMessage("Cargando...");
       progressDialog.setCancelable(false);
       progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/registrarIngresos.php?";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                etNombre.setText("");
                etApellido.setText("");
                etDni.setText("");
                etMotivo.setText("");
                ivFotoRegistro.setImageResource(R.drawable.img_base);
                progressDialog.dismiss();
                Toast.makeText(context, "Se registró correctamente", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertaError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String idIngresos = "a";
                String idGuardia = String.valueOf(idguardia);
                String nombre = etNombre.getText().toString();
                String apellido = etApellido.getText().toString();
                String dni = etDni.getText().toString();
                String motivo = etMotivo.getText().toString();
                String imagenRegistro = null;
                if (bitmap == null) {
                    Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.img_base);
                    imagenRegistro = convertirImgString(bitmap2);
                } else {
                    imagenRegistro = convertirImgString(bitmap);
                }
                Map<String, String> para = new HashMap<>();
                para.put("idIngresos", idIngresos);
                para.put("idGuardia", idGuardia);
                para.put("nombreIngresante", nombre);
                para.put("apellidoIngresante", apellido);
                para.put("dni", dni);
                para.put("motivo", motivo);
                para.put("imagenRegistro", imagenRegistro);
                return para;
            }
        };
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }

    private void tomarFoto() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }


        path = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + nombreImagen;

        File imagen = new File(path);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getApplicationContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(context, authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, CODIGO_FOTO);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODIGO_SELECCIONA:
                    Uri miPath = data.getData();
                    ivFotoRegistro.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), miPath);
                        ivFotoRegistro.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CODIGO_FOTO:
                    MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.i("Ruta de almacenamiento", "Path: " + path);
                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = redimensionarImagen(bitmap, 1600, 1200);

                    rotateimagen(bitmap);
                    // imageView.setImageBitmap(bitmap);

                    break;
            }
        }
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float rotador = 0;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotador = 90;

                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotador = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotador = 270;
                break;
            default:
        }
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();
        //float a = 0.0;
        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);
            matrix.postRotate(rotador);
            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }

    private void rotateimagen(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);

                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        ivFotoRegistro.setImageBitmap(rotateBitmap);
    }
    private void AlertaError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Error al registrar, comprueba tu conexión");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    private void camposVacios() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Debe rellenar todo el formulario");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
}
