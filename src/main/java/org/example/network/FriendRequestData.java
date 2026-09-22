package org.example.network;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FriendRequestData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final int senderId;
    private final String senderUsername;
    private final String senderName;
    private final LocalDateTime dateCreated;

    public FriendRequestData(
            int id,
            int senderId,
            String senderUsername,
            String senderName,
            LocalDateTime dateCreated) {

        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.senderName = senderName;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderName() {
        return senderName;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
}