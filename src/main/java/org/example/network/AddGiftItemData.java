package org.example.network;

import java.io.Serializable;

public class AddGiftItemData implements Serializable {

    private final String name;
    private final double price;

    public AddGiftItemData(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}