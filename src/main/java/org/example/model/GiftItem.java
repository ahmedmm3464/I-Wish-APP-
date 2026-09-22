package org.example.model ;

public class GiftItem {
    private int id;
    private String name;
    private double price;

    public GiftItem(String name, double price) {
        setName(name);
        setPrice(price);
    }

    public GiftItem(int id, String name, double price) {
        this(name, price);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Gift item name is required");
        this.name = name.trim();
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Price must be greater than zero");
        this.price = price;
    }

    @Override public boolean equals(Object o) {
        return o instanceof GiftItem && id != 0 && id == ((GiftItem) o).id;
    }
    @Override public int hashCode() { return Integer.hashCode(id); }
    @Override public String toString() { return name + " (" + price + ")"; }
}
