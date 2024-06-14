package com.example.digitalPocket.ui.login;

import com.example.digitalPocket.data.model.User;

public class LoggedUser {
    public static User loggedUser;
    public static String  loggedUserName;
    public static String  loggedUserAddress;
    public static String loggedUserEmail;
    public static String loggedUserPubKey;

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        LoggedUser.loggedUser = loggedUser;
    }

    public static String getLoggedUserName() {
        return loggedUserName;
    }

    public static void setLoggedUserName(String loggedUserName) {
        LoggedUser.loggedUserName = loggedUserName;
    }

    public static String getLoggedUserAddress() {
        return loggedUserAddress;
    }

    public static void setLoggedUserAddress(String loggedUserAddress) {
        LoggedUser.loggedUserAddress = loggedUserAddress;
    }

    public static String getLoggedUserEmail() {
        return loggedUserEmail;
    }

    public static void setLoggedUserEmail(String loggedUserEmail) {
        LoggedUser.loggedUserEmail = loggedUserEmail;
    }

    public static String getLoggedUserPubKey() {
        return loggedUserPubKey;
    }

    public static void setLoggedUserPubKey(String loggedUserPubKey) {
        LoggedUser.loggedUserPubKey = loggedUserPubKey;
    }
}
