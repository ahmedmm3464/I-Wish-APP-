package org.example.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.model.Client;
import org.example.util.PasswordHasher;
import org.example.util.SqlErrors;

public class UserDAO {
    public boolean create(Client client) throws SQLException {
        String sql = "INSERT INTO clients (username, password_hash, name, date_of_birth) VALUES (?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getUsername());
            ps.setString(2, PasswordHasher.hash(client.getPassword()));
            ps.setString(3, client.getName());
            ps.setDate(4, client.getDateOfBirth() == null ? null : Date.valueOf(client.getDateOfBirth()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) client.setId(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            if (SqlErrors.isDuplicateKey(e)) return false;
            throw e;
        }
    }

    public Client findById(int id) throws SQLException {
        String sql = "SELECT id, username, name, date_of_birth FROM clients WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs, "") : null;
            }
        }
    }

    public Client findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, name, date_of_birth FROM clients WHERE username = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs, "") : null;
            }
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        return findByUsername(username) != null;
    }
    public List<Client> searchUsers(String query, int currentUserId) throws SQLException {
        String sql =
                "SELECT id, username, name, date_of_birth " +
                        "FROM clients " +
                        "WHERE id <> ? " +
                        "AND (username LIKE ? OR name LIKE ?) " +
                        "ORDER BY name, username";

        Connection con = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";

            ps.setInt(1, currentUserId);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {

                List<Client> users = new ArrayList<>();

                while (rs.next()) {
                    users.add(map(rs, ""));
                }

                return users;
            }
        }
    }

    public boolean validateLogin(String username, String password) throws SQLException {
        String sql = "SELECT password_hash FROM clients WHERE username = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && PasswordHasher.verify(password, rs.getString(1));
            }
        }
    }

    static Client map(ResultSet rs, String prefix) throws SQLException {
        String dobCol = prefix.isEmpty() ? "date_of_birth" : prefix + "dob";
        Date dob = rs.getDate(dobCol);
        LocalDate birth = dob == null ? null : dob.toLocalDate();
        return new Client(rs.getInt(prefix + "id"), rs.getString(prefix + "username"), rs.getString(prefix + "name"), birth);
    }
}
