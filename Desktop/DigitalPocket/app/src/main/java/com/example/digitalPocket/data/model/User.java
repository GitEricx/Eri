package com.example.digitalPocket.data.model;

import java.math.BigDecimal;

public class User {
    int id;
    String username;
    String email;
    String passwd;
    String address;
    String cryptoName;
    String pubKey;
    BigDecimal balance;

    public User(int id, String username, String email, String passwd, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwd = passwd;
        this.address = address;
    }

    public User(int id, String username, String email, String passwd, String address, String cryptoName, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwd = passwd;
        this.address = address;
        this.cryptoName = cryptoName;
        this.balance = balance;
    }

    public User(String username, String email, String passwd) {
        this.username = username;
        this.email = email;
        this.passwd = passwd;
    }

    public User(String username, String email, String passwd, String address, String cryptoName, String pubKey, BigDecimal balance) {
        this.username = username;
        this.email = email;
        this.passwd = passwd;
        this.address = address;
        this.cryptoName = cryptoName;
        this.pubKey = pubKey;
        this.balance = balance;
    }

    public User(String cryptoName, BigDecimal balance) {
        this.cryptoName = cryptoName;
        this.balance = balance;
    }

    public String getCryptoName() {return cryptoName;}

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public BigDecimal getBalance() {return balance; }

    public void setBalance(BigDecimal balance) {this.balance = balance; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
}
