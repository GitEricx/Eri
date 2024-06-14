package com.example.digitalPocket.ui.market;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.digitalPocket.dao.NftDao;
import com.example.digitalPocket.data.model.NFT;

import java.util.List;

public class MarketViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<NFT>> nftList;

    public MarketViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");

        // 初始化 nftList
        nftList = new MutableLiveData<>();
        loadNftList();
    }

    public void loadNftList(){
        NftDao.getInstance().getNFT(null,1,new NftDao.NftCallback<List<NFT>>() {
            @Override
            public void onSuccess(List<NFT> data) {
                nftList.postValue(data);
            }

            @Override
            public void onError(String message) {
                // 处理错误，可以选择设置一个空列表或者显示错误信息
                nftList.postValue(null);
            }
        });
    }

    public MutableLiveData<List<NFT>> getNFTList() {
        return nftList;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
