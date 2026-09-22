package org.example.service;

public class Login {
    private final Auth auth;

    public Login(Auth auth) { this.auth = auth; }

    public boolean login(String username, String password) { return auth.login(username, password); }
}
