package org.example.network;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NotificationData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String type;
    private final String message;
    private final LocalDateTime date;
    private final boolean read;

    public NotificationData(
            int id,
            String type,
            String message,
            LocalDateTime date,
            boolean read) {

        this.id = id;
        this.type = type;
        this.message = message;
        this.date = date;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isRead() {
        return read;
    }

    @Override
    public String toString() {
        return message;
    }
}