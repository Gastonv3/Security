package com.v3.security;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.v3.security.Clases.Control;
import com.v3.security.Clases.Email;
import com.v3.security.Clases.Guardia;
import com.v3.security.Clases.Lugar;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Session;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class InformesActivity extends AppCompatActivity {
    Session session = null;
    String to, subject, body;
    String usuarioEmail= "informessy@gmail.com";
    String contraseñaEmail="seguridadinformes18";

    EditText etinforme, etTituloInforme;
    Context context;
    ImageView imageView;
    JsonObjectRequest jsonObjectRequest;
    Button btnInsertarInforme, btnFoto, btnCancelar;
    String path = null;
    String path2 = null;
    String path3 = null;
    String path4 = null;
    StringRequest stringRequest;
    int idControl;
    String email;
    private final String CARPETA_RAIZ = "Security/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "SecurityImagenes";
    final int CODIGO_FOTO = 20;
    final int CODIGO_SELECCIONA = 10;
    Bitmap bitmap;
    ImageButton ibInsertarInforme, ibCancelar, ibFoto;
    ProgressDialog progressDialog;


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
                    bitmap = redimensionarImagen(bitmap, 1600, 1200);

                    rotateimagen(bitmap);
                    guardar(bitmap);
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

    private Bitmap redimensionarLogo(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

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
        //etTituloInforme = findViewById(R.id.etTituloInforme);
        etinforme = findViewById(R.id.etInforme);
        imageView = findViewById(R.id.ivfoto);
        // btnInsertarInforme = findViewById(R.id.btnInsertarInforme);
        // btnFoto = findViewById(R.id.btnfoto);
        //  btnCancelar = findViewById(R.id.btnCancelar);
        ibInsertarInforme = findViewById(R.id.ibInsertarInforme);
        ibCancelar = findViewById(R.id.ibCancelar);
        ibFoto = findViewById(R.id.ibFoto);
        guardarlogo2();
        guardarlogo();
        extraerId();

        ibCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarControl();
                finish();
            }
        });
        ibInsertarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etinforme.getText().toString().isEmpty()) {
                    AlertaCampoVacio();
                } else {
                    String informe = etinforme.getText().toString();
                    confirmar2(informe);
                    //cargarInforme();
                }

            }
        });
        ibFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });

      /*  btnInsertarInforme.setOnClickListener(new View.OnClickListener() {
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
        });*/

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

    private void guardar(Bitmap bitmap){
        /*
         * Save bitmap to ExternalStorageDirectory
         */

        //get bitmap from ImageVIew
        //not always valid, depends on your drawable
        // Bitmap bitmap = ((BitmapDrawable)ivFotoRegistro.getDrawable()).getBitmap();

        //always save as
        String fileName = "holamundo.jpg";

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        path2 =Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + fileName ;
        // File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(path2);

        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());

           /* ContentResolver cr = getContentResolver();
            String imagePath = file.getAbsolutePath();
            String name = file.getName();
            String description = "My bitmap created by Android-er";
            String savedURL = MediaStore.Images.Media
                    .insertImage(cr, imagePath, name, description);*/

          /*  Toast.makeText(this,
                    path,
                    Toast.LENGTH_LONG).show();
*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
    private void guardarlogo2(){

        Bitmap bitMap = BitmapFactory.decodeResource(getResources(),R.drawable.security);
        bitMap = redimensionarLogo(bitMap,150,136);
        //always save as
        String fileName = "logosecurity.jpg";

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        path4 =Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + fileName ;
        // File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(path4);

        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
    private void guardarlogo(){

        Bitmap bitMap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        bitMap = redimensionarLogo(bitMap,322,107);
        //always save as
        String fileName = "logo.jpg";

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        path3 =Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + fileName ;
        // File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(path3);

        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

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
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        //progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        //String url = ip + "/security/extraerControlMayorId.php";
        String url = ip + "/security/extraerEmail.php";
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Control control = null;
                Lugar lugar = null;
                Guardia guardia = null;
                JSONArray json = response.optJSONArray("datos");
                try {
                    control = new Control();
                    lugar = new Lugar();
                    guardia = new Guardia();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(0);
                    control.setIdControles(jsonObject.optInt("idControles"));
                    guardia.setIdPersona(jsonObject.optInt("idGuardia"));
                    lugar.setIdLugares(jsonObject.getInt("idLugares"));
                    control.setLatitud(jsonObject.getString("latitud"));
                    control.setLongitud(jsonObject.getString("longitud"));
                    lugar.setEmails(jsonObject.getString("emails"));
                    control.setLugar(lugar);
                    control.setGuardia(guardia);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                idControl = control.getIdControles();
                email = lugar.getEmails();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void eliminarControl() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/eliminarControl.php?idControles=" + idControl;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        });

        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    //subir imagen
    public void cargarInforme() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip + "/security/insertarInforme.php?";
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                to = email;
             //   subject = etTituloInforme.getText().toString();
                body = etinforme.getText().toString();
                EmailSender emailSender = new EmailSender();
                emailSender.execute(usuarioEmail, contraseñaEmail, to, "Informe", body,path2, path3, path4);
                //progressDialog = ProgressDialog.show(context, "", "Cargando...", true);
                if (response.trim().equalsIgnoreCase("registra")) {
//                    etTituloInforme.setText("");
                    etinforme.setText("");
                    bitmap = null;
                    imageView.setImageResource(R.drawable.img_base);
                    //progressDialog.hide();
                    // btnInsertarInforme.setText("Enviar Otro informe");
                    Toast.makeText(context, "Se registró correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    AlertaError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertaError();

            }
        }) {
            //enviar datos mediante post
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String idinforme = "a";
                String idControles = String.valueOf(idControl);
                String tituloInforme = "Informe";
                String informe = etinforme.getText().toString();
                // String fecha_hora="2018-03-31 01:41:48"
                String imagenInforme = null;
                if (bitmap == null) {
                    Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.img_base);
                    bitmap2 = redimensionarLogo(bitmap2,600,600);
                    imagenInforme = convertirImgString(bitmap2);
                } else {
                    imagenInforme = convertirImgString(bitmap);
                }


                Map<String, String> parametros = new HashMap<>();
                parametros.put("idinforme", idinforme);
                parametros.put("idControles", idControles);
                parametros.put("informe", informe);
                parametros.put("tituloInforme", tituloInforme);
                //  parametros.put("fecha_hora",fecha_hora);
                parametros.put("imagenInforme", imagenInforme);

                return parametros;


            }
        };
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
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
                matrix.setRotate(0);

                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(0);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(0);
                break;
            default:
        }
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotateBitmap);
    }


    private void AlertaError() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Error al registrar, comprueba tu conexión.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
    private void AlertaCampoVacio() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Debe escribir un informe.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    private void confirmar(String informe) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("¿Confirmar Informe?");
        builder.setCancelable(false);
        builder.setMessage(""+ informe);
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarInforme();
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void confirmar2(String informe) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.dialog, null);
        ImageView imageView = view.findViewById(R.id.imagendialog);
        imageView.setImageBitmap(bitmap);
        builder.setView(view);
        builder.setTitle("¿Confirmar Informe?");
        builder.setCancelable(false);
        builder.setMessage(""+ informe);
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarInforme();
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    class EmailSender extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... data) {
            String emailSenderAddress = (String) data[0];
            String emailSenderPassword = (String) data[1];
            String recipients = (String) data[2];
            String subject = (String) data[3];
            String comments = (String) data[4];
            String pictureFileName = (String) data[5];
            String pictureFileName2 = (String) data[6];
            String pictureFileName3 = (String) data[7];

            Email m = new Email(emailSenderAddress, emailSenderPassword);

            m.setTo(recipients);
            m.setFrom(emailSenderAddress);
            m.setSubject(subject);
            m.setBody(comments);

            try {
                ///m.addAttachment("/sdcard/filelocation");
                m.setPictureFileName(pictureFileName);
                m.set_fileName2(pictureFileName2);
                m.set_fileName3(pictureFileName3);

                return m.send();
            } catch (Exception e) {
                //Toast.makeText(MainActivity.this, "There was a problem sending the email." + e.getMessage(), Toast.LENGTH_LONG).show();
                //throw  new Exception("Error sending ")
                throw new RuntimeException("Bang");
                //throw new RuntimeException(e);


            }


        }

        @Override
        protected void onPostExecute(Boolean result) {

            progressDialog.dismiss();
        }
    }
}
