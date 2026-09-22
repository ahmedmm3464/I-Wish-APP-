package org.example.network;

import java.io.Serializable;

public class AddFriendData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int senderId;
    private final int receiverId;

    public AddFriendData(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }
}