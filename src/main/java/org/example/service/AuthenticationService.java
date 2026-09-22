package org.example.service;


import java.sql.SQLException;
import org.example.model.Client;
import org.example.database.UserDAO;

public class AuthenticationService implements Auth {
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_USERNAME_LENGTH = 3;

    private final UserDAO userDAO;

    public AuthenticationService() {
        this(new UserDAO());
    }

    public AuthenticationService(UserDAO userDAO) { this.userDAO = userDAO; }

    @Override
    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null) return false;
        try {
            return userDAO.validateLogin(username.trim(), password);
        } catch (SQLException e) {
            throw new IWishException("Login failed because of a database error", e);
        }
    }

    @Override
    public boolean register(Client client) {
        if (client == null) throw new IllegalArgumentException("Client is required");
        if (client.getUsername().length() < MIN_USERNAME_LENGTH)
            throw new IWishException("Username must have at least " + MIN_USERNAME_LENGTH + " characters");
        if (client.getPassword().length() < MIN_PASSWORD_LENGTH)
            throw new IWishException("Password must have at least " + MIN_PASSWORD_LENGTH + " characters");
        try {
            if (userDAO.usernameExists(client.getUsername())) return false;
            return userDAO.create(client);
        } catch (SQLException e) {
            throw new IWishException("Registration failed because of a database error", e);
        }
    }

    public Client findClient(String username) {
        try {
            return userDAO.findByUsername(username);
        } catch (SQLException e) {
            throw new IWishException("Could not load the client", e);
        }
    }
}
