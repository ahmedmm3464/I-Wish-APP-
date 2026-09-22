package org.example.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.example.network.FriendRequestData;
import org.example.client.NetworkClient;
import org.example.network.Response;

import java.util.List;

public class FriendRequestsScreen extends Application {

    private final ObservableList<FriendRequestData> requests =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        BorderPane root = getView(stage);

        Scene scene =
                new Scene(
                        root,
                        1050,
                        700
                );

        try {

            scene.getStylesheets().add(
                    getClass()
                            .getResource("/AppStyle.css")
                            .toExternalForm()
            );

        } catch (Exception ex) {

            System.out.println(
                    "AppStyle.css not found."
            );
        }

        stage.setTitle(
                "I Wish - Friend Requests"
        );

        stage.setScene(scene);
        stage.show();
    }

    public BorderPane getView(Stage stage) {

        BorderPane root = new BorderPane();

        root.getStyleClass().add(
                "login-root"
        );

        VBox left =
                new VBox(15);

        left.getStyleClass().add(
                "login-brand"
        );

        left.setAlignment(
                Pos.CENTER
        );

        left.setPadding(
                new Insets(30)
        );

        left.setPrefWidth(
                320
        );

        Label logo =
                new Label("I-Wish");

        logo.getStyleClass().add(
                "login-logo"
        );

        Label heart =
                new Label("♡");

        heart.getStyleClass().add(
                "login-heart"
        );

        Label slogan =
                new Label(
                        "Manage your requests\ngrow your circle ✨"
                );

        slogan.getStyleClass().add(
                "login-slogan"
        );

        slogan.setAlignment(
                Pos.CENTER
        );

        Label description =
                new Label(
                        "Review pending friend requests, accept them to connect,\nor clear the ones you don't want."
                );

        description.getStyleClass().add(
                "login-description"
        );

        description.setWrapText(true);

        description.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER
        );

        left.getChildren().addAll(
                logo,
                heart,
                slogan,
                description
        );

        VBox card =
                new VBox(15);

        card.getStyleClass().add(
                "login-card"
        );

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setPadding(
                new Insets(30)
        );

        card.setMaxWidth(
                520
        );

        Label title =
                new Label(
                        "Friend Requests ♡"
                );

        title.getStyleClass().add(
                "login-title"
        );

        ListView<FriendRequestData> requestList =
                new ListView<>();

        requestList.setItems(
                requests
        );

        requestList.setPrefHeight(
                260
        );

        requestList.setPrefWidth(
                460
        );

        requestList.setCellFactory(
                list ->
                        new ListCell<FriendRequestData>() {

                            private final Label nameLabel =
                                    new Label();

                            private final Button acceptButton =
                                    new Button("Accept");

                            private final Button deleteButton =
                                    new Button("Delete");

                            private final HBox box =
                                    new HBox(10);

                            {
                                box.setAlignment(
                                        Pos.CENTER_LEFT
                                );

                                nameLabel.setPrefWidth(
                                        260
                                );

                                acceptButton
                                        .getStyleClass()
                                        .add(
                                                "login-button"
                                        );

                                deleteButton
                                        .getStyleClass()
                                        .add(
                                                "register-button"
                                        );

                                acceptButton.setPrefWidth(
                                        85
                                );

                                deleteButton.setPrefWidth(
                                        85
                                );

                                acceptButton.setOnAction(e -> {

                                    FriendRequestData req =
                                            getItem();

                                    if (req == null) {
                                        return;
                                    }

                                    acceptRequest(req);
                                });

                                deleteButton.setOnAction(e -> {

                                    FriendRequestData req =
                                            getItem();

                                    if (req == null) {
                                        return;
                                    }

                                    declineRequest(req);
                                });

                                box.getChildren().addAll(
                                        nameLabel,
                                        acceptButton,
                                        deleteButton
                                );
                            }

                            @Override
                            protected void updateItem(
                                    FriendRequestData req,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        req,
                                        empty
                                );

                                if (empty || req == null) {

                                    setText(null);
                                    setGraphic(null);

                                    return;
                                }

                                String displayName =
                                        req.getSenderName() != null &&
                                                !req.getSenderName().isBlank()
                                                ? req.getSenderName()
                                                : req.getSenderUsername();

                                String username =
                                        req.getSenderUsername() != null &&
                                                !req.getSenderUsername().isBlank()
                                                ? " @" +
                                                req.getSenderUsername()
                                                : "";

                                nameLabel.setText(
                                        displayName +
                                                username
                                );

                                setText(null);
                                setGraphic(box);
                            }
                        }
        );

        loadRequests();

        Label footer =
                new Label(
                        "Your wishes deserve to come true ♡"
                );

        footer.getStyleClass().add(
                "login-footer"
        );

        card.getChildren().addAll(
                title,
                requestList,
                footer
        );

        StackPane right =
                new StackPane(card);

        right.getStyleClass().add(
                "login-right"
        );

        right.setPadding(
                new Insets(20)
        );

        root.setLeft(left);
        root.setCenter(right);

        return root;
    }

    private void loadRequests() {

        if (Session.getCurrentUser() == null) {
            return;
        }

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            Response response =
                    networkClient.getFriendRequests(
                            Session.getCurrentUser().getId()
                    );

            if (!response.isSuccess()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Friend Requests",
                        response.getMessage()
                );

                return;
            }

            requests.clear();

            if (response.getData() != null) {

                List<FriendRequestData> list =
                        (List<FriendRequestData>)
                                response.getData();

                requests.setAll(
                        list
                );
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Connection Error",
                    "Could not load friend requests."
            );

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    private void acceptRequest(
            FriendRequestData request
    ) {

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            Response response =
                    networkClient.acceptFriendRequest(
                            request.getId()
                    );

            if (!response.isSuccess()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Friend Request",
                        response.getMessage()
                );

                return;
            }

            String name =
                    request.getSenderName() != null &&
                            !request.getSenderName().isBlank()
                            ? request.getSenderName()
                            : request.getSenderUsername();

            NotificationManager.addNotification(
                    "You accepted friend request from "
                            + name + "!"
            );

            requests.remove(
                    request
            );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Friend Request",
                    response.getMessage()
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Connection Error",
                    "Could not accept friend request."
            );

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    private void declineRequest(
            FriendRequestData request
    ) {

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            Response response =
                    networkClient.declineFriendRequest(
                            request.getId()
                    );

            if (!response.isSuccess()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Friend Request",
                        response.getMessage()
                );

                return;
            }

            requests.remove(
                    request
            );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Friend Request",
                    response.getMessage()
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Connection Error",
                    "Could not decline friend request."
            );

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}