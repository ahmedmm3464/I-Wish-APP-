package org.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.model.GiftItem;

public class MainLayout {

    private static Stage stage;
    private static BorderPane root;
    private static VBox sidebar;

    public static void show(Stage primaryStage) {

        stage = primaryStage;

        root = new BorderPane();
        root.setId("home-root");

        createSidebar();

        root.setLeft(sidebar);

        showHome();

        Scene scene =
                new Scene(
                        root,
                        1000,
                        650
                );

        String css =
                MainLayout.class
                        .getResource("/AppStyle.css")
                        .toExternalForm();

        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setTitle("I-Wish");
        stage.show();
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private static void createSidebar() {

        Label title =
                new Label("I-Wish");

        title.getStyleClass().add(
                "sidebar-title"
        );

        Button homeButton =
                createButton("🏠  Home");

        Button friendsButton =
                createButton("👥  Friends");

        Button requestsButton =
                createButton("🔔  Friend Requests");

        Button wishesButton =
                createButton("❤️  My Wish List");

        Button notificationsButton =
                createButton("🔔  Notifications");

        Button logoutButton =
                createButton("🚪  Logout");

        homeButton.setOnAction(
                e -> showHome()
        );

        friendsButton.setOnAction(
                e -> showFriends()
        );

        requestsButton.setOnAction(
                e -> showFriendRequests()
        );

        wishesButton.setOnAction(
                e -> showMyWishList()
        );

        notificationsButton.setOnAction(
                e -> showNotifications()
        );

        logoutButton.setOnAction(
                e -> logout()
        );

        sidebar =
                new VBox(
                        10,
                        title,
                        homeButton,
                        friendsButton,
                        requestsButton,
                        wishesButton,
                        notificationsButton,
                        logoutButton
                );

        sidebar.setPadding(
                new Insets(25)
        );

        sidebar.setPrefWidth(
                230
        );

        sidebar.setAlignment(
                Pos.TOP_CENTER
        );

        sidebar.getStyleClass().add(
                "sidebar"
        );
    }

    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private static Button createButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefWidth(
                180
        );

        button.setPrefHeight(
                45
        );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.getStyleClass().add(
                "sidebar-button"
        );

        return button;
    }

    // =========================================================
    // CONTENT
    // =========================================================

    public static void setContent(
            Node content) {

        root.setCenter(content);
    }

    // =========================================================
    // HOME
    // =========================================================

    public static void showHome() {

        HomeScreen screen =
                new HomeScreen();

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // FRIENDS
    // =========================================================

    public static void showFriends() {

        FriendScreen screen =
                new FriendScreen();

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // FRIEND REQUESTS
    // =========================================================

    public static void showFriendRequests() {

        FriendRequestsScreen screen =
                new FriendRequestsScreen();

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // MY WISH LIST
    // =========================================================

    public static void showMyWishList() {

        MyWishListScreen screen =
                new MyWishListScreen();

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // NOTIFICATIONS
    // =========================================================

    public static void showNotifications() {

        NotificationsScreen screen =
                new NotificationsScreen();

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // FRIEND WISH LIST
    // =========================================================

    public static void showFriendWishList(
            String friendName,
            int friendId) {

        FriendWishListScreen screen =
                new FriendWishListScreen(
                        friendName,
                        friendId
                );

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // CONTRIBUTION
    // =========================================================

    public static void showContribution(
            String friendName,
            int friendId,
            GiftItem giftItem) {

        ContributionScreen screen =
                new ContributionScreen(
                        friendName,
                        friendId,
                        giftItem
                );

        setContent(
                screen.getView(stage)
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private static void logout() {

        Session.clear();

        LoginScreen loginScreen =
                new LoginScreen();

        loginScreen.start(stage);
    }

    // =========================================================
    // GET STAGE
    // =========================================================

    public static Stage getStage() {

        return stage;
    }
}