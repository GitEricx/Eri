package com.example.digitalPocket.ui.deal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.util.BarcodeScanner;
import com.example.digitalPocket.util.EthQRCodeGenerator;
import com.example.digitalPocket.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.WriterException;

import java.math.BigDecimal;
import java.util.Map;

public class DealFragment extends Fragment {

    private DealViewModel dealViewModel;
    private ImageView dealScanView;
    private BarcodeScanner barcodeScanner;
    private Button sendLayoutButt;
    private Button receiveLayoutButt;
    private View sendLayout;
    private View recevieLayout;
    private ImageView addressQRview;
    private TextView addressTextView;
    private EditText toaddressView;
    private EditText sendNumberView;
    private Spinner cryptoSpinner;
    private TextView balanceView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeScanner = new BarcodeScanner(getActivity(), new BarcodeScanner.ScanResultCallback() {
            @Override
            public void onScanResult(String result) {
                toaddressView.setText(result);
            }

            @Override
            public void onScanFailed(String message) {
                Snackbar.make(getView(), "Scan failed: " + message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dealViewModel = new ViewModelProvider(this).get(DealViewModel.class);
        View root = inflater.inflate(R.layout.fragment_deal, container, false);

        TextView saddressView = root.findViewById(R.id.dealAddressview);
        balanceView = root.findViewById(R.id.dealBalanceView);
        toaddressView = root.findViewById(R.id.toAddress);
        sendNumberView = root.findViewById(R.id.sendNumber);
        cryptoSpinner = root.findViewById(R.id.cryptoSpinner);

        dealViewModel.getAddress().observe(getViewLifecycleOwner(), saddressView::setText);
        dealViewModel.getBalance().observe(getViewLifecycleOwner(), balanceView::setText);
        dealViewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

        dealViewModel.getUserBalanceLiveData().observe(getViewLifecycleOwner(), balanceMap -> {
            cryptoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    String selectedCryptoName = adapterView.getItemAtPosition(position).toString();
                    dealViewModel.updateBalance(selectedCryptoName, balanceMap);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    dealViewModel.updateBalance("", balanceMap);
                }
            });
        });

        Button sendButt = root.findViewById(R.id.dealSendbutton);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toAddress = toaddressView.getText().toString();
                String sendNumber = sendNumberView.getText().toString();
                String cryptoType = cryptoSpinner.getSelectedItem().toString();

                if (toAddress.isEmpty() || sendNumber.isEmpty()) {
                    Snackbar.make(getView(), "请输入发送地址和数量", Snackbar.LENGTH_SHORT).show();
                } else {
                    dealViewModel.sendCrypto(toAddress, sendNumber, cryptoType, new UserDao.UserCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Snackbar.make(getView(), "发送成功", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(getView(), "发送失败: " + message, Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });

        dealScanView = root.findViewById(R.id.dealScanView);
        dealScanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeScanner.startScan();
            }
        });

        sendLayoutButt = root.findViewById(R.id.sendLayoutButton);
        receiveLayoutButt = root.findViewById(R.id.receiveLayoutButton);
        sendLayout = root.findViewById(R.id.sendLayout);
        recevieLayout = root.findViewById(R.id.receiveLayout);
        addressQRview = root.findViewById(R.id.QRimageView);
        addressTextView = root.findViewById(R.id.QRaddressView);

        sendLayoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLayoutButt.setTypeface(null, Typeface.BOLD);
                receiveLayoutButt.setTypeface(null, Typeface.NORMAL);
                sendLayout.setVisibility(View.VISIBLE);
                recevieLayout.setVisibility(View.GONE);
            }
        });

        receiveLayoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLayoutButt.setTypeface(null, Typeface.NORMAL);
                receiveLayoutButt.setTypeface(null, Typeface.BOLD);
                sendLayout.setVisibility(View.GONE);
                recevieLayout.setVisibility(View.VISIBLE);
                String address = dealViewModel.getAddress().getValue();
                addressTextView.setText(address);
                try {
                    Bitmap QRbitMap = EthQRCodeGenerator.generateQRCode(address, 200, 200);
                    addressQRview.setImageBitmap(QRbitMap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        barcodeScanner.handleActivityResult(requestCode, resultCode, data);
    }
}
