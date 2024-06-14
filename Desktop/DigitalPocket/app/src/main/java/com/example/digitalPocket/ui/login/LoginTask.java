package com.example.digitalPocket.ui.login;



import com.example.digitalPocket.Config;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginTask {
    String ip= Config.IP;
    public interface LoginCallback {
        void onLoginSuccess(boolean success);
    }
    public void loginAuth(String username, String passwd,LoginCallback loginCallback){
        String url=ip+"/user/auth";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("e_mail",username)
                .add("passwd",passwd).build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loginCallback.onLoginSuccess(false);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str =response.body().string();
                if(str.equals("true")){
                    loginCallback.onLoginSuccess(true);
                }else{
                    loginCallback.onLoginSuccess(false);
                }
            }
        });
    }
}
