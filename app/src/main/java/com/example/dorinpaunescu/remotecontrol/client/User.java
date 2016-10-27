package com.example.dorinpaunescu.remotecontrol.client;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public class User {

    private String username;
    private String password;

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
