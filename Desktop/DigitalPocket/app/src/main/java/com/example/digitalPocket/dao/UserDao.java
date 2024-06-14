package com.example.digitalPocket.dao;

import com.example.digitalPocket.Config;
import com.example.digitalPocket.data.model.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class UserDao {

    private OkHttpClient httpClient;
    private static UserDao instance;

    public static User LoggedUser;
    public static List<User> LoggedUserBalance;
    public String ip= Config.IP;

    // Private constructor to prevent instantiation
    private UserDao() {
        this.httpClient = new OkHttpClient();
    }

    // Method to get the single instance of UserDao
    public static synchronized UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public User getLoggedUser() {
        return LoggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        LoggedUser = loggedUser;
    }

    public void getUserById(int id, final UserCallback<User> callback) {
        String url = ip+"/user/id?id=" + id;
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    User user = parseUserJson(responseData);
                    callback.onSuccess(user);
                } else {
                    callback.onError("Failed to get user by id");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getUserByEmail(String email, final UserCallback<User> callback) {
        String url = ip+"/user/email?email=" + email;
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    User user = parseUserJson(responseData);
                    callback.onSuccess(user);
                } else {
                    callback.onError("Failed to get user by email");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getBalanceByEmail(String email, final UserCallback<List<User>> callback) {
        String url = ip+"/user/getbalance?e_mail=" + email;
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    List<User> userBalance = parseUserListJson(responseData);
                    callback.onSuccess(userBalance);
                } else {
                    callback.onError("Failed to get balance by email");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void addNewUser(String username,String email,String pwd, final UserCallback<String> callback) {
        String url = ip+"/user/addNewUser";
        FormBody formBody = new FormBody.Builder()
                .add("username",username)
                .add("email",email)
                .add("passwd",pwd).build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                } else {
                    callback.onError("Failed to add new user");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }


    public void sendCrypto(String saddress, String cryptoName, BigDecimal number, String raddress, final UserCallback<String> callback) {
        String url = ip + "/send";
        FormBody formBody = new FormBody.Builder()
                .add("saddress", saddress)
                .add("cryptoName", cryptoName)
                .add("number", String.valueOf(number))
                .add("raddress", raddress).build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
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

    public void updateUser(String email,String username,String pwd, final UserCallback<String> callback) {
        String url = ip+"/user/update";
        FormBody formBody;
        if(email!=null&&pwd!=null){
            formBody = new FormBody.Builder()
                    .add("email",email)
                    .add("passwd",pwd).build();
        }else{
            formBody = new FormBody.Builder()
                    .add("username",username)
                    .add("email",email).build();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                } else {
                    callback.onError("Failed to update user");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }


    private User parseUserJson(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, User.class);
    }

    private List<User> parseUserListJson(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new com.google.gson.reflect.TypeToken<List<User>>() {}.getType());
    }

    public interface UserCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}
