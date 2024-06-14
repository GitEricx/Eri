package com.example.digitalPocket.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.digitalPocket.R;
import com.example.digitalPocket.adapter.UserBalanceAdapter;
import com.example.digitalPocket.data.model.BalanceMap;
import com.example.digitalPocket.dao.UserBalanceLoader;
import com.example.digitalPocket.dao.UserDao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    private final Context context;
    private final MutableLiveData<String> mText;
    private final MutableLiveData<Map<String, BigDecimal>> userBalanceLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        mText = new MutableLiveData<>();
        String loggedUserEmail = getUserCredentials();
        mText.setValue(loggedUserEmail);
        errorMessageLiveData = new MutableLiveData<>();
        loadUserBalance();
    }

    private void loadUserBalance() {
        new BalanceMap().getBalanceMap(new BalanceMap.BalanceMapCallback() {
            @Override
            public void onBalanceMapLoaded(Map<String, BigDecimal> balanceMap) {
                userBalanceLiveData.postValue(balanceMap);
            }

            @Override
            public void onLoadFailed(String message) {
                errorMessageLiveData.postValue(message);
            }
        });
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public LiveData<Map<String, BigDecimal>> getUserBalanceLiveData() {
        return userBalanceLiveData;
    }

    public void filter(String text, ListView listView) {
        Map<String, BigDecimal> filteredList = new HashMap<>();
        Map<String, BigDecimal> balanceMap = userBalanceLiveData.getValue();

        if (balanceMap != null) {
            for (Map.Entry<String, BigDecimal> entry : balanceMap.entrySet()) {
                String cryptoName = entry.getKey();
                BigDecimal balance = entry.getValue();
                if (cryptoName.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.put(cryptoName, balance);
                }
            }
        }

        adapterSet(listView, R.layout.balance_item, filteredList, true);
    }

    public void adapterSet(ListView container, int layoutItem, Map<String, BigDecimal> balanceMap, boolean displayZero) {
        if (balanceMap != null) {
            UserBalanceAdapter adapter = new UserBalanceAdapter(context, layoutItem, balanceMap, displayZero);
            container.setAdapter(adapter);
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    private String getUserCredentials() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DigitalPursePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }
}
