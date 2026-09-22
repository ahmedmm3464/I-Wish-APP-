package org.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.example.client.NetworkClient;
import org.example.model.GiftItem;
import org.example.network.GiftItemData;
import org.example.network.Response;
import org.example.network.WishListData;

import java.util.List;

public class FriendWishListScreen {

    private final String friendName;
    private final int friendId;

    public FriendWishListScreen(
            String friendName,
            int friendId) {

        this.friendName = friendName;
        this.friendId = friendId;
    }

    public Node getView(Stage stage) {

        VBox card = new VBox(15);

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
                550
        );

        Label title =
                new Label(
                        friendName + "'s Wish List"
                );

        title.getStyleClass().add(
                "login-title"
        );

        // =====================================================
        // TABLE
        // =====================================================

        TableView<GiftItemData> table =
                new TableView<>();

        table.setPrefWidth(500);
        table.setPrefHeight(300);

        TableColumn<GiftItemData, String> itemColumn =
                new TableColumn<>("Wish");

        itemColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        itemColumn.setPrefWidth(280);

        TableColumn<GiftItemData, Double> priceColumn =
                new TableColumn<>("Price");

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        priceColumn.setPrefWidth(180);

        table.getColumns().addAll(
                itemColumn,
                priceColumn
        );

        // =====================================================
        // LOAD FRIEND WISHLIST
        // =====================================================

        loadWishList(table);

        // =====================================================
        // CONTRIBUTE BUTTON
        // =====================================================

        Button contributeButton =
                new Button("Contribute");

        contributeButton.getStyleClass().add(
                "login-button"
        );

        contributeButton.setMaxWidth(
                Double.MAX_VALUE
        );

        contributeButton.setOnAction(e -> {

            GiftItemData selectedItem =
                    table.getSelectionModel()
                            .getSelectedItem();

            if (selectedItem == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Contribute",
                        "Please select a wish first."
                );

                return;
            }

            if (selectedItem.getPrice() <= 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Contribute",
                        "This wish cannot receive contributions."
                );

                return;
            }

            // Convert GiftItemData -> GiftItem
            GiftItem giftItem =
                    new GiftItem(
                            selectedItem.getId(),
                            selectedItem.getName(),
                            selectedItem.getPrice()
                    );

            MainLayout.setContent(
                    new ContributionScreen(
                            friendName,
                            friendId,
                            giftItem
                    ).getView(stage)
            );
        });

        // =====================================================
        // ADD TO CARD
        // =====================================================

        card.getChildren().addAll(
                title,
                table,
                contributeButton
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
    // LOAD WISHLIST
    // =========================================================

    private void loadWishList(
            TableView<GiftItemData> table) {

        NetworkClient networkClient =
                new NetworkClient();

        try {

            networkClient.connect();

            Response response =
                    networkClient.getMyWishList(
                            friendId
                    );

            if (!response.isSuccess()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Wish List",
                        response.getMessage()
                );

                return;
            }

            WishListData wishList =
                    (WishListData) response.getData();

            if (wishList == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Wish List",
                        "This friend has no wish list."
                );

                return;
            }

            List<GiftItemData> items =
                    wishList.getItems();

            if (items != null) {

                table.getItems().setAll(
                        items
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Wish List",
                    "Failed to load friend's wish list."
            );

        } finally {

            networkClient.disconnect();
        }
    }

    // =========================================================
    // START
    // =========================================================

    public void start(Stage stage) {

        MainLayout.show(stage);

        MainLayout.setContent(
                getView(stage)
        );
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