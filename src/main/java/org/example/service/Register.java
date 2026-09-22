package org.example.service;

import org.example.model.Client;

public class Register {
    private final Auth auth;

    public Register(Auth auth) { this.auth = auth; }

    public boolean register(Client client) { return auth.register(client); }
}
