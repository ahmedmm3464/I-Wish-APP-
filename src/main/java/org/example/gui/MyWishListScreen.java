package org.example.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.client.NetworkClient;
import org.example.network.GiftItemData;
import org.example.network.Response;
import org.example.network.WishListData;

import java.util.Optional;

public class MyWishListScreen {

    private final NetworkClient networkClient = new NetworkClient();

    private WishListData currentWishList;
    private TableView<GiftItemData> table;

    public Node getView(Stage stage) {

        VBox card = new VBox(15);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(30));
        card.setMaxWidth(520);

        Label title = new Label("My Wish List ♡");
        title.getStyleClass().add("login-title");

        table = new TableView<>();
        table.setPrefWidth(460);
        table.setPrefHeight(260);

        TableColumn<GiftItemData, String> itemColumn =
                new TableColumn<>("Wish");

        itemColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        itemColumn.setPrefWidth(260);

        TableColumn<GiftItemData, Double> priceColumn =
                new TableColumn<>("Price");

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        priceColumn.setPrefWidth(195);

        table.getColumns().addAll(itemColumn, priceColumn);

        Button addButton = new Button("Add Wish ♡");
        addButton.getStyleClass().add("login-button");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnAction(e -> addWish());

        Button updateButton = new Button("Update Wish");
        updateButton.getStyleClass().add("register-button");
        updateButton.setMaxWidth(Double.MAX_VALUE);
        updateButton.setOnAction(e -> updateWish());

        Button deleteButton = new Button("Delete Wish");
        deleteButton.getStyleClass().add("register-button");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setOnAction(e -> deleteWish());

        VBox buttonsBox = new VBox(8);
        buttonsBox.getChildren().addAll(
                addButton,
                updateButton,
                deleteButton
        );

        Label footer =
                new Label("Your wishes deserve to come true ♡");

        footer.getStyleClass().add("login-footer");

        card.getChildren().addAll(
                title,
                table,
                buttonsBox,
                footer
        );

        StackPane right = new StackPane(card);
        right.getStyleClass().add("login-right");
        right.setPadding(new Insets(20));

        loadWishList();

