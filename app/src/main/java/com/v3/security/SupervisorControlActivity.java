package com.v3.security;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.v3.security.Clases.Control;
import com.v3.security.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class SupervisorControlActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView nombreguarda, nombrelugar, fechacontrol;
    private Location lastLocation;
    Double latitud;
    Double longitud;

    private Marker currentLocationMarker;
    private GoogleApiClient googleClient;

    ////
    MapView mapView;
    GoogleMap mMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_control);
        mapView = findViewById(R.id.map22);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        nombreguarda = findViewById(R.id.nombreGuardia);
        nombrelugar = findViewById(R.id.nombreLugar);
        fechacontrol = findViewById(R.id.fecha);
        Bundle bundle = getIntent().getExtras();
        Control control2 = null;
        if (bundle != null) {
            control2 = (Control) bundle.getSerializable("control");
            nombreguarda.setText("Guardia: " + (control2.getGuardia().getNombre()) + " " + (control2.getGuardia().getApellido()));
            nombrelugar.setText("Lugar: " + (control2.getLugar().getNombre_lugares()));
            String fechastring2 = control2.getFechaHora();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date a = sdf.parse(fechastring2);
                SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String b = fmtOut.format(a);
                fechacontrol.setText("Fecha y Hora: " + b);
                latitud = Double.valueOf(control2.getLatitud());
                longitud = Double.valueOf(control2.getLongitud());

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(latitud, longitud);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(sydney);
        markerOptions.title("Ubicación");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMinZoomPreference(18);
        mMap.setMaxZoomPreference(18);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
