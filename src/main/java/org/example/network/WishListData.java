package org.example.network;

import java.io.Serializable;
import java.util.List;

public class WishListData implements Serializable {

    private final int id;
    private final String name;
    private final List<GiftItemData> items;

    public WishListData(int id, String name, List<GiftItemData> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<GiftItemData> getItems() {
        return items;
    }
}