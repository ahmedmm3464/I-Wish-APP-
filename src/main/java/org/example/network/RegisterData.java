package org.example.network;

import java.io.Serializable;

public class RegisterData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final String name;
    private final String email;

    public RegisterData(
            String username,
            String password,
            String name,
            String email
    ) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}