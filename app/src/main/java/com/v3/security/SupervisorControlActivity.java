package com.v3.security;

import android.location.Location;
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
import com.v3.security.Clases.Control2;
import com.v3.security.R;

import java.util.ResourceBundle;

public class SupervisorControlActivity extends AppCompatActivity implements OnMapReadyCallback{
    TextView nombreguarda, nombrelugar, fechacontrol;
    private Location lastLocation;

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
        nombreguarda = findViewById(R.id.nombreGuardia);
        nombrelugar = findViewById(R.id.nombreLugar);
        fechacontrol = findViewById(R.id.fecha);
        Bundle bundle = getIntent().getExtras();
        Control2 control2 = null;
        if (bundle != null) {
            control2 = (Control2) bundle.getSerializable("control");
            nombreguarda.setText("Guardia: " + (control2.getGuardia().getNombre()) + " " + (control2.getGuardia().getApellido()));
            nombrelugar.setText("Lugar: " + (control2.getLugar().getNombre_lugares()));
            fechacontrol.setText("Fecha y Hora: " + (control2.getFechaHora()));
            Double latitud = Double.valueOf(control2.getLatitud());
            Double longitud = Double.valueOf(control2.getLongitud());


           if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            LatLng latlng = new LatLng(latitud, longitud);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            markerOptions.title("Ni ubicación");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            currentLocationMarker = mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            //   mMap.animateCamera(CameraUpdateFactory.zoomBy(00));
            mMap.setMinZoomPreference(15);
            mMap.setMaxZoomPreference(15);
            mapView.getMapAsync( this);
        }

        }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}

