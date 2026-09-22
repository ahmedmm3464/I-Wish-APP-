package org.example.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.example.client.NetworkClient;
import org.example.network.ClientData;
import org.example.network.Response;

public class LoginScreen extends Application {

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("login-root");

        VBox left = new VBox(15);
        left.getStyleClass().add("login-brand");
        left.setAlignment(Pos.CENTER);
        left.setPadding(new Insets(40));

        Label logo = new Label("I-Wish");
        logo.getStyleClass().add("login-logo");

        Label heart = new Label("♡");
        heart.getStyleClass().add("login-heart");

        Label slogan = new Label("Make your wishes\ncome true ✨");
        slogan.getStyleClass().add("login-slogan");
        slogan.setAlignment(Pos.CENTER);

        Label description = new Label(
                "Share your wishes, discover your friends' dreams\nand make every wish possible."
        );
        description.getStyleClass().add("login-description");
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

        VBox card = new VBox(12);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(35));
        card.setMaxWidth(430);

        Label title = new Label("Welcome Back ♡");
        title.getStyleClass().add("login-title");

        Label subtitle =
                new Label("Login to your I-Wish account");

        subtitle.getStyleClass().add("login-subtitle");

        TextField usernameField =
                new TextField();

        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("login-field");

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("login-field");

        Button loginButton =
                new Button("Login ♡");

        loginButton.getStyleClass()
                .add("login-button");

        loginButton.setMaxWidth(
                Double.MAX_VALUE
        );

        loginButton.setOnAction(e -> {

            String username =
                    usernameField.getText().trim();

            String password =
                    passwordField.getText();

            if (username.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Login",
                        "Please enter your username."
                );

                return;
            }

            if (password.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Login",
                        "Please enter your password."
                );

                return;
            }

            NetworkClient client =
                    new NetworkClient();

            try {

                client.connect();

                Response response =
                        client.login(
                                username,
                                password
                        );

                if (response.isSuccess()) {

                    ClientData loggedInUser =
                            (ClientData) response.getData();

                    Session.setCurrentUser(
                            loggedInUser
                    );

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Login",
                            response.getMessage()
                    );

                    MainLayout.show(stage);

                } else {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Login Failed",
                            response.getMessage()
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert(
                        Alert.AlertType.ERROR,
                        "Connection Error",
                        "Could not connect to the server."
                );

            } finally {

                try {
                    client.disconnect();
                } catch (Exception ignored) {
                }
            }
        });

        Button registerButton =
                new Button("Create Account");

        registerButton
                .getStyleClass()
                .add("register-button");

        registerButton.setMaxWidth(
                Double.MAX_VALUE
        );

        registerButton.setOnAction(e -> {

            try {

                new RegisterScreen()
                        .start(stage);

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Could not open Register Screen."
                );
            }
        });

        Label footer =
                new Label(
                        "Your wishes deserve to come true ♡"
                );

        footer.getStyleClass()
                .add("login-footer");

        card.getChildren().addAll(
                title,
                subtitle,
                usernameField,
                passwordField,
                loginButton,
                registerButton,
                footer
        );

        StackPane right =
                new StackPane(card);

        right.getStyleClass()
                .add("login-right");

        right.setPadding(
                new Insets(30)
        );

        root.setLeft(left);
        root.setCenter(right);

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
                "I-Wish - Login"
        );

        stage.setScene(scene);
        stage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}