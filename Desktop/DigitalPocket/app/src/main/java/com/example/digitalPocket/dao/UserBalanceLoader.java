package com.example.digitalPocket.dao;

import com.example.digitalPocket.data.model.User;

import java.util.List;

public class UserBalanceLoader {

    private static UserBalanceLoader instance;
    private UserDao userDao;

    private UserBalanceLoader() {
        userDao = UserDao.getInstance();
    }

    public static synchronized UserBalanceLoader getInstance() {
        if (instance == null) {
            instance = new UserBalanceLoader();
        }
        return instance;
    }

    public void loadUserBalance(String userEmail, final UserBalanceCallback callback) {
        userDao.getBalanceByEmail(userEmail, new UserDao.UserCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                callback.onUserBalanceLoaded(data);
            }

            @Override
            public void onError(String message) {
                callback.onLoadFailed(message);
            }
        });
    }
    public interface UserBalanceCallback {
        void onUserBalanceLoaded(List<User> data);
        void onLoadFailed(String message);
    }
}

