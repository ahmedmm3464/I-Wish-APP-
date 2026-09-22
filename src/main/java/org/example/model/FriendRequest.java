package org.example.model ;

import java.time.LocalDateTime;

public class FriendRequest {
    private int id;
    private final Client sender;
    private final Client receiver;
    private LocalDateTime dateCreated;
    private FriendRequestStatus status;

    public FriendRequest(Client sender, Client receiver) {
        if (sender == null || receiver == null) throw new IllegalArgumentException("Sender and receiver are required");
        if (sender.equals(receiver)) throw new IllegalArgumentException("You cannot send a friend request to yourself");
        this.sender = sender;
        this.receiver = receiver;
        this.dateCreated = LocalDateTime.now().withNano(0);
        this.status = FriendRequestStatus.PENDING;
    }

    public FriendRequest(int id, Client sender, Client receiver, LocalDateTime dateCreated, FriendRequestStatus status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public void acceptFriend() {
        requirePending();
        status = FriendRequestStatus.ACCEPTED;
    }

    public void declineFriend() {
        requirePending();
        status = FriendRequestStatus.DECLINED;
    }

    private void requirePending() {
        if (status != FriendRequestStatus.PENDING)
            throw new IllegalStateException("Request is already " + status + "; only PENDING requests can change");
    }

    public boolean involves(Client c) { return sender.equals(c) || receiver.equals(c); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Client getSender() { return sender; }
    public Client getReceiver() { return receiver; }
    public LocalDateTime getDateCreated() { return dateCreated; }
    public FriendRequestStatus getStatus() { return status; }

    @Override public String toString() { return sender.getUsername() + " -> " + receiver.getUsername() + " [" + status + "]"; }
}
