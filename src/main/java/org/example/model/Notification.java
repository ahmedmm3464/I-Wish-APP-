package org.example.model ;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private final Client recipient;
    private final NotificationType type;
    private String message;
    private LocalDateTime date;
    private boolean isRead;

    public Notification(Client recipient, NotificationType type, String message) {
        if (recipient == null) throw new IllegalArgumentException("A notification needs a recipient");
        if (type == null) throw new IllegalArgumentException("A notification needs a type");
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Message is required");
        this.recipient = recipient;
        this.type = type;
        this.message = message;
        this.date = LocalDateTime.now().withNano(0);
        this.isRead = false;
    }

    public Notification(int id, Client recipient, NotificationType type, String message, LocalDateTime date, boolean isRead) {
        this(recipient, type, message);
        this.id = id;
        this.date = date;
        this.isRead = isRead;
    }

    public void send() {
        this.date = LocalDateTime.now().withNano(0);
        this.isRead = false;
    }

    public void markAsRead() { this.isRead = true; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Client getRecipient() { return recipient; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public LocalDateTime getDate() { return date; }
    public boolean isRead() { return isRead; }

    @Override public String toString() { return "[" + type + (isRead ? ", read" : ", unread") + "] " + message; }
}
