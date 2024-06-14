package com.example.digitalPocket.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.digitalPocket.R;
import com.example.digitalPocket.data.model.User;
import com.example.digitalPocket.dao.UserDao;
import com.google.android.material.snackbar.Snackbar;

public class RegistActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;
    String pwd,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(LoginViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText usernameEditText=findViewById(R.id.username);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final Button registButton=findViewById(R.id.regbutt);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwd= passwordEditText.getText().toString().trim();
                if(!loginViewModel.isPasswordValid(pwd)) passwordEditText.setError("至少6位密码");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username= emailEditText.getText().toString().trim();
                if(!loginViewModel.isUserNameValid(username)) emailEditText.setError("不符合邮箱格式");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString().trim();
                String username=usernameEditText.getText().toString().trim();
                String pwd=passwordEditText.getText().toString().trim();
                if(email.equals("")||username.equals("")||pwd.equals("")){
                    Snackbar.make(findViewById(android.R.id.content), "邮箱,用户名，密码不能为空", Snackbar.LENGTH_SHORT).show();
                }else{
                    UserDao.getInstance().getUserByEmail(email, new UserDao.UserCallback<User>() {
                        @Override
                        public void onSuccess(User data) {
                            if(data!=null){
                                Snackbar.make(findViewById(android.R.id.content), "邮箱已注册", Snackbar.LENGTH_SHORT).show();
                            }else{
                                User newUser=new User(username,email,pwd);
                                UserDao.getInstance().addNewUser(username, email, pwd, new UserDao.UserCallback<String>() {
                                    @Override
                                    public void onSuccess(String data) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 创建一个AlertDialog.Builder
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                                                // 设置对话框标题和消息
                                                builder.setTitle("注册成功");
                                                builder.setMessage("您的用户名为：" + username + "，邮箱为：" + email+"私钥为："+data.toString());
                                                // 添加一个确定按钮，并设置点击事件，点击后关闭对话框
                                                builder.setPositiveButton("确定", (dialogInterface, i) -> {
                                                    dialogInterface.dismiss(); // 关闭对话框
                                                    finish(); // 结束当前Activity
                                                });
                                                // 创建并显示AlertDialog
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Snackbar.make(findViewById(android.R.id.content),"注册失败", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Snackbar.make(findViewById(android.R.id.content), "出现错误: " + message, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }
}