package org.example.model ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WishList {
    private int id;
    private String name;
    private Client owner;
    private final List<GiftItem> items = new ArrayList<>();

    public WishList(String name, Client owner) {
        setName(name);
        if (owner == null) throw new IllegalArgumentException("A wish list needs an owner");
        this.owner = owner;
    }

    public WishList(int id, String name, Client owner) {
        this(name, owner);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Wish list name is required");
        this.name = name.trim();
    }

    public Client getOwner() { return owner; }

    public List<GiftItem> getItems() { return Collections.unmodifiableList(items); }

    public void setItems(List<GiftItem> loaded) {
        items.clear();
        items.addAll(loaded);
    }

    public boolean contains(GiftItem item) { return items.contains(item); }

    public void addItem(GiftItem item) {
        if (item == null) throw new IllegalArgumentException("Item is required");
        if (items.contains(item)) throw new IllegalStateException("Item is already in the wish list");
        items.add(item);
    }

    public void removeItem(GiftItem item) {
        if (!items.remove(item)) throw new IllegalStateException("Item is not in the wish list");
    }

    @Override public String toString() { return name + " [" + items.size() + " items]"; }
}
