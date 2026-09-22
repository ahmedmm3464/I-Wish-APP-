package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.model.GiftItem;
import org.example.model.WishList;
import org.example.util.SqlErrors;

public class WishListDAO {
    public boolean create(WishList wishList) throws SQLException {
        String sql = "INSERT INTO wish_lists (client_id, name) VALUES (?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, wishList.getOwner().getId());
            ps.setString(2, wishList.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) wishList.setId(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            if (SqlErrors.isDuplicateKey(e)) return false;
            throw e;
        }
    }

    public boolean update(WishList wishList) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement("UPDATE wish_lists SET name = ? WHERE id = ?")) {
            ps.setString(1, wishList.getName());
            ps.setInt(2, wishList.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM wish_lists WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public WishList findByUser(int userId) throws SQLException {
        String sql = "SELECT w.id AS w_id, w.name AS w_name, c.id AS c_id, c.username AS c_username, "
                + "c.name AS c_name, c.date_of_birth AS c_dob "
                + "FROM wish_lists w JOIN clients c ON c.id = w.client_id WHERE w.client_id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        WishList wl;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                wl = new WishList(rs.getInt("w_id"), rs.getString("w_name"), UserDAO.map(rs, "c_"));
            }
        }
        wl.setItems(getItems(wl.getId()));
        return wl;
    }

    public boolean addItem(int wishListId, int giftItemId) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO wishlist_items (wishlist_id, gift_item_id) VALUES (?, ?)")) {
            ps.setInt(1, wishListId);
            ps.setInt(2, giftItemId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            if (SqlErrors.isDuplicateKey(e)) return false;
            throw e;
        }
    }

    public boolean removeItem(int wishListId, int giftItemId) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(
                "DELETE FROM wishlist_items WHERE wishlist_id = ? AND gift_item_id = ?")) {
            ps.setInt(1, wishListId);
            ps.setInt(2, giftItemId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean containsItem(int wishListId, int giftItemId) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM wishlist_items WHERE wishlist_id = ? AND gift_item_id = ?")) {
            ps.setInt(1, wishListId);
            ps.setInt(2, giftItemId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    List<GiftItem> getItems(int wishListId) throws SQLException {
        String sql = "SELECT g.id, g.name, g.price FROM gift_items g "
                + "JOIN wishlist_items wi ON wi.gift_item_id = g.id WHERE wi.wishlist_id = ? ORDER BY g.name";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, wishListId);
            try (ResultSet rs = ps.executeQuery()) {
                List<GiftItem> items = new ArrayList<>();
                while (rs.next()) items.add(GiftItemDAO.map(rs, ""));
                return items;
            }
        }
    }
}
