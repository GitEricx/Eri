package com.example.digitalPocket.data.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.digitalPocket.dao.UserBalanceLoader;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.ui.login.LoggedUser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceMap {
    private Map<String, BigDecimal> balanceMap = new HashMap<>();

    public interface BalanceMapCallback {
        void onBalanceMapLoaded(Map<String, BigDecimal> balanceMap);
        void onLoadFailed(String message);
    }

    public void getBalanceMap(BalanceMapCallback callback) {
        UserBalanceLoader.getInstance().loadUserBalance(LoggedUser.loggedUserEmail, new UserBalanceLoader.UserBalanceCallback() {
            @Override
            public void onUserBalanceLoaded(List<User> users) {
                for (User user : users) {
                    balanceMap.put(user.getCryptoName(), user.getBalance());
                }
                callback.onBalanceMapLoaded(balanceMap);
            }

            @Override
            public void onLoadFailed(String message) {
                callback.onLoadFailed(message);
            }
        });
    }

}
