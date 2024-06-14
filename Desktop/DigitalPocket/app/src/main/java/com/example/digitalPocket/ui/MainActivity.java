package com.example.digitalPocket.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.digitalPocket.R;
import com.example.digitalPocket.data.model.User;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.ui.login.LoggedUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="1";
    public User loggedUser;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_deal, R.id.navigation_market)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        welcomeAndgetLoggedUser();
    }



    public void welcomeAndgetLoggedUser(){
        Intent intent = getIntent();
        if(intent.getStringExtra("email")!=null){
            String email = intent.getStringExtra("email");
            UserDao.getInstance().getUserByEmail(email, new UserDao.UserCallback<User>() {
                @Override
                public void onSuccess(User data) {
                    LoggedUser.loggedUserAddress=data.getAddress();
                    loggedUser=data;
                    LoggedUser.setLoggedUser(data);
                    username=data.getUsername();
                    Snackbar.make(findViewById(R.id.container), "Welcome "+username, Snackbar.LENGTH_SHORT).show();
                }
                @Override
                public void onError(String message) {
                    Snackbar.make(findViewById(R.id.container), "failed to get name", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}