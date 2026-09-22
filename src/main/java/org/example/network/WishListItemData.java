package org.example.network;

import java.io.Serializable;

public class WishListItemData implements Serializable {

    private final int wishListId;
    private final int giftItemId;

    public WishListItemData(int wishListId, int giftItemId) {
        this.wishListId = wishListId;
        this.giftItemId = giftItemId;
    }

    public int getWishListId() {
        return wishListId;
    }

    public int getGiftItemId() {
        return giftItemId;
    }
}