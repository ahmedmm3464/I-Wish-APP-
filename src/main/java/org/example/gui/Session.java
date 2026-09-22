package org.example.gui;

import org.example.network.ClientData;

public class Session {

    private static ClientData currentUser;

    public static void setCurrentUser(ClientData user) {
        currentUser = user;
    }

    public static ClientData getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}