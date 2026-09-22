package org.example.network ;

import java.io.Serializable;

public class WishItemData implements Serializable {

    private int id;
    private String item;
    private double price;

    public WishItemData(
            int id,
            String item,
            double price
    ) {
        this.id = id;
        this.item = item;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }
}