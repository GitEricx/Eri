package com.example.digitalPocket.ui.Dialog;

import androidx.annotation.NonNull;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.digitalPocket.util.EthQRCodeGenerator;
import com.example.digitalPocket.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.WriterException;

public class ReceiveDialog extends BottomSheetDialog {
    private String address;
    public TextView addressView;
    public ImageView addressQRview;

    public ReceiveDialog(@NonNull Context context) {
        super(context);
    }
    public ReceiveDialog(@NonNull Context context, int theme) {
        super(context,theme);
    }

    protected ReceiveDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        addressView.setText(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    protected void onStart() {
        super.onStart();
        addressView = findViewById(R.id.addressView);
        if (addressView != null) {
            addressView.setText(address);
        }
        addressQRview=findViewById(R.id.addressQRview);
        Bitmap qrCodeBitmap = null;
        try {
            qrCodeBitmap = EthQRCodeGenerator.generateQRCode(address, 300, 300);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        addressQRview.setImageBitmap(qrCodeBitmap);

    }
}