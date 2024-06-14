package com.example.digitalPocket.ui.Dialog;

import androidx.annotation.NonNull;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitalPocket.ui.login.LoggedUser;
import com.example.digitalPocket.util.IconManager;
import com.example.digitalPocket.R;
import com.example.digitalPocket.dao.UserDao;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;

public class SendDialog extends BottomSheetDialog {
    private String address;
    public TextView addressView;
    public ImageView addressQRview;
    private String cryptoName;
    private BigDecimal cryptoBalance;
    public String result;
    public SendDialog(@NonNull Context context) {
        super(context);
    }
    public SendDialog(@NonNull Context context, int theme) {
        super(context,theme);
    }

    protected SendDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        result="";
        super.onCreate(savedInstanceState);
        TextView cryptoNameView=findViewById(R.id.cryptoNameView);
        ImageView cryptoImage=findViewById(R.id.sendDialogCryptoView);
        cryptoNameView.setText(cryptoName);
        IconManager iconManager=new IconManager();
        cryptoImage.setImageResource(iconManager.getIconResId(cryptoName));
        String saddress= LoggedUser.loggedUserAddress;
        TextView amountView=findViewById(R.id.amountView);
        TextView toaddressView=findViewById(R.id.swapToaddressView);
        Button sendButt=findViewById(R.id.sendbutton);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toaddressView.getText().toString()==null||amountView.getText().toString()==null||amountView.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "请输入地址和数量", Toast.LENGTH_SHORT).show();
                }else if(cryptoBalance.compareTo(new BigDecimal(amountView.getText().toString().trim()))<=0){
                    Toast.makeText(getContext(), "余额不足", Toast.LENGTH_SHORT).show();
                }else{
                    BigDecimal amount=new BigDecimal(amountView.getText().toString().trim());
                    String raddress=toaddressView.getText().toString().trim();
                    UserDao.getInstance().sendCrypto(saddress,cryptoName,amount,raddress, new UserDao.UserCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            result="发送成功";
                            dismiss();
                        }
                        @Override
                        public void onError(String message) {
                            result="发送失败"+message;
                            dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(result!=""){
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    public void setCrypto(String crytoName) {
        this.cryptoName=crytoName;
    }

    public void setCryptoBalance(BigDecimal cryptoBalance) {
        this.cryptoBalance = cryptoBalance;
    }
}