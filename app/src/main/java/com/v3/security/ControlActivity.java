package com.v3.security;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.v3.security.Clases.Lugar;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONObject;

public class ControlActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    EditText idGuardia;
    EditText coordenadas;
    //EditText Estado;
    Button btninsertar;
    ProgressDialog progressDialog;
  //  RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    int idlugar, Estado, idguardia;
    Context context;
    ImageView imageView;
    TextView tvNombreLugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        context = this;
        tvNombreLugar = findViewById(R.id.tvNombreLugar);
        imageView = findViewById(R.id.ivControl);

        coordenadas = findViewById(R.id.edtCoordenadas);
        Estado = 1;
        btninsertar = findViewById(R.id.btnInsertar);

        idguardia = Preferencias.getInteger(context, Preferencias.getKeyGuardia());

        Bundle extras = getIntent().getBundleExtra("picture");
        byte[] b = extras.getByteArray("imagen");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView.setImageBitmap(bmp);
        idlugar = extras.getInt("idLugar");
        tvNombreLugar.setText(extras.getString("nombre"));
       // request = Volley.newRequestQueue(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //leer permiso y lo almacena en permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //si el permiso esta denegado lo solicitamos
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            //si requiere mostrar un mensaje antes de solicitar el permiso hacerlo dentro del siguiente if
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                /*progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setMessage("Cargando...");
                progressDialog.show();*/
                // Called when a new location is found by the network location provider.
                coordenadas.setText("" + location.getLatitude() + " " + location.getLongitude());
                progressDialog.hide();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        btninsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebservice();
            }
        });
    }

    public void cargarWebservice() {
        String url = "http://192.168.0.14/seguridad/insertarcontrol.php?idGuardia=" + idguardia + "&idLugares=" + idlugar + "&coordenadas=" + coordenadas.getText() +
                "&Estado=" + Estado;
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
       // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Se registro correctamente", Toast.LENGTH_SHORT).show();
    }
}
