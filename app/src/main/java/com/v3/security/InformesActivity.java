package com.v3.security;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.v3.security.Clases.Control;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class InformesActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    EditText etinforme;
    Context context;
    ImageView imageView;
    JsonObjectRequest jsonObjectRequest;
    Button btnInsertarInforme, btnFoto, btnCancelar;
    String path;
    StringRequest stringRequest;
    int idControl;
    private final String CARPETA_RAIZ = "misImagenesPueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    final int CODIGO_FOTO = 20;
    final int CODIGO_SELECCIONA = 10;
    Bitmap bitmap;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODIGO_SELECCIONA:
                    Uri miPath = data.getData();
                    imageView.setImageURI(miPath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), miPath);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CODIGO_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.i("Ruta de almacenamiento", "Path: " + path);
                        }
                    });
                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = redimensionarImagen(bitmap, 1280, 960);

                    rotateimagen(bitmap);
                   // imageView.setImageBitmap(bitmap);

                    break;
            }
        }
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        context = this;
        etinforme = findViewById(R.id.etInforme);
        imageView = findViewById(R.id.ivfoto);
        btnInsertarInforme = findViewById(R.id.btnInsertarInforme);
        btnFoto = findViewById(R.id.btnfoto);
        btnCancelar = findViewById(R.id.btnCancelar);
        extraerId();
        if (validaPermisos()) {
            btnFoto.setEnabled(true);
        } else {
            btnFoto.setEnabled(false);
        }
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence opciones[] = {"Tomar foto", "Cargar Imagen", "Cancelar"};
                final AlertDialog.Builder alertaOpciones = new AlertDialog.Builder(context);
                alertaOpciones.setTitle("Seleccione una Opción");
                alertaOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (opciones[i].equals("Tomar foto")) {
                            Toast.makeText(context, "a", Toast.LENGTH_SHORT).show();
                            tomarFoto();
                        } else {
                            if (opciones[i].equals("Cargar Imagen")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/");
                                startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicacion"), CODIGO_SELECCIONA);
                            } else {
                                dialogInterface.dismiss();
                            }
                        }
                    }
                });
                alertaOpciones.show();
            }
        });

        btnInsertarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etinforme.getText().toString().isEmpty()){
                    Toast.makeText(context, "Debe Ingresar un informe", Toast.LENGTH_SHORT).show();
                }else{
                    cargarInforme();
                }


            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarControl();
                finish();
            }
        });

    }

    private boolean validaPermisos() {
//si buld.version.sdk_int es menor a build.VERSION_CODES.M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;

        }//en caso de que sea falso pregunto si los permisos estan activos
        //checkeo si los permisos estan aceptados
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        //en caso de que la version no sea menor y los permisos no se hayan dado debe solicitar los permisos
        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        }//solicita los permisos
        else {

            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);

        }
        return false;
    }

    //recibe el string de persimoss, el codigo
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            //verifica si tya se le dio el permisos despues de que aparezca la ventana que lo solicita
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnFoto.setEnabled(true);
            }//si desea solicitar los permisos manual
            else {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence opciones[] = {"Si", "No"};
        final AlertDialog.Builder alertaOpciones = new AlertDialog.Builder(context);
        alertaOpciones.setTitle("¿Desea configurar los permisos de froma manual?");
        alertaOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertaOpciones.show();
    }


    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(InformesActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        //si el usuario da aceptar carga los permisos de escritura y camara
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
                }
            }
        });
        dialogo.show();
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
            Uri imageUri = FileProvider.getUriForFile(this, authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, CODIGO_FOTO);

    }

    private void extraerId() {

        String url = "http://192.168.0.14/seguridad/extraerMayorId.php";
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    private  void eliminarControl(){
        String url = "http://192.168.0.14/seguridad/eliminarcontrol.php?idControles="+idControl;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "se elimino", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    //subir imagen
    public void cargarInforme() {

        String url = "http://192.168.0.14/seguridad/informe2.php?";
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")) {
                    etinforme.setText("");
                    Toast.makeText(context, "Se ha registrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No ha registrado", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();

            }
        }) {
            //enviar datos mediante post
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String idinforme="a";
                String idControles= String.valueOf(idControl);
                String 	observacion="hola";
               // String fecha_hora="2018-03-31 01:41:48"
                String foto = null;
                if (bitmap==null){
                    Bitmap bitmap2= BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.img_base);
                    foto=convertirImgString(bitmap2);
                }else {
                    foto=convertirImgString(bitmap);
                }



                Map<String,String> parametros=new HashMap<>();
                parametros.put("idinforme",idinforme);
                parametros.put("idControles",idControles);
                parametros.put("observacion",observacion);
              //  parametros.put("fecha_hora",fecha_hora);
                parametros.put("foto",foto);

                return parametros;


            }
        };
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Control control = null;
        JSONArray json = response.optJSONArray("datos");
        try {
            control = new Control();
            JSONObject jsonObject = null;
            jsonObject = json.getJSONObject(0);
            control.setIdControles(jsonObject.optInt("idControles"));
            control.setIdGuardia(jsonObject.optInt("idGuardia"));
            control.setIdLugares(jsonObject.getInt("idLugares"));
            control.setCoordenadas(jsonObject.getString("coordenadas"));
            control.setEstado(jsonObject.getInt("Estado"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        idControl = control.getIdControles();
    }
    private void rotateimagen (Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface=new ExifInterface(path);
        }catch (IOException e){
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);

                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        imageView.setImageBitmap(rotateBitmap);
    }


}
