package com.example.digitalPocket.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitalPocket.ui.login.LoggedUser;
import com.example.digitalPocket.util.IconManager;
import com.example.digitalPocket.R;
import com.example.digitalPocket.data.model.BalanceMap;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.ui.Dialog.ReceiveDialog;
import com.example.digitalPocket.ui.Dialog.SendDialog;

import java.math.BigDecimal;
import java.util.Map;

public class SwapActivity extends AppCompatActivity {
    private String cryptoName;
    private String cryptoBalance;
    private TextView cryptoNameView;
    private ImageView cryptoNameIconView;
    private ImageView quitView;
    private TextView cryptoBalanceView;
    private TextView addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);
        cryptoName = getIntent().getStringExtra("cryptoName");
        cryptoBalance = getIntent().getStringExtra("balance").trim();
        cryptoNameView = findViewById(R.id.swapCrytoNameView);
        cryptoNameIconView = findViewById(R.id.cryNameIconView);
        quitView = findViewById(R.id.quitView);
        cryptoBalanceView = findViewById(R.id.exchangBalanceView);
        addressView = findViewById(R.id.addressTextView);
        cryptoNameView.setText(cryptoName);
        IconManager iconManager = new IconManager();
        cryptoNameIconView.setImageResource(iconManager.getIconResId(cryptoName));
        quitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String address = LoggedUser.loggedUserAddress;
        addressView.setText(address);
        ImageButton receiveButton = findViewById(R.id.reciveButton);
        ReceiveDialog receiveDialog = new ReceiveDialog(this);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveDialog.setContentView(R.layout.dialog_receive);
                receiveDialog.setAddress(address);
                receiveDialog.show();
            }
        });
        Button copyButton = findViewById(R.id.copyButt);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", address);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SwapActivity.this, "Address copied successfully", Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDialog sendDialog = new SendDialog(view.getContext());
                sendDialog.setContentView(R.layout.send_dialog);
                sendDialog.setCrypto(cryptoName);
                sendDialog.setCryptoBalance(new BigDecimal(cryptoBalance));
                sendDialog.show();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateCryptoBalance();
        }
    }

    private void updateCryptoBalance() {
        SharedPreferences sharedPreferences = getSharedPreferences("DigitalPursePreferences", Context.MODE_PRIVATE);
        String address=sharedPreferences.getString("username", null);
        new BalanceMap().getBalanceMap(new BalanceMap.BalanceMapCallback() {
            @Override
            public void onBalanceMapLoaded(Map<String, BigDecimal> balanceMap) {
                BigDecimal balance = balanceMap.get(cryptoName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (balance != null) {
                            cryptoBalanceView.setText(balance.toString());
                        } else {
                            cryptoBalanceView.setText("0");
                        }
                    }
                });
            }

            @Override
            public void onLoadFailed(String message) {
                // Handle the case when loading the balance map fails
            }
        });
    }
}
