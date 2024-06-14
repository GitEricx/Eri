package com.example.digitalPocket.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.digitalPocket.R;
import com.example.digitalPocket.dao.NftDao;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.data.model.NFT;
import com.example.digitalPocket.ui.login.LoggedUser;
import com.example.digitalPocket.ui.market.MarketFragment;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.List;

public class NftDetailActivity extends AppCompatActivity {
    private TextView costView;
    private TextView nameView;
    private TextView ownerView;
    private TextView creatorView;
    private ImageView backView;
    private ImageView nftImageView;
    private Button collectButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nft_detail);
        costView = findViewById(R.id.detailCoastView);
        nameView = findViewById(R.id.nftNameView);
        ownerView = findViewById(R.id.detailOwnerView);
        creatorView = findViewById(R.id.detailCreatorView);
        backView = findViewById(R.id.backImageView);
        nftImageView = findViewById(R.id.nftDetaiImageView);
        collectButt = findViewById(R.id.collectButton);

        backView.setOnClickListener(view -> finish());

        String flag = getIntent().getStringExtra("flag");
        NFT nft = (NFT) getIntent().getSerializableExtra("nft");
        if (nft != null) {
            costView.setText(String.valueOf(nft.getCost()));
            nameView.setText("Name: " + nft.getName());
            ownerView.setText("Owner: " + nft.getOwner());
            creatorView.setText("Creator: " + nft.getCreator());
            Glide.with(nftImageView.getContext())
                    .load(nft.getData())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_dino)
                    .error(R.drawable.ic_mine)
                    .into(nftImageView);

            if ("buy".equals(flag)) {
                collectButt.setText("购买 - " + nft.getCost() + " ETH");
                collectButt.setOnClickListener(view -> showConfirmationDialog(nft));
            } else if ("sell".equals(flag)) {
                if(nft.getState()==-1){
                    collectButt.setText("上架");
                    collectButt.setOnClickListener(view -> showSellDialog(nft));
                }else{
                    collectButt.setText("上架中");
                    collectButt.setEnabled(false);
                }


            }
        }
    }

    private void showConfirmationDialog(NFT nft) {
        new AlertDialog.Builder(this)
                .setTitle("购买确认")
                .setMessage("您确定购买这个NFT花费 " + nft.getCost() + " ETH?")
                .setPositiveButton("是", (dialog, which) -> NftDao.getInstance().buyNFT(nft.getOwner(), LoggedUser.loggedUserAddress, nft.getId(), nft.getCost(), new NftDao.NftCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        runOnUiThread(this::showSuccessDialog);
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show());
                    }

                    private void showSuccessDialog() {
                        new AlertDialog.Builder(NftDetailActivity.this)
                                .setTitle("购买成功")
                                .setMessage("您已经成功购买这个NFT")
                                .setPositiveButton("OK", (dialog1, which1) -> {
                                    setResult(RESULT_OK);
                                    finish();
                                })
                                .show();
                    }
                }))
                .setNegativeButton("No", null)
                .show();
    }

    private void showSellDialog(NFT nft) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上架NFT");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String amountStr = input.getText().toString();
            if (!amountStr.isEmpty()) {
                BigDecimal amount = new BigDecimal(amountStr);
                if(amount.compareTo(BigDecimal.valueOf(0.0001))<0){
                    Snackbar.make(findViewById(android.R.id.content), "请输入有效价格.", Snackbar.LENGTH_LONG).show();
                }else{
                    NftDao.getInstance().updateNFTonSell(nft.getId(), amount, new NftDao.NftCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            runOnUiThread(() -> {
                                new AlertDialog.Builder(NftDetailActivity.this)
                                        .setTitle("上架成功")
                                        .setMessage("您成功上架该NFT!")
                                        .setPositiveButton("OK", (dialog1, which1) -> {
                                            setResult(RESULT_OK);
                                            finish();
                                        })
                                        .show();
                            });
                        }

                        @Override
                        public void onError(String message) {
                            runOnUiThread(() -> Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show());
                        }
                    });
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "请输入一个有效价格.", Snackbar.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
