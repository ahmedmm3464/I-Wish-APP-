package org.example.model ;

import java.time.LocalDate;
import java.util.Objects;

public class Client {
    private int id;
    private String username;
    private String password;
    private String name;
    private LocalDate dateOfBirth;

    public Client(String username, String password, String name, LocalDate dateOfBirth) {
        setUsername(username);
        setPassword(password);
        setName(name);
        this.dateOfBirth = dateOfBirth;
    }

    public Client(int id, String username, String name, LocalDate dateOfBirth) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
        this.username = username.trim();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password is required");
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        this.name = name.trim();
    }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        this.dateOfBirth = dateOfBirth;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        return Objects.equals(username, ((Client) o).username);
    }
    @Override public int hashCode() { return Objects.hash(username); }
    @Override public String toString() { return name + " (@" + username + ")"; }
}
