package org.example.service;

import org.example.database.GiftItemDAO;
import org.example.database.WishListDAO;
import java.sql.SQLException;
import org.example.model.Client;
import org.example.model.GiftItem;
import org.example.model.WishList;
import org.example.util.SqlErrors;

public class WishListOperation {
    private final Client owner;
    private final WishListDAO wishListDAO;
    private final GiftItemDAO giftItemDAO = new GiftItemDAO();

    public WishListOperation(Client owner) { this(owner, new WishListDAO()); }

    public WishListOperation(Client owner, WishListDAO wishListDAO) {
        if (owner == null || owner.getId() <= 0) throw new IllegalArgumentException("The owner must be a registered client");
        this.owner = owner;
        this.wishListDAO = wishListDAO;
    }

    public WishList createWishList(String name) {
        WishList wl = new WishList(name, owner);
        try {
            if (!wishListDAO.create(wl)) throw new IWishException("You already have a wish list");
            return wl;
        } catch (SQLException e) {
            throw new IWishException("Could not create the wish list", e);
        }
    }

    public void updateWishList(String newName) {
        WishList wl = requireWishList();
        wl.setName(newName);
        try {
            wishListDAO.update(wl);
        } catch (SQLException e) {
            throw new IWishException("Could not update the wish list", e);
        }
    }

    public void deleteWishList() {
        WishList wl = requireWishList();
        try {
            wishListDAO.delete(wl.getId());
        } catch (SQLException e) {
            if (SqlErrors.isIntegrityViolation(e))
                throw new IWishException("This wish list cannot be deleted because friends already contributed to its items", e);
            throw new IWishException("Could not delete the wish list", e);
        }
    }

    public void addGiftItem(GiftItem item) {
        WishList wl = requireWishList();
        try {
            if (giftItemDAO.findById(item.getId()) == null)
                throw new IWishException("This item is not in the catalog");
            if (!wishListDAO.addItem(wl.getId(), item.getId()))
                throw new IWishException("The item is already in your wish list");
        } catch (SQLException e) {
            throw new IWishException("Could not add the item to the wish list", e);
        }
    }

    public void removeGiftItem(GiftItem item) {
        WishList wl = requireWishList();
        try {
            if (!wishListDAO.removeItem(wl.getId(), item.getId()))
                throw new IWishException("The item is not in your wish list");
        } catch (SQLException e) {
            if (SqlErrors.isIntegrityViolation(e))
                throw new IWishException("Friends already contributed to this item, it cannot be removed", e);
            throw new IWishException("Could not remove the item", e);
        }
    }

    public WishList getWishList() {
        try {
            return wishListDAO.findByUser(owner.getId());
        } catch (SQLException e) {
            throw new IWishException("Could not load the wish list", e);
        }
    }

    private WishList requireWishList() {
        WishList wl = getWishList();
        if (wl == null) throw new IWishException("You do not have a wish list yet");
        return wl;
    }
}
