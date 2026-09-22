package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.model.Contribution;
import org.example.model.WishList;

public class ContributionDAO {
    private static final String SELECT =
            "SELECT co.id, co.amount, "
            + "c.id AS c_id, c.username AS c_username, c.name AS c_name, c.date_of_birth AS c_dob, "
            + "g.id AS g_id, g.name AS g_name, g.price AS g_price, "
            + "w.id AS w_id, w.name AS w_name, "
            + "o.id AS o_id, o.username AS o_username, o.name AS o_name, o.date_of_birth AS o_dob "
            + "FROM contributions co "
            + "JOIN clients c ON c.id = co.client_id "
            + "JOIN gift_items g ON g.id = co.gift_item_id "
            + "JOIN wish_lists w ON w.id = co.wishlist_id "
            + "JOIN clients o ON o.id = w.client_id ";

    public boolean create(Contribution c) throws SQLException {
        String sql = "INSERT INTO contributions (client_id, gift_item_id, wishlist_id, amount) VALUES (?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getClient().getId());
            ps.setInt(2, c.getGiftItem().getId());
            ps.setInt(3, c.getWishList().getId());
            ps.setDouble(4, c.getAmount());
            if (ps.executeUpdate() != 1) return false;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
            return true;
        }
    }

    public double getTotal(int wishListId, int giftItemId) throws SQLException {
        return sum("SELECT COALESCE(SUM(amount), 0) FROM contributions WHERE wishlist_id = ? AND gift_item_id = ?",
                wishListId, giftItemId);
    }

    public double getTotalForGiftItem(int giftItemId) throws SQLException {
        return sum("SELECT COALESCE(SUM(amount), 0) FROM contributions WHERE gift_item_id = ?", giftItemId);
    }

    public List<Contribution> getContributions(int wishListId, int giftItemId) throws SQLException {
        return query(SELECT + "WHERE co.wishlist_id = ? AND co.gift_item_id = ? ORDER BY co.id", wishListId, giftItemId);
    }

    public List<Contribution> getContributions(int giftItemId) throws SQLException {
        return query(SELECT + "WHERE co.gift_item_id = ? ORDER BY co.id", giftItemId);
    }

    private double sum(String sql, int... params) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setInt(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        }
    }

    private List<Contribution> query(String sql, int... params) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setInt(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                List<Contribution> list = new ArrayList<>();
                while (rs.next()) {
                    WishList wl = new WishList(rs.getInt("w_id"), rs.getString("w_name"), UserDAO.map(rs, "o_"));
                    list.add(new Contribution(rs.getInt("id"), UserDAO.map(rs, "c_"), GiftItemDAO.map(rs, "g_"),
                            wl, rs.getDouble("amount")));
                }
                return list;
            }
        }
    }
}
