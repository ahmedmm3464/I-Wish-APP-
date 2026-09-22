package org.example.network;

import java.io.Serializable;

public class UpdateGiftItemData implements Serializable {

    private final int id;
    private final String name;
    private final double price;

    public UpdateGiftItemData(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}