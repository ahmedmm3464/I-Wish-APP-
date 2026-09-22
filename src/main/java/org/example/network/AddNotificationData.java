package org.example.network;

import java.io.Serializable;

public class AddNotificationData implements Serializable {

    private final int recipientId;
    private final String message;

    public AddNotificationData(
            int recipientId,
            String message) {

        this.recipientId = recipientId;
        this.message = message;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }
}