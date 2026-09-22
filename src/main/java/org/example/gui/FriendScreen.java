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

import org.example.client.NetworkClient;
import org.example.network.AddFriendData;
import org.example.network.FriendData;
import org.example.network.FriendRequestData;
import org.example.network.Response;

import java.util.List;

public class FriendScreen extends Application {

    private final ObservableList<FriendData> friends =
            FXCollections.observableArrayList();

    private Label requestsCountLabel;

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
                "I Wish - Friends"
        );

        stage.setScene(scene);
        stage.show();
    }

    public BorderPane getView(Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("login-root");

        // =====================================================
        // LEFT BRAND
        // =====================================================

        VBox left = new VBox(15);

        left.getStyleClass().add(
                "login-brand"
        );

        left.setAlignment(
                Pos.CENTER
        );

        left.setPadding(
                new Insets(30)
        );

        left.setPrefWidth(320);

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
                        "Connect with friends\nshare your dreams ✨"
                );

        slogan.getStyleClass().add(
                "login-slogan"
        );

        slogan.setAlignment(
                Pos.CENTER
        );

        Label description =
                new Label(
                        "Manage your friends list, view their wish lists,\nand help make their wishes come true."
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

        // =====================================================
        // MAIN CARD
        // =====================================================

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

        // =====================================================
        // TITLE
        // =====================================================

        HBox titleBox =
                new HBox(15);

        titleBox.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label(
                        "My Friends ♡"
                );

        title.getStyleClass().add(
                "login-title"
        );

        requestsCountLabel =
                new Label(
                        "🔔 0"
                );

        requestsCountLabel.getStyleClass().add(
                "login-subtitle"
        );

        titleBox.getChildren().addAll(
                title,
                requestsCountLabel
        );

        // =====================================================
        // FRIEND LIST
        // =====================================================

        ListView<FriendData> friendList =
                new ListView<>(friends);

        friendList.setPrefWidth(
                460
        );

        friendList.setPrefHeight(
                260
        );

        friendList.setCellFactory(
                list -> new ListCell<>() {

                    private final Label nameLabel =
                            new Label();

                    private final Button viewButton =
                            new Button("View");

                    private final HBox box =
                            new HBox(10);

                    {
                        box.setAlignment(
                                Pos.CENTER_LEFT
                        );

                        nameLabel.setPrefWidth(
                                320
                        );

                        viewButton
                                .getStyleClass()
                                .add(
                                        "register-button"
                                );

                        viewButton.setPrefWidth(
                                90
                        );

                        // =================================================
                        // VIEW FRIEND WISHLIST
                        // =================================================

                        viewButton.setOnAction(e -> {

                            FriendData friend =
                                    getItem();

                            if (friend == null) {
                                return;
                            }

                            String displayName =
                                    friend.getName() != null &&
                                            !friend.getName().isBlank()
                                            ? friend.getName()
                                            : friend.getUsername();

                            try {

                                MainLayout.setContent(
                                        new FriendWishListScreen(
                                                displayName,
                                                friend.getId()
                                        ).getView(stage)
                                );

                            } catch (Exception ex) {

                                ex.printStackTrace();

                                showAlert(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "Could not open friend's wish list."
                                );
                            }
                        });

                        box.getChildren().addAll(
                                nameLabel,
                                viewButton
                        );
                    }

                    @Override
                    protected void updateItem(
                            FriendData friend,
                            boolean empty
                    ) {

                        super.updateItem(
                                friend,
                                empty
                        );

                        if (empty || friend == null) {

                            setText(null);
                            setGraphic(null);

                            return;
                        }

                        String displayName =
                                friend.getName() != null &&
                                        !friend.getName().isBlank()
                                        ? friend.getName()
                                        : friend.getUsername();

                        nameLabel.setText(
                                displayName
                        );

                        setText(null);
                        setGraphic(box);
                    }
                }
        );

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadFriends();

        loadFriendRequestsCount();

        // =====================================================
        // ADD FRIEND BUTTON
        // =====================================================

        Button addFriendButton =
                new Button(
                        "Add Friend ♡"
                );

        addFriendButton
                .getStyleClass()
                .add(
                        "login-button"
                );

        addFriendButton.setMaxWidth(
                Double.MAX_VALUE
        );

        addFriendButton.setOnAction(
                e -> openAddFriendWindow(stage)
        );

        VBox bottomButtons =
                new VBox(8);

        bottomButtons.getChildren().add(
                addFriendButton
        );

        // =====================================================
        // FOOTER
        // =====================================================

        Label footer =
                new Label(
                        "Your wishes deserve to come true ♡"
                );

        footer.getStyleClass().add(
                "login-footer"
        );

        card.getChildren().addAll(
                titleBox,
                friendList,
                bottomButtons,
                footer
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

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

    // =========================================================
    // ADD FRIEND WINDOW
    // =========================================================

    private void openAddFriendWindow(
            Stage parentStage
    ) {

        if (Session.getCurrentUser() == null) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No user logged in."
            );

            return;
        }

        Stage searchStage =
                new Stage();

        searchStage.setTitle(
                "Add Friend"
        );

        VBox root =
                new VBox(15);

        root.setPadding(
                new Insets(25)
        );

        root.setAlignment(
                Pos.TOP_CENTER
        );

        Label title =
                new Label(
                        "Find Friends"
                );

        title.getStyleClass().add(
                "login-title"
        );

        HBox searchBox =
                new HBox(10);

        searchBox.setAlignment(
                Pos.CENTER
        );

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search by username or name..."
        );

        searchField.setPrefWidth(
                300
        );

        Button searchButton =
                new Button(
                        "Search"
                );

        searchButton
                .getStyleClass()
                .add(
                        "login-button"
                );

        searchBox.getChildren().addAll(
                searchField,
                searchButton
        );

        ListView<FriendData> resultList =
                new ListView<>();

        resultList.setPrefWidth(
                450
        );

        resultList.setPrefHeight(
                300
        );

        searchButton.setOnAction(e -> {

            String query =
                    searchField
                            .getText()
                            .trim();

            if (query.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Search",
                        "Enter a name or username."
                );

                return;
            }

            searchUsers(
                    query,
                    resultList
            );
        });

        searchField.setOnAction(
                e -> searchButton.fire()
        );

        resultList.setCellFactory(
                list -> new ListCell<>() {

                    private final Label nameLabel =
                            new Label();

                    private final Button addButton =
                            new Button("Add");

                    private final HBox box =
                            new HBox(10);

                    {
                        box.setAlignment(
                                Pos.CENTER_LEFT
                        );

                        nameLabel.setPrefWidth(
                                300
                        );

                        addButton
                                .getStyleClass()
                                .add(
                                        "register-button"
                                );

                        addButton.setPrefWidth(
                                80
                        );

                        addButton.setOnAction(e -> {

                            FriendData user =
                                    getItem();

                            if (user == null) {
                                return;
                            }

                            sendFriendRequest(
                                    user,
                                    searchStage
                            );
                        });

                        box.getChildren().addAll(
                                nameLabel,
                                addButton
                        );
                    }

                    @Override
                    protected void updateItem(
                            FriendData user,
                            boolean empty
                    ) {

                        super.updateItem(
                                user,
                                empty
                        );

                        if (empty || user == null) {

                            setText(null);
                            setGraphic(null);

                            return;
                        }

                        String displayName =
                                user.getName() != null &&
                                        !user.getName().isBlank()
                                        ? user.getName()
                                        : user.getUsername();

                        nameLabel.setText(
                                displayName +
                                        "  @" +
                                        user.getUsername()
                        );

                        setText(null);
                        setGraphic(box);
                    }
                }
        );

        Button closeButton =
                new Button(
                        "Close"
                );

        closeButton
                .getStyleClass()
                .add(
                        "register-button"
                );

        closeButton.setOnAction(
                e -> searchStage.close()
        );

        root.getChildren().addAll(
                title,
                searchBox,
                resultList,
                closeButton
        );

        Scene scene =
                new Scene(
                        root,
                        520,
                        500
                );

        try {

            scene.getStylesheets().add(
                    getClass()
                            .getResource("/AppStyle.css")
                            .toExternalForm()
            );

        } catch (Exception ignored) {
        }

        searchStage.setScene(scene);
        searchStage.show();
    }

    // =========================================================
    // SEARCH USERS
    // =========================================================

    private void searchUsers(
            String query,
            ListView<FriendData> resultList
    ) {

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            Response response =
                    networkClient.searchUsers(
                            query,
                            Session.getCurrentUser().getId()
                    );

            if (!response.isSuccess()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Search",
                        response.getMessage()
                );

                return;
            }

            resultList.getItems().clear();

            if (response.getData() != null) {

                List<FriendData> users =
                        (List<FriendData>)
                                response.getData();

                int currentUserId =
                        Session.getCurrentUser().getId();

                users.removeIf(
                        user ->
                                user.getId() ==
                                        currentUserId
                );

                resultList.getItems()
                        .addAll(users);
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Connection Error",
                    "Could not search users."
            );

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // SEND FRIEND REQUEST
    // =========================================================

    private void sendFriendRequest(
            FriendData user,
            Stage searchStage
    ) {

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            AddFriendData data =
                    new AddFriendData(
                            Session.getCurrentUser().getId(),
                            user.getId()
                    );

            Response response =
                    networkClient.addFriend(
                            data
                    );

            if (response.isSuccess()) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Add Friend",
                        response.getMessage()
                );

                searchStage.close();

                loadFriendRequestsCount();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Add Friend",
                        response.getMessage()
                );
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Connection Error",
                    "Could not send friend request."
            );

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // LOAD FRIENDS
    // =========================================================

    private void loadFriends() {

        if (Session.getCurrentUser() == null) {
            return;
        }

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            Response response =
                    networkClient.getFriends(
                            Session.getCurrentUser().getId()
                    );

            if (!response.isSuccess()) {
                return;
            }

            friends.clear();

            if (response.getData() != null) {

                List<FriendData> list =
                        (List<FriendData>)
                                response.getData();

                friends.setAll(list);
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // LOAD FRIEND REQUEST COUNT
    // =========================================================

    private void loadFriendRequestsCount() {

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

                requestsCountLabel.setText(
                        "🔔 0"
                );

                return;
            }

            int count = 0;

            if (response.getData() != null) {

                List<FriendRequestData> list =
                        (List<FriendRequestData>)
                                response.getData();

                count = list.size();
            }

            requestsCountLabel.setText(
                    "🔔 " + count
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            requestsCountLabel.setText(
                    "🔔 0"
            );

        } finally {

            try {
                networkClient.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // ALERT
    // =========================================================

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

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        launch(args);
    }
}