package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.model.GiftItem;


public class GiftItemDAO {
    public boolean add(GiftItem item) throws SQLException {
        String sql = "INSERT INTO gift_items (name, price) VALUES (?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            if (ps.executeUpdate() != 1) return false;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) item.setId(keys.getInt(1));
            }
            return true;
        }
    }

    public boolean delete(int id) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM gift_items WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public GiftItem findById(int id) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT id, name, price FROM gift_items WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs, "") : null;
            }
        }
    }

    public List<GiftItem> findAll() throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, price FROM gift_items ORDER BY name")) {
            List<GiftItem> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs, ""));
            return list;
        }
    }

    static GiftItem map(ResultSet rs, String prefix) throws SQLException {
        return new GiftItem(rs.getInt(prefix + "id"), rs.getString(prefix + "name"), rs.getDouble(prefix + "price"));
    }
    public boolean update(GiftItem item) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = con.prepareStatement(
                "UPDATE gift_items SET name = ?, price = ? WHERE id = ?")) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setInt(3, item.getId());

            return ps.executeUpdate() == 1;
        }
    }
}
