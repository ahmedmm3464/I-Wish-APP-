package org.example.gui;

import org.example.client.NetworkClient;
import org.example.network.RegisterData;
import org.example.network.Response;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterScreen extends Application {

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

        Label slogan = new Label("Join us & make\nyour wishes come true ");
        slogan.getStyleClass().add("login-slogan");
        slogan.setAlignment(Pos.CENTER);

        Label description = new Label(
                "Create an account, share your dreams with friends,\nand start making wishes possible."
        );
        description.getStyleClass().add("login-description");
        description.setWrapText(true);
        description.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

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

        Label title = new Label("Create Account ♡");
        title.getStyleClass().add("login-title");

        Label subtitle = new Label("Sign up for a new I-Wish account");
        subtitle.getStyleClass().add("login-subtitle");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("login-field");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("login-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("login-field");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("login-field");

        Button registerButton = new Button("Register ♡");
        registerButton.getStyleClass().add("login-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        registerButton.setOnAction(e -> {

            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Registration",
                        "Please enter your username."
                );
                return;
            }

            if (email.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Registration",
                        "Please enter your email."
                );
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Registration",
                        "Please enter a valid email."
                );
                return;
            }

            if (password.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Registration",
                        "Please enter your password."
                );
                return;
            }

            if (password.length() < 6) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Registration",
                        "Password must be at least 6 characters."
                );
                return;
            }

            if (confirmPassword.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Registration",
                        "Please confirm your password."
                );
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Registration",
                        "Passwords do not match."
                );
                return;
            }

            NetworkClient networkClient = new NetworkClient();

            try {

                networkClient.connect();

                RegisterData data = new RegisterData(
                        username,
                        password,
                        email,
                        email
                );

                Response response = networkClient.register(data);

                networkClient.disconnect();

                if (response.isSuccess()) {

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Registration Successful",
                            response.getMessage()
                    );

                    new LoginScreen().start(stage);

                } else {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Registration Failed",
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
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.getStyleClass().add("register-button");
        backButton.setMaxWidth(Double.MAX_VALUE);

        backButton.setOnAction(e -> {
            try {
                new LoginScreen().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Could not open Login Screen."
                );
            }
        });

        Label footer = new Label("Your wishes deserve to come true ♡");
        footer.getStyleClass().add("login-footer");

        card.getChildren().addAll(
                title,
                subtitle,
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                registerButton,
                backButton,
                footer
        );

        StackPane right = new StackPane(card);
        right.getStyleClass().add("login-right");
        right.setPadding(new Insets(30));

        root.setLeft(left);
        root.setCenter(right);

        Scene scene = new Scene(root, 1050, 700);

        try {
            scene.getStylesheets().add(
                    getClass()
                            .getResource("/AppStyle.css")
                            .toExternalForm()
            );
        } catch (Exception ex) {
            System.out.println("AppStyle.css not found.");
        }

        stage.setTitle("I-Wish - Register");
        stage.setScene(scene);
        stage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}