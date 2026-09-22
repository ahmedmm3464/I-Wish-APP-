package org.example.network;

import java.io.Serializable;
import java.time.LocalDate;

public class ClientData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String username;
    private final String name;
    private final LocalDate dateOfBirth;

    public ClientData(
            int id,
            String username,
            String name,
            LocalDate dateOfBirth
    ) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}