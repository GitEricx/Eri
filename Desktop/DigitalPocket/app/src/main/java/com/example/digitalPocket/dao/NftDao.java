package com.example.digitalPocket.dao;

import androidx.annotation.NonNull;

import com.example.digitalPocket.Config;
import com.example.digitalPocket.data.model.NFT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NftDao {
    private OkHttpClient httpClient;
    private static NftDao instance;

    public static List<NFT> allNFT;

    // 私有构造函数，防止实例化
    private NftDao() {
        this.httpClient = new OkHttpClient();
    }

    private String ip= Config.IP;

    // 获取 NftDao 的单例实例的方法
    public static synchronized NftDao getInstance() {
        if (instance == null) {
            instance = new NftDao();
        }
        return instance;
    }

    public void getNFT(@NonNull String owner, int signNum, final NftCallback<List<NFT>> callback) {
        String url;
        if(owner==null){
            url = ip+"/NFT/getNFT?state="+signNum;
        }else{
            url = ip+"/NFT/getNFT?state="+signNum+"&address="+owner;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    List<NFT> nftList = parseNFTListJson(responseData);
                    callback.onSuccess(nftList);
                } else {
                    callback.onError("获取 NFT 失败");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }




    public void buyNFT(String soldAddress,String buyerAddress,int nftId,BigDecimal number,final NftCallback<String> callback) {
        String url = ip+"/NFT/swapNFT?solderAddress="+soldAddress+"&buyerAddress="+buyerAddress+"&nftId="+nftId+"&number="+number;
//        FormBody formBody = new FormBody.Builder()
//                .add("solderAddress",soldAddress)
//                .add("buyerAddress",buyerAddress)
//                .add("nftId", String.valueOf(nftId))
//                .add("number", String.valueOf(number)).build();
        Request request = new Request.Builder()
                .url(url)
//                .post(formBody)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                } else {
                    String errorMessage = response.body().string();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void updateNFTonSell(@NonNull int nftId, BigDecimal price,final NftCallback<String> callback) {
        String url= ip+"/NFT/updateNFTonSell?"+"nftId="+nftId+"&price="+price;
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                } else {
                    String errorMessage = response.body().string();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }


    // 解析 NFT 列表的 JSON 数据
    private List<NFT> parseNFTListJson(String jsonData) {
        Gson gson = new Gson();
        Type nftListType = new TypeToken<List<NFT>>() {}.getType();
        return gson.fromJson(jsonData, nftListType);
    }

    public interface NftCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}
