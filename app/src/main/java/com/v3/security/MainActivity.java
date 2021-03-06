package com.v3.security;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    private TextView mTextMessage;
    Camera camera;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.c, new LugarFragment()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.c, new RegistrosFragment()).commit();

                    return true;
                case R.id.personalAturizado:
                    transaction.replace(R.id.c, new AutorizadoFragment()).commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.c, new LugarFragment()).commit();
    }
 public  void apagarFlash(){
     camera = Camera.open();
     Camera.Parameters parameters = camera.getParameters();
     parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
     camera.setParameters(parameters);
     camera.startPreview();
 }
}
