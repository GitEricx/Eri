package com.example.digitalPocket.util;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanner {

    public interface ScanResultCallback {
        void onScanResult(String result);
        void onScanFailed(String message);
    }

    private Activity activity;
    private ScanResultCallback callback;

    public BarcodeScanner(Activity activity, ScanResultCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                callback.onScanFailed("Cancelled");
            } else {
                callback.onScanResult(result.getContents());
            }
        } else {
            callback.onScanFailed("Failed to scan");
        }
    }
}
