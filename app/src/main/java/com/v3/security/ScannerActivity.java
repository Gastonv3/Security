package com.v3.security;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.v3.security.Util.Preferencias;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler{
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true);

        } else {
            mFlash = false;
            mAutoFocus = true;

        }


        setContentView(R.layout.activity_scanner);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewGroup contentFrame =  findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);

        contentFrame.addView(mScannerView);
    }

    @Override
    public void handleResult(Result result) {
         String code = result.getContents();
       // final  String format = result.getBarcodeFormat().getName();
        //String fullMessage = "Contents = "+code+", Format = "+format;
      /*  //Bundle bundle = new Bundle();

        Intent intent = new Intent(Scanner.this, AutorizadoFragment.class);
     //   bundle.putString("qr", fullMessage);
        intent.putExtra("scan", fullMessage);
        startActivity(intent);*/
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("hola");
        builder.setMessage(fullMessage);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

        /*Intent intent = new Intent();
        intent.putExtra("barcode", fullMessage);
        setResult(RESULT_OK, intent);*/
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",code);
        setResult(Activity.RESULT_OK,returnIntent); finish();
        // mScannerView.resumeCameraPreview(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.flash:
                mFlash = !mFlash;
                if(mFlash) {
                  //  item.setTitle(R.string.flash_on);
                } else {
                  //  item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();


    }
}
