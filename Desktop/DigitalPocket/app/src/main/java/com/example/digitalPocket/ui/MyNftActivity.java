package com.example.digitalPocket.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitalPocket.R;
import com.example.digitalPocket.adapter.NFTAdapter;
import com.example.digitalPocket.dao.NftDao;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.data.model.NFT;
import com.example.digitalPocket.ui.login.LoggedUser;

import java.util.List;

public class MyNftActivity extends AppCompatActivity {
    private RecyclerView myNFTview;
    private ImageView backView;
    private TextView noNFTTextView;
    private NFTAdapter nftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_nft);

        myNFTview = findViewById(R.id.myNFTview);
        backView = findViewById(R.id.backView);
        noNFTTextView = findViewById(R.id.noNFTTextView);

        // 设置返回按钮的点击监听器
        backView.setOnClickListener(view -> finish());

        // 设置RecyclerView的布局管理器
        myNFTview.setLayoutManager(new GridLayoutManager(this, 2)); // 2列的网格布局

        // 获取当前用户地址
        String userAddress = LoggedUser.loggedUserAddress;

        // 加载用户的NFT数据
        NftDao.getInstance().getNFT(userAddress, 0, new NftDao.NftCallback<List<NFT>>() {
            @Override
            public void onSuccess(List<NFT> data) {
                // 在主线程上更新UI
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (data == null || data.isEmpty()) {
                        // 显示“您没有 NFT”的消息
                        noNFTTextView.setVisibility(View.VISIBLE);
                        myNFTview.setVisibility(View.GONE);
                    } else {
                        // 隐藏“您没有 NFT”的消息
                        noNFTTextView.setVisibility(View.GONE);
                        myNFTview.setVisibility(View.VISIBLE);

                        // 初始化Adapter并设置给RecyclerView
                        nftAdapter = new NFTAdapter(data);
                        myNFTview.setAdapter(nftAdapter);
                        nftAdapter.setOnItemClickListener(new NFTAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(NFT nft) {
                                Intent intent = new Intent(MyNftActivity.this, NftDetailActivity.class);
                                intent.putExtra("flag","sell");
                                intent.putExtra("nft", nft);
                                startActivityForResult(intent, 1);  // 启动 NftDetailActivity
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(String message) {
                // 在主线程上处理错误
                new Handler(Looper.getMainLooper()).post(() -> {
                    // 显示错误信息或进行其他错误处理
                    // 例如，可以显示一个Toast
                    Toast.makeText(MyNftActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}
