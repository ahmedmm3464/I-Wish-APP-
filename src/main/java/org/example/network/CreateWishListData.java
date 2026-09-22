package org.example.network;

import java.io.Serializable;

public class CreateWishListData implements Serializable {

    private final int userId;
    private final String name;

    public CreateWishListData(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}