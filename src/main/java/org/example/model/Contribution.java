package org.example.model ;

public class Contribution {
    private int id;
    private double amount;
    private final Client client;
    private final GiftItem giftItem;
    private final WishList wishList;

    public Contribution(Client client, GiftItem giftItem, WishList wishList, double amount) {
        this.client = client;
        this.giftItem = giftItem;
        this.wishList = wishList;
        setAmount(amount);
    }

    public Contribution(int id, Client client, GiftItem giftItem, WishList wishList, double amount) {
        this(client, giftItem, wishList, amount);
        this.id = id;
    }

    public void contribute() {
        if (client == null || giftItem == null || wishList == null)
            throw new IllegalStateException("A contribution needs a contributor, a gift item and a wish list");
        if (client.equals(wishList.getOwner()))
            throw new IllegalStateException("You cannot contribute to your own wish list");
        if (amount > giftItem.getPrice())
            throw new IllegalStateException("A contribution cannot exceed the item price");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero");
        this.amount = amount;
    }
    public Client getClient() { return client; }
    public GiftItem getGiftItem() { return giftItem; }
    public WishList getWishList() { return wishList; }

    @Override public String toString() {
        return client.getUsername() + " gave " + amount + " for " + giftItem.getName();
    }
}
