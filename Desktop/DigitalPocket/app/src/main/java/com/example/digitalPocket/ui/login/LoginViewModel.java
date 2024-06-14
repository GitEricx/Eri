package com.example.digitalPocket.ui.login;

import androidx.lifecycle.AndroidViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

public class LoginViewModel extends AndroidViewModel {
    private Context context;

    // 构造方法，接收一个Context对象
    public LoginViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public interface LoginResultCallback {
        void onSuccess(String email);
        void onError(String message);
    }
    public void login(String username, String password, LoginResultCallback callback) {
        if (isUserNameValid(username) && isPasswordValid(password)) {
            LoginTask loginTask = new LoginTask();
            loginTask.loginAuth(username, password, new LoginTask.LoginCallback() {
                @Override
                public void onLoginSuccess(boolean success) {
                    if (success) {
                        saveUserCredentials(username);
                        callback.onSuccess(username);
                    } else {
                        callback.onError("Invalid username or password");
                    }
                }
            });
        } else {
            callback.onError("failed to login");
        }
    }


    private void saveUserCredentials(String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DigitalPursePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", email);
        editor.apply();
    }

    // A placeholder username validation check
    public boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            return true;
        } else {
            return false;
        }
    }

    // A placeholder password validation check
    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}