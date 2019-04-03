package com.powerbtc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class BarCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause.
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

       // Toast.makeText(MainActivity.this, "" + rawResult.getText(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(MainActivity.this, "" + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        Log.e("Demo", rawResult.getText()); // Prints scan results
        rawResult.getText().toString();
        Log.e("Demo", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        mScannerView.resumeCameraPreview(this);
        if (!rawResult.getText().toString().equals(""))
        {
            Intent intent = new Intent();
            intent.putExtra("Barcode", rawResult.getText().toString());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        // If you would like to resume scanning, call this method below:
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("Barcode", "");
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}

