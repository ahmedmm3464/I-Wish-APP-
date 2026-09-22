package org.example.service;

import org.example.model.Client;

public interface Auth {
    boolean login(String username, String password);

    boolean register(Client client);
}
