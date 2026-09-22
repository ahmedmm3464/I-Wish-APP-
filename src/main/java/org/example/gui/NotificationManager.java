 package org.example.gui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotificationManager {
    public static final ObservableList<String> notifications = FXCollections.observableArrayList();

    public static void addNotification(String message) {
        if (message == null || message.trim().isEmpty()) return;
        notifications.add(message);
    }

    public static void clearNotifications() {
        notifications.clear();
    }

    public static boolean hasNotifications() {
        return !notifications.isEmpty();
    }

    public static int getNotificationCount() {
        return notifications.size();
    }
}
