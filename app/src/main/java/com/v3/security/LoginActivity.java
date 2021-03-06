package com.v3.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.v3.security.Clases.Guardia;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class LoginActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{
    EditText etlogin;
    EditText etpass;
    Button btnentrar;
    ProgressDialog progressDialog;
    //RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    Context context;
    CheckBox cbkrecuerdame;
    int idguardia;
    String tipouser;
    boolean estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        referencias();
        validaPermisos();

        estado = Preferencias.getBoolean(context,Preferencias.getKeyRecuerdame(),false);
        if (estado == true){
            cbkrecuerdame.setChecked(estado);
            recordar();
        }
        cbkrecuerdame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!cbkrecuerdame.isChecked()){
                    Preferencias.setBoolean(context, Preferencias.getKeyRecuerdame(), false);
                }
            }
        });
        btnentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etlogin.getText().toString().isEmpty()){
                    AlertaCampoVacioUser();
                }else if (etpass.getText().toString().isEmpty()){
                    AlertaCampoVacioPass();
                }else {
                    cargarWebServis();
                }


            }
        });
    }

    private void referencias() {
        cbkrecuerdame = findViewById(R.id.cbRecuerdame);
        etlogin = findViewById(R.id.etUsuario);
        etpass = findViewById(R.id.etpass);
        btnentrar = findViewById(R.id.btnEntrar);
    }

    private void cargarWebServis() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String ip = getString(R.string.ip_bd);
        String url = ip+"/security/login.php?user=" + etlogin.getText() + "&pass=" + etpass.getText();
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        AlertaError();

    }

    @Override
    public void onResponse(JSONObject response) {
        Guardia guardia = null;
        JSONArray json = response.optJSONArray("datos");
        try {
            guardia = new Guardia();
            JSONObject jsonObject = null;
            jsonObject = json.getJSONObject(0);
            guardia.setIdPersona(jsonObject.optInt("idPersona"));
            guardia.setTipoUsuario(jsonObject.optString("tipoUsuario"));
            guardia.setLogin(jsonObject.getString("user"));
            guardia.setPassword(jsonObject.getString("pass"));
            guardia.setEstado(jsonObject.getString("estado"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        idguardia = guardia.getIdPersona();
        tipouser = guardia.getTipoUsuario();
        String estado = guardia.getEstado();
        Preferencias.setInteger(context, Preferencias.getKeyGuardia(), idguardia);
        Preferencias.setString(context, Preferencias.getKeyUser(), guardia.getLogin());
        Preferencias.setString(context, Preferencias.getKeyPass(), guardia.getPassword());
        Preferencias.setBoolean(context, Preferencias.getKeyRecuerdame(), cbkrecuerdame.isChecked());

        progressDialog.hide();
        if (estado.equals("0")){
            AlertaCuentaBloqueada();
        }
        else if (tipouser.equals("guardia")){
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);

        }else {
            Intent intent3 = new Intent(this, SupervisorActivity.class);
            startActivity(intent3);

        }

    }

    public void recordar() {
        if (Preferencias.getBoolean(context, Preferencias.getKeyRecuerdame(), true)) {
            etlogin.setText(Preferencias.getString(context, Preferencias.getKeyUser()));
            etpass.setText(Preferencias.getString(context, Preferencias.getKeyPass()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED&&
                        grantResults[2]==PackageManager.PERMISSION_GRANTED&&grantResults[3]==PackageManager.PERMISSION_GRANTED&&grantResults[4]==PackageManager.PERMISSION_GRANTED){

                }else {
                    cargarDialogoRecomendacion();
                }
            } break;

        }

    }
    private void cargarDialogoRecomendacion() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(LoginActivity.this);
       dialog.setCancelable(false);
        dialog.setTitle("Permiso Desactivado");
        dialog.setMessage("Debe aceptar el permiso.");
        dialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{ACCESS_FINE_LOCATION,WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_COARSE_LOCATION,CALL_PHONE}, 1);
                }
            }
        });
        dialog.show();
    }
    private boolean validaPermisos() {
//si buld.version.sdk_int es menor a build.VERSION_CODES.M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;

        }//en caso de que sea falso pregunto si los permisos estan activos
        //checkeo si los permisos estan aceptados
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)&& (checkSelfPermission(CALL_PHONE)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        //en caso de que la version no sea menor y los permisos no se hayan dado debe solicitar los permisos
        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(CALL_PHONE))||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||(shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))||(shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION))) {
            cargarDialogoRecomendacion();
        }//solicita los permisos
        else {

            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION,CALL_PHONE}, 1);

        }
        return false;
    }
    private void AlertaError (){
         AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogCustom);
         builder.setTitle("Error");
         builder.setMessage("Comprueba tu cuenta y/o conexión.");
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         });
         builder.show();
    }
    private void AlertaCampoVacioUser (){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Debe ingresas un usuario.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaCampoVacioPass (){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Debe ingresas una contraseña.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void AlertaCuentaBloqueada (){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogCustom);
        builder.setTitle("Error");
        builder.setMessage("Usuario bloqueado, contacte al administrador.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}
