package org.example.service;

import org.example.database.GiftItemDAO;
import java.sql.SQLException;
import java.util.List;
import org.example.model.GiftItem;
import org.example.util.SqlErrors;

public class GiftItemOperations {
    private final GiftItemDAO giftItemDAO;

    public GiftItemOperations() { this(new GiftItemDAO()); }

    public GiftItemOperations(GiftItemDAO giftItemDAO) { this.giftItemDAO = giftItemDAO; }

    public void addGiftItem(GiftItem item) {
        try {
            if (!giftItemDAO.add(item)) throw new IWishException("The gift item could not be added");
        } catch (SQLException e) {
            throw new IWishException("Could not add the gift item", e);
        }
    }

    public void deleteGiftItem(GiftItem item) {
        try {
            if (!giftItemDAO.delete(item.getId())) throw new IWishException("Gift item not found");
        } catch (SQLException e) {
            if (SqlErrors.isIntegrityViolation(e))
                throw new IWishException("This gift item is used in wish lists and cannot be deleted", e);
            throw new IWishException("Could not delete the gift item", e);
        }
    }

    public List<GiftItem> getAvailableItems() {
        try {
            return giftItemDAO.findAll();
        } catch (SQLException e) {
            throw new IWishException("Could not load the gift items", e);
        }
    }
}
