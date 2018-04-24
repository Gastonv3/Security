package com.v3.security;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.v3.security.Clases.Lugar;
import com.v3.security.Util.Preferencias;
import com.v3.security.Util.VolleySingleton;

import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ControlActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private Location lastLocation;

    private Marker currentLocationMarker;
    private GoogleApiClient googleClient;

    ////
    MapView mapView;
    GoogleMap mMap;
    EditText idGuardia;
    //EditText coordenadas;
    //EditText Estado;
    Button btnPolicia;
    Button btninsertar;
    Button btninforme;
    ProgressDialog progressDialog;
    //  RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String latitud, longitud;
    int idlugar, Estado, idguardia;
    //Double latitud, longitud;

    Context context;
    ImageView imageView;
    TextView tvNombreLugar;

    /*
        @Override
        protected void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mapView.onPause();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);
        context = this;
        tvNombreLugar = findViewById(R.id.tvNombreLugar);
        imageView = findViewById(R.id.ivControl);
        // mapView = findViewById(R.id.mapView);
        //mapView.onCreate(savedInstanceState);
        btnPolicia = findViewById(R.id.btnPolicia);
        btninsertar = findViewById(R.id.btnInsertar);
        btninforme = findViewById(R.id.btnInforme);
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
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                // Called when a new location is found by the network location provider.
            /*    lastLocation = location;

                if (currentLocationMarker != null) {
                    currentLocationMarker.remove();
                }

                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latlng);
                markerOptions.title("Ni ubicación");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                currentLocationMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                //   mMap.animateCamera(CameraUpdateFactory.zoomBy(00));
                mMap.setMinZoomPreference(15);
                mMap.setMaxZoomPreference(15);

               /* latitud = location.getLatitude();
                longitud = location.getLongitude();*/
                latitud = ("" + location.getLatitude());
                longitud = ("" + location.getLongitude());
                progressDialog.hide();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        //  permissionCheck = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        // permissionCheck2 = ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION);
// Register the listener with the Location Manager to receive location updates
        // para que el proveedor sea internet LocationManager.NETWORK_PROVIDER,
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        btnPolicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:*555"));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                finish();
                startActivity(intent);

            }
        });
        btninsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebservice();
                //  finish();
            }
        });
        btninforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebservice2();
                Intent intent = new Intent(context, InformesActivity.class);
                startActivity(intent);
            }
        });
        // mapView.getMapAsync(this);
    }

    public void cargarWebservice() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        String ip = getString(R.string.ip_bd);
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = ip + "/security/insertarControl.php?idGuardia=" + idguardia + "&idLugares=" + idlugar + "&latitud=" + latitud +
                "&longitud=" + longitud;
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        AlertaError();
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.hide();

        Toast.makeText(getApplicationContext(), "Se registró correctamente", Toast.LENGTH_SHORT).show();

    }


   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }*/
   private void AlertaError() {
       android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);
       builder.setTitle("Error");
       builder.setMessage("Error al registrar, comprueba tu conexión");
       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });

       builder.show();
   }
    public void cargarWebservice2() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        String ip = getString(R.string.ip_bd);
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = ip + "/security/insertarControl.php?idGuardia=" + idguardia + "&idLugares=" + idlugar + "&latitud=" + latitud +
                "&longitud=" + longitud;
        //lee y procesa la informacion (Realiza el llamado a la url e intenta conectarse al webservis
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });
        //permite establecer la cominicacion con los metodos response o error
        // request.add(jsonObjectRequest);
        VolleySingleton.getInstanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}