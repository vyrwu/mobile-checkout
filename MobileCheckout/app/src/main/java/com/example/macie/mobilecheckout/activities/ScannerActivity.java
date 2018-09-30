package com.example.macie.mobilecheckout.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.example.macie.mobilecheckout.R;
import com.example.macie.mobilecheckout.fragments.PopupFragment;
import com.example.macie.mobilecheckout.program_logic.Product;
import com.example.macie.mobilecheckout.program_logic.ProductFactory;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Scanner activity containing primary logic for Scan Barcode functionality. Contains
 * SurfaceView on which camera preview is being displayed, and a button launching VirtualBasketActivity.
 */
public class ScannerActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private BroadcastReceiver productReceiver, popupCloseReceiver;
    private ImageButton goToBasketButton;
    /**
     * Request code used in receiving camera permission.
     */
    private static int MY_REQUEST_CODE = 0;
    /**
     * Amount of miliseconds application should wait before it repeat scanning.
     */
    private static final int DECODER_SLEEP_TIME = 1500;

    /**
     * Sets up the window change animation style, binds fields to layouts and adds
     * OnClickListener to button goToBasketButton --> starts BasketActivity.
     * Instantiates BarcodeDetector object. Adds Callback to cameraPreview in order to
     * request users permission for utilizing mobile camera on SurfaceView creation. Invokes
     * first barcodeDetection on creation of this activity.
     * Registers two BroadcastReceiver objects listening for intents:
     *
     * 1. Product Information - intent containing Product object created in ProductFactory. Shows PopupFragment
     * supplied with Product object on receive.
     *
     * 2. Close Popup - intent indicating destruction of PopupFragment.
     * Repeats barcode detection after DECODER_SLEEP_TIME (THIS SOLUTION CAUSES MEMORY LEAKS, BECAUSE IT
     * GETS ACCESS TO OUTER CLASS FIELDS INSIDE NON-STATIC ANONYMOUS CLASS. SHOULD USE CUSTOM HOLDER CLASS).
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.Fade;
        setContentView(R.layout.activity_scanner);

        cameraPreview = (SurfaceView)findViewById(R.id.camera_preview);
        goToBasketButton = (ImageButton)findViewById(R.id.scanner_go_to_basket_button);


        goToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VirtualBasketActivity.class);
                startActivity(intent);
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(this).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                requestCameraPermission();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        productReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    showDialog((Product) intent.getParcelableExtra("product"));
            }
        };
        registerReceiver(productReceiver, new IntentFilter("Product Information"));


        popupCloseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        barcodeDetection();
                    }
                }, DECODER_SLEEP_TIME);
            }
        };
        registerReceiver(popupCloseReceiver, new IntentFilter("Close Popup"));

        barcodeDetection();
    }

    /**
     * Unregisters registered BroadcastReceiver instances. Releases camerSource and barcodeDetector resources.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(productReceiver);
        unregisterReceiver(popupCloseReceiver);
        barcodeDetector.release();
        cameraSource.release();
    }

    /**
     * Invokes createCameraSource() on permission grant. In case of permission denial, repeats
     * asking for the permission by invoking requestCameraPermission.
     * @param requestCode used to recognise permission call
     * @param permission
     * @param grantResult
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                createCameraSource();
            } else {
                requestCameraPermission();
            }
        }
    }

    /**
     * Creates and setups camera source, supplying it with Detector instance. Starts cameraSource
     * only if user permission to use camera was granted.
     */
    private void createCameraSource() {
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();
        try {
            if (ActivityCompat.checkSelfPermission(ScannerActivity.this,
                    android.Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                // if Permission denied
                finish();
            }
            cameraSource.start(cameraPreview.getHolder());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Supplies barcodeDetector with Processor<Barcode>() instance. On detection, Processor disconnects
     * barcode detection from the cameraPreview and extracts first detection result returned by barcodeDetector
     * as String. Then invokes ProductFactory.downloadProductInformation method passing its Context and barcode
     * as String object.
     */
    private void barcodeDetection() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size()>0){
                    barcodeDetector.release();
                    Barcode barcode = barcodes.valueAt(0);
                    ProductFactory.downloadProductInformation(getApplicationContext(), barcode.displayValue);

                }
            }
        });
    }

    /**
     * Requests camera permission from the system.
     */
    private void requestCameraPermission() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_REQUEST_CODE);
    }

    /**
     * Creates DialogFragment object and sets it to a new instance of PopupFragment class, which
     * is supplied with a Product object. Displays Dialog on the screen.
     * @param product
     */
    public void showDialog(Product product) {
        DialogFragment newFragment = PopupFragment.newInstance(R.string.app_name, product);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        newFragment.show(ft, "dialog");
    }
}
