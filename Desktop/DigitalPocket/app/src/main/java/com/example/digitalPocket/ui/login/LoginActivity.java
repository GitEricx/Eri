package com.example.digitalPocket.ui.login;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.digitalPocket.ui.MainActivity;
import com.example.digitalPocket.R;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private String pwd, username;
    private View rootView; // 根视图
    private ProgressBar loadingProgressBar;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("DigitalPursePreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("username", null) != null) {
            String email = sharedPreferences.getString("username", null);
            LoggedUser.loggedUserEmail=email;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }

        loginViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.loginbutt);
        final Button registButton = findViewById(R.id.regbutt);
        loadingProgressBar = findViewById(R.id.loading);
        rootView = findViewById(R.id.login_container); // 初始化根视图

        if (getIntent().getStringExtra("lastLoggedUser") != null) {
            usernameEditText.setText(getIntent().getStringExtra("lastLoggedUser"));
        }

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwd = passwordEditText.getText().toString().trim();
                if (!loginViewModel.isPasswordValid(pwd)) {
                    passwordEditText.setError("至少6位密码");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username = usernameEditText.getText().toString().trim();
                if (!loginViewModel.isUserNameValid(username)) {
                    usernameEditText.setError("不符合邮箱格式");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString().trim();
                showLoading(true); // 显示加载进度条并禁用用户交互
                loginViewModel.login(username, pwd, new LoginViewModel.LoginResultCallback() {
                    @Override
                    public void onSuccess(String email) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoggedUser.loggedUserEmail=email;
                                showLoading(false); // 隐藏加载进度条并启用用户交互
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showLoading(false); // 隐藏加载进度条并启用用户交互
                                Snackbar.make(findViewById(R.id.login_container), message, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            rootView.setEnabled(false); // 禁用根视图用户交互
            passwordEditText.setEnabled(false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); // 禁用窗口用户交互
        } else {
            loadingProgressBar.setVisibility(View.GONE);
            rootView.setEnabled(true); // 启用根视图用户交互
            passwordEditText.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); // 启用窗口用户交互
        }
    }
}
