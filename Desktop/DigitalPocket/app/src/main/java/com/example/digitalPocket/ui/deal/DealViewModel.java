package com.example.digitalPocket.ui.deal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.data.model.BalanceMap;
import com.example.digitalPocket.ui.login.LoggedUser;

import java.math.BigDecimal;
import java.util.Map;

public class DealViewModel extends ViewModel {
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<String> balance = new MutableLiveData<>();
    private final MutableLiveData<String> snackbarMessage = new MutableLiveData<>();
    private final MutableLiveData<Map<String, BigDecimal>> userBalanceLiveData = new MutableLiveData<>();
    private final String loggedUserAddress;

    public DealViewModel() {
        loggedUserAddress = LoggedUser.loggedUserAddress;
        if (loggedUserAddress != null) {
            address.postValue(loggedUserAddress);
            loadUserBalance();
        }
    }

    private void loadUserBalance() {
        new BalanceMap().getBalanceMap(new BalanceMap.BalanceMapCallback() {
            @Override
            public void onBalanceMapLoaded(Map<String, BigDecimal> balanceMap) {
                userBalanceLiveData.postValue(balanceMap);
            }

            @Override
            public void onLoadFailed(String message) {
                snackbarMessage.postValue("Failed to load balance: " + message);
            }
        });
    }

    public LiveData<String> getAddress() {
        return address;
    }

    public LiveData<String> getBalance() {
        return balance;
    }

    public LiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }

    public LiveData<Map<String, BigDecimal>> getUserBalanceLiveData() {
        return userBalanceLiveData;
    }

    public void updateBalance(String cryptoName, Map<String, BigDecimal> loggedUserBalance) {
        BigDecimal cryptoBalance = loggedUserBalance.get(cryptoName);
        if (cryptoBalance != null) {
            balance.postValue(cryptoBalance.toString());
        } else {
            balance.postValue("0");
        }
    }

    public void sendCrypto(String toAddress, String sendNumber, String cryptoName, UserDao.UserCallback<String> callback) {
        if (loggedUserAddress != null) {
            BigDecimal sendNumberValue;
            try {
                sendNumberValue = new BigDecimal(sendNumber);
            } catch (NumberFormatException e) {
                snackbarMessage.postValue("无效的发送数量");
                callback.onError("Invalid send number");
                return;
            }

            UserDao.getInstance().sendCrypto(loggedUserAddress, cryptoName, sendNumberValue, toAddress, new UserDao.UserCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    snackbarMessage.postValue("发送成功");
                    loadUserBalance();
                    callback.onSuccess(data);
                }

                @Override
                public void onError(String message) {
                    snackbarMessage.postValue("发送失败: " + message);
                    callback.onError(message);
                }
            });
        } else {
            snackbarMessage.postValue("用户地址无效");
            callback.onError("Invalid user address");
        }
    }
}
