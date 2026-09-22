package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.example.model.Client;
import org.example.model.FriendRequest;
import org.example.model.FriendRequestStatus;
import org.example.util.SqlErrors;

public class FriendRequestDAO {
    private static final String SELECT =
            "SELECT fr.id, fr.date_created, fr.status, "
            + "s.id AS s_id, s.username AS s_username, s.name AS s_name, s.date_of_birth AS s_dob, "
            + "r.id AS r_id, r.username AS r_username, r.name AS r_name, r.date_of_birth AS r_dob "
            + "FROM friend_requests fr "
            + "JOIN clients s ON s.id = fr.sender_id "
            + "JOIN clients r ON r.id = fr.receiver_id ";

    public boolean create(FriendRequest request) throws SQLException {
        String sql = "INSERT INTO friend_requests (sender_id, receiver_id, date_created, status) VALUES (?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, request.getSender().getId());
            ps.setInt(2, request.getReceiver().getId());
            ps.setTimestamp(3, Timestamp.valueOf(request.getDateCreated()));
            ps.setString(4, request.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) request.setId(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            if (SqlErrors.isDuplicateKey(e)) return false;
            throw e;
        }
    }

    public boolean accept(int id) throws SQLException { return transition(id, FriendRequestStatus.ACCEPTED); }

    public boolean decline(int id) throws SQLException { return transition(id, FriendRequestStatus.DECLINED); }

    private boolean transition(int id, FriendRequestStatus to) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE friend_requests SET status = ? WHERE id = ? AND status = 'PENDING'")) {
            ps.setString(1, to.name());
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM friend_requests WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public FriendRequest findBetween(int clientA, int clientB) throws SQLException {
        String sql = SELECT + "WHERE (fr.sender_id = ? AND fr.receiver_id = ?) "
                + "OR (fr.sender_id = ? AND fr.receiver_id = ?) ORDER BY fr.id LIMIT 1";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clientA);
            ps.setInt(2, clientB);
            ps.setInt(3, clientB);
            ps.setInt(4, clientA);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<FriendRequest> findByUser(int clientId) throws SQLException {
        return query(SELECT + "WHERE fr.sender_id = ? OR fr.receiver_id = ? ORDER BY fr.date_created DESC, fr.id DESC",
                clientId, clientId);
    }

    public List<FriendRequest> findPendingForReceiver(int receiverId) throws SQLException {
        return query(SELECT + "WHERE fr.receiver_id = ? AND fr.status = 'PENDING' ORDER BY fr.date_created, fr.id",
                receiverId);
    }

    public List<Client> findFriends(int clientId) throws SQLException {
        String sql = "SELECT c.id, c.username, c.name, c.date_of_birth FROM clients c "
                + "JOIN friend_requests fr ON (fr.sender_id = c.id AND fr.receiver_id = ?) "
                + "OR (fr.receiver_id = c.id AND fr.sender_id = ?) "
                + "WHERE fr.status = 'ACCEPTED' ORDER BY c.name";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ps.setInt(2, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Client> friends = new ArrayList<>();
                while (rs.next()) friends.add(UserDAO.map(rs, ""));
                return friends;
            }
        }
    }

    private List<FriendRequest> query(String sql, int... params) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setInt(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                List<FriendRequest> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    private static FriendRequest map(ResultSet rs) throws SQLException {
        return new FriendRequest(rs.getInt("id"), UserDAO.map(rs, "s_"), UserDAO.map(rs, "r_"),
                rs.getTimestamp("date_created").toLocalDateTime(),
                FriendRequestStatus.valueOf(rs.getString("status")));
    }
}