        return right;
    }

    private void loadWishList() {

        if (Session.getCurrentUser() == null) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No user logged in."
            );
            return;
        }

        try {

            networkClient.connect();

            Response response =
                    networkClient.getMyWishList(
                            Session.getCurrentUser().getId()
                    );

            if (!response.isSuccess()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "My Wish List",
                        response.getMessage()
                );
                return;
            }

            currentWishList =
                    (WishListData) response.getData();

            table.getItems().clear();

            if (currentWishList != null
                    && currentWishList.getItems() != null) {

                table.getItems().addAll(
                        currentWishList.getItems()
                );
            }

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "My Wish List",
                    "Could not load your wish list."
            );

            e.printStackTrace();

        } finally {
            networkClient.disconnect();
        }
    }

    private void addWish() {

        TextInputDialog itemDialog =
                new TextInputDialog();

        itemDialog.setTitle("Add Wish");
        itemDialog.setHeaderText(null);
        itemDialog.setContentText(
                "Enter wish name:"
        );

        Optional<String> itemResult =
                itemDialog.showAndWait();

        if (itemResult.isEmpty()) {
            return;
        }

        String itemName =
                itemResult.get().trim();

        if (itemName.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Add Wish",
                    "Please enter a wish name."
            );
            return;
        }

        TextInputDialog priceDialog =
                new TextInputDialog();

        priceDialog.setTitle("Add Wish");
        priceDialog.setHeaderText(null);
        priceDialog.setContentText(
                "Enter wish price:"
        );

        Optional<String> priceResult =
                priceDialog.showAndWait();

        if (priceResult.isEmpty()) {
            return;
        }

        double price;

        try {
            price =
                    Double.parseDouble(
                            priceResult.get().trim()
                    );
        } catch (NumberFormatException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Add Wish",
                    "Please enter a valid price."
            );
            return;
        }

        if (price <= 0) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Add Wish",
                    "Price must be greater than zero."
            );
            return;
        }

        try {

            networkClient.connect();

            if (currentWishList == null) {

                Response createResponse =
                        networkClient.createWishList(
                                Session.getCurrentUser().getId(),
                                "My Wishes"
                        );

                if (!createResponse.isSuccess()) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Create Wish List",
                            createResponse.getMessage()
                    );
                    return;
                }

                loadWishList();

                if (currentWishList == null) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Add Wish",
                            "Could not create your wish list."
                    );
                    return;
                }
            }

            Response giftResponse =
                    networkClient.addGiftItem(
                            itemName,
                            price
                    );

            if (!giftResponse.isSuccess()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Add Wish",
                        giftResponse.getMessage()
                );
                return;
            }

            GiftItemData giftItem =
                    (GiftItemData) giftResponse.getData();

            Response addResponse =
                    networkClient.addWishListItem(
                            currentWishList.getId(),
                            giftItem.getId()
                    );

            if (!addResponse.isSuccess()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Add Wish",
                        addResponse.getMessage()
                );
                return;
            }

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Add Wish",
                    "Wish added successfully."
            );

            loadWishList();

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Add Wish",
                    "Could not add the wish."
            );

            e.printStackTrace();

        } finally {
            networkClient.disconnect();
        }
    }

    private void updateWish() {

        GiftItemData selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Update Wish",
                    "Please select a wish first."
            );
            return;
        }

        TextInputDialog itemDialog =
                new TextInputDialog(
                        selected.getName()
                );

        itemDialog.setTitle("Update Wish");
        itemDialog.setHeaderText(null);
        itemDialog.setContentText(
                "Enter new wish name:"
        );

        Optional<String> itemResult =
                itemDialog.showAndWait();

        if (itemResult.isEmpty()) {
            return;
        }

        String newName =
                itemResult.get().trim();

        if (newName.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Update Wish",
                    "Wish name cannot be empty."
            );
            return;
        }

        TextInputDialog priceDialog =
                new TextInputDialog(
                        String.valueOf(
                                selected.getPrice()
                        )
                );

        priceDialog.setTitle("Update Wish");
        priceDialog.setHeaderText(null);
        priceDialog.setContentText(
                "Enter new price:"
        );

        Optional<String> priceResult =
                priceDialog.showAndWait();

        if (priceResult.isEmpty()) {
            return;
        }

        double newPrice;

        try {
            newPrice =
                    Double.parseDouble(
                            priceResult.get().trim()
                    );
        } catch (NumberFormatException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Update Wish",
                    "Please enter a valid price."
            );
            return;
        }

        if (newPrice <= 0) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Update Wish",
                    "Price must be greater than zero."
            );
            return;
        }

        try {

            networkClient.connect();

            Response response =
                    networkClient.updateGiftItem(
                            selected.getId(),
                            newName,
                            newPrice
                    );

            if (!response.isSuccess()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Update Wish",
                        response.getMessage()
                );
                return;
            }

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Update Wish",
                    "Wish updated successfully."
            );

            loadWishList();

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Update Wish",
                    "Could not update the wish."
            );

            e.printStackTrace();

        } finally {
            networkClient.disconnect();
        }
    }

    private void deleteWish() {

        GiftItemData selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Delete Wish",
                    "Please select a wish first."
            );
            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle("Delete Wish");
        confirmation.setHeaderText(null);
        confirmation.setContentText(
                "Are you sure you want to delete \""
                        + selected.getName()
                        + "\"?"
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        try {

            networkClient.connect();

            Response removeResponse =
                    networkClient.removeWishListItem(
                            currentWishList.getId(),
                            selected.getId()
                    );

            if (!removeResponse.isSuccess()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Delete Wish",
                        removeResponse.getMessage()
                );
                return;
            }

            Response deleteResponse =
                    networkClient.deleteGiftItem(
                            selected.getId()
                    );

            if (!deleteResponse.isSuccess()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Delete Wish",
                        "Wish removed from your list, "
                                + "but could not be deleted."
                );
                return;
            }

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Delete Wish",
                    "Wish deleted successfully."
            );

            loadWishList();

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Delete Wish",
                    "Could not delete the wish."
            );

            e.printStackTrace();

        } finally {
            networkClient.disconnect();
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}