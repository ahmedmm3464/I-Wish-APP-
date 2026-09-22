package org.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.example.client.NetworkClient;
import org.example.network.ClientData;
import org.example.model.GiftItem;
import org.example.network.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ContributionScreen {

    private final String friendName;
    private final int friendId;
    private final GiftItem wishItem;

    private static final Map<String, Double> contributedSoFar =
            new HashMap<>();

    public ContributionScreen(
            String friendName,
            int friendId,
            GiftItem wishItem) {

        this.friendName = friendName;
        this.friendId = friendId;
        this.wishItem = wishItem;
    }

    public Node getView(Stage stage) {

        VBox card = new VBox(14);

        card.getStyleClass().add("login-card");

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setPadding(
                new Insets(35)
        );

        card.setMaxWidth(460);

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("Contribute ♡");

        title.getStyleClass().add(
                "login-title"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle =
                new Label(
                        "To " + friendName + "'s Wish"
                );

        subtitle.getStyleClass().add(
                "login-subtitle"
        );

        // =====================================================
        // WISH
        // =====================================================

        Label wishLabel =
                new Label(
                        "Wish: " + wishItem.getName()
                );

        wishLabel.getStyleClass().add(
                "post-text"
        );

        // =====================================================
        // PRICE
        // =====================================================

        Label priceLabel =
                new Label(
                        String.format(
                                "Price: %.2f EGP",
                                wishItem.getPrice()
                        )
                );

        priceLabel.getStyleClass().add(
                "post-price"
        );

        // =====================================================
        // AMOUNT FIELD
        // =====================================================

        TextField amountField =
                new TextField();

        amountField.setPromptText(
                "Enter contribution amount"
        );

        amountField.getStyleClass().add(
                "login-field"
        );



        Button contributeButton =
                new Button("Contribute ♡");

        contributeButton.getStyleClass().add(
                "login-button"
        );

        contributeButton.setMaxWidth(
                Double.MAX_VALUE
        );

        contributeButton.setOnAction(e -> {

            // =================================================
            // CHECK LOGIN
            // =================================================

            ClientData currentUser = Session.getCurrentUser();
            if (currentUser == null) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "You must be logged in to contribute."
                );

                return;
            }

            // =================================================
            // GET AMOUNT
            // =================================================

            String text =
                    amountField
                            .getText()
                            .trim();

            if (text.isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Please enter an amount."
                );

                return;
            }

            double amount;

            try {

                amount =
                        Double.parseDouble(text);

            } catch (NumberFormatException ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Amount",
                        "Please enter a valid number."
                );

                return;
            }

            // =================================================
            // VALIDATE AMOUNT
            // =================================================

            if (amount <= 0) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Amount",
                        "Amount must be greater than 0."
                );

                return;
            }

            // =================================================
            // CHECK REMAINING AMOUNT
            // =================================================

            double wishPrice =
                    wishItem.getPrice();

            String contributionKey =
                    friendId + "_" + wishItem.getId();

            double alreadyContributed =
                    contributedSoFar.getOrDefault(
                            contributionKey,
                            0.0
                    );

            double remaining =
                    wishPrice - alreadyContributed;

            if (amount > remaining) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Contribution",
                        String.format(
                                "You can contribute at most %.2f EGP.",
                                remaining
                        )
                );

                return;
            }

            // =================================================
            // NETWORK
            // =================================================

            NetworkClient networkClient =
                    new NetworkClient();

            try {

                networkClient.connect();

                String contributorName =
                        currentUser.getName();

                // =================================================
                // NOTIFICATION FOR WISH OWNER
                // =================================================

                String ownerMessage =
                        contributorName
                                + " contributed "
                                + String.format(
                                "%.2f",
                                amount
                        )
                                + " EGP to your wish: "
                                + wishItem.getName();

                Response ownerResponse =
                        networkClient.addNotification(
                                friendId,
                                ownerMessage
                        );

                if (!ownerResponse.isSuccess()) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Notification Error",
                            ownerResponse.getMessage()
                    );

                    return;
                }

                // =================================================
                // NOTIFICATION FOR CONTRIBUTOR
                // =================================================

                String contributorMessage =
                        "You contributed "
                                + String.format(
                                "%.2f",
                                amount
                        )
                                + " EGP to "
                                + friendName
                                + "'s wish: "
                                + wishItem.getName();

                Response contributorResponse =
                        networkClient.addNotification(
                                currentUser.getId(),
                                contributorMessage
                        );

                if (!contributorResponse.isSuccess()) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Notification Error",
                            "Your contribution was sent, "
                                    + "but your confirmation notification "
                                    + "could not be created."
                    );

                    return;
                }

                // =================================================
                // UPDATE CONTRIBUTION ONLY AFTER SUCCESS
                // =================================================

                double newTotal =
                        alreadyContributed + amount;

                contributedSoFar.put(
                        contributionKey,
                        newTotal
                );

                // =================================================
                // SUCCESS
                // =================================================

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        String.format(
                                "You contributed %.2f EGP successfully!",
                                amount
                        )
                );

                MainLayout.showHome();

            } catch (
                    IOException |
                    ClassNotFoundException ex) {

                ex.printStackTrace();

                showAlert(
                        Alert.AlertType.ERROR,
                        "Connection Error",
                        "Could not complete the contribution."
                );

            } finally {

                networkClient.disconnect();
            }
        });

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

        // =====================================================
        // CARD
        // =====================================================

        card.getChildren().addAll(
                title,
                subtitle,
                wishLabel,
                priceLabel,
                amountField,
                contributeButton,
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

        return right;
    }

    // =========================================================
    // ALERT
    // =========================================================

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