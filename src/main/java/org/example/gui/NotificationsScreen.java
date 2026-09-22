package org.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.example.client.NetworkClient;
import org.example.network.NotificationData;
import org.example.network.Response;

import java.io.IOException;
import java.util.List;

public class NotificationsScreen {

    public Node getView(Stage stage) {

        VBox root = new VBox(15);

        root.setPadding(
                new Insets(30)
        );

        root.setAlignment(
                Pos.TOP_CENTER
        );

        root.getStyleClass().add(
                "notification-root"
        );

        Label title =
                new Label("Notifications");

        title.getStyleClass().add(
                "login-title"
        );

        ListView<NotificationData> listView =
                new ListView<>();

        listView.setPrefHeight(400);
        listView.setPrefWidth(600);

        listView.setCellFactory(
                list -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            NotificationData notification,
                            boolean empty) {

                        super.updateItem(
                                notification,
                                empty
                        );

                        if (empty || notification == null) {

                            setText(null);
                            setGraphic(null);

                            return;
                        }

                        VBox box =
                                new VBox(5);

                        Label message =
                                new Label(
                                        notification.getMessage()
                                );

                        message.getStyleClass().add(
                                "notification-title"
                        );

                        Label date =
                                new Label(
                                        notification
                                                .getDate()
                                                .toString()
                                );

                        date.getStyleClass().add(
                                "notification-subtitle"
                        );

                        box.getChildren().addAll(
                                message,
                                date
                        );

                        box.getStyleClass().add(
                                "notification-card"
                        );

                        setGraphic(box);
                    }
                }
        );

        loadNotifications(listView);

        Button refreshButton =
                new Button("Refresh");

        refreshButton.getStyleClass().add(
                "login-button"
        );

        refreshButton.setOnAction(
                e -> loadNotifications(listView)
        );

        root.getChildren().addAll(
                title,
                listView,
                refreshButton
        );

        return root;
    }

    private void loadNotifications(
            ListView<NotificationData> listView) {

        if (Session.getCurrentUser() == null) {

            return;
        }

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            int userId =
                    Session.getCurrentUser()
                            .getId();

            Response response =
                    networkClient.getNotifications(
                            userId
                    );

            if (!response.isSuccess()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Notifications",
                        response.getMessage()
                );

                return;
            }

            @SuppressWarnings("unchecked")
            List<NotificationData> notifications =
                    (List<NotificationData>)
                            response.getData();

            listView.getItems().setAll(
                    notifications
            );

        } catch (
                IOException |
                ClassNotFoundException ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Notifications",
                    "Could not load notifications."
            );

        } finally {

            networkClient.disconnect();
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}