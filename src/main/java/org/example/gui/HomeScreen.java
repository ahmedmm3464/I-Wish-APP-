package org.example.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.GiftItem;

import java.io.File;
import java.io.InputStream;

public class HomeScreen extends Application {

    private final String PURPLE = "#4035D6";
    private final String LIGHT_PURPLE = "#9A6CFF";
    private final String BACKGROUND = "#F6F7FC";
    private final String PINK = "#FFD9EB";

    @Override
    public void start(Stage stage) {

        BorderPane root =
                getView(stage);

        Scene scene =
                new Scene(
                        root,
                        1250,
                        750
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

        stage.setTitle("I-Wish");
        stage.setScene(scene);
        stage.show();
    }

    public BorderPane getView(Stage stage) {

        BorderPane root =
                new BorderPane();

        root.setId("home-root");

        VBox feed =
                createFeed(stage);

        VBox rightBar =
                createRightBar();

        root.setCenter(feed);
        root.setRight(rightBar);

        return root;
    }

    // =========================================================
    // FEED
    // =========================================================

    private VBox createFeed(Stage stage) {

        VBox feed =
                new VBox();

        feed.getStyleClass().add(
                "feed"
        );

        feed.setPadding(
                new Insets(
                        0,
                        25,
                        30,
                        25
                )
        );

        HBox header =
                new HBox(10);

        header.getStyleClass().add(
                "feed-header"
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        Label title =
                new Label("Home");

        title.getStyleClass().add(
                "feed-title"
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        Button createPostButton =
                new Button("＋  Create Post");

        createPostButton
                .getStyleClass()
                .add(
                        "create-post-button"
                );

        header.getChildren().addAll(
                title,
                headerSpacer,
                createPostButton
        );

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setFitToWidth(true);

        scrollPane.getStyleClass().add(
                "feed-scroll"
        );

        VBox posts =
                new VBox(12);

        posts.getStyleClass()
                .add(
                        "posts-container"
                );

        createPostButton.setOnAction(
                e -> showCreatePostDialog(
                        stage,
                        posts
                )
        );

        posts.getChildren().addAll(

                createPost(
                        stage,
                        "سارة",
                        "Make Your Wish Come True ✨",
                        "4 hours ago",
                        "Perfume",
                        2000,
                        "Images/images (5).jpg"
                ),

                createPost(
                        stage,
                        "رنا",
                        "وقل رب زدني علماً ♡",
                        "3 hours ago",
                        "Laptop",
                        30000,
                        "Images/images (6).jpg"
                ),

                createPost(
                        stage,
                        "سما",
                        "Dream Big ♡",
                        "5 hours ago",
                        "Travel",
                        5000,
                        "Images/images (7).jpg"
                ),

                createPost(
                        stage,
                        "مريم",
                        "Shopping Lover ♡",
                        "6 hours ago",
                        "Bag",
                        4000,
                        "Images/images (8).jpg"
                ),

                createPost(
                        stage,
                        "نور",
                        "Music Lover ♡",
                        "7 hours ago",
                        "Headphones",
                        3000,
                        "Images/images (9).jpg"
                ),

                createPost(
                        stage,
                        "سارة",
                        "Lifestyle Lover ♡",
                        "8 hours ago",
                        "Smart Watch",
                        5000,
                        "Images/images.png"
                ),

                createPost(
                        stage,
                        "ملك",
                        "Beauty Lover ♡",
                        "9 hours ago",
                        "Perfume",
                        2500,
                        "Images/images (10).jpg"
                ),

                createPost(
                        stage,
                        "ياسمين",
                        "Fashion Lover ♡",
                        "10 hours ago",
                        "Bag",
                        5000,
                        "Images/images (11).jpg"
                ),

                createPost(
                        stage,
                        "آية",
                        "Student ♡",
                        "11 hours ago",
                        "Laptop",
                        30000,
                        "Images/images (12).jpg"
                ),

                createPost(
                        stage,
                        "جنى",
                        "Music Lover ♡",
                        "12 hours ago",
                        "Headphones",
                        3000,
                        "Images/images (13).jpg"
                ),

                createPost(
                        stage,
                        "ليان",
                        "Fashion Lover ♡",
                        "13 hours ago",
                        "Bag",
                        5000,
                        "Images/images (14).jpg"
                )
        );

        scrollPane.setContent(
                posts
        );

        feed.getChildren().addAll(
                header,
                scrollPane
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        return feed;
    }

    // =========================================================
    // CREATE POST DIALOG
    // =========================================================

    private void showCreatePostDialog(
            Stage stage,
            VBox posts) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Create New Post"
        );

        dialog.setHeaderText(
                "Create a new wish post"
        );

        ButtonType postButton =
                new ButtonType(
                        "Post",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        postButton,
                        ButtonType.CANCEL
                );

        VBox content =
                new VBox(12);

        content.setPadding(
                new Insets(10)
        );

        TextField usernameField =
                new TextField("سارة");

        usernameField.setPromptText(
                "Your name"
        );

        TextArea textArea =
                new TextArea();

        textArea.setPromptText(
                "What is your wish?"
        );

        textArea.setWrapText(true);

        textArea.setPrefRowCount(4);

        TextField itemField =
                new TextField();

        itemField.setPromptText(
                "Wish item e.g. Laptop"
        );

        TextField priceField =
                new TextField();

        priceField.setPromptText(
                "Price"
        );

        Label imageLabel =
                new Label(
                        "No image selected"
                );

        Button chooseImageButton =
                new Button(
                        "Choose Image"
                );

        final File[] selectedImage =
                new File[1];

        chooseImageButton.setOnAction(e -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Choose Post Image"
            );

            fileChooser
                    .getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Image Files",
                                    "*.png",
                                    "*.jpg",
                                    "*.jpeg",
                                    "*.gif"
                            )
                    );

            File file =
                    fileChooser.showOpenDialog(
                            stage
                    );

            if (file != null) {

                selectedImage[0] = file;

                imageLabel.setText(
                        file.getName()
                );
            }
        });

        content.getChildren().addAll(
                new Label("Name"),
                usernameField,
                new Label("Post"),
                textArea,
                new Label("Wish Item"),
                itemField,
                new Label("Price"),
                priceField,
                chooseImageButton,
                imageLabel
        );

        dialog.getDialogPane()
                .setContent(content);

        Button realPostButton =
                (Button) dialog
                        .getDialogPane()
                        .lookupButton(
                                postButton
                        );

        realPostButton.addEventFilter(
                javafx.event.ActionEvent.ACTION,
                event -> {

                    String username =
                            usernameField
                                    .getText()
                                    .trim();

                    String text =
                            textArea
                                    .getText()
                                    .trim();

                    String item =
                            itemField
                                    .getText()
                                    .trim();

                    String priceText =
                            priceField
                                    .getText()
                                    .trim();

                    if (username.isEmpty()) {

                        showError(
                                "Please enter your name."
                        );

                        event.consume();

                        return;
                    }

                    if (text.isEmpty()) {

                        showError(
                                "Please write your post."
                        );

                        event.consume();

                        return;
                    }

                    double price = 0;

                    if (!priceText.isEmpty()) {

                        try {

                            price =
                                    Double.parseDouble(
                                            priceText
                                    );

                            if (price < 0) {

                                showError(
                                        "Price cannot be negative."
                                );

                                event.consume();

                                return;
                            }

                        } catch (
                                NumberFormatException ex) {

                            showError(
                                    "Please enter a valid price."
                            );

                            event.consume();

                            return;
                        }
                    }

                    String imagePath = null;

                    if (selectedImage[0] != null) {

                        imagePath =
                                selectedImage[0]
                                        .toURI()
                                        .toString();
                    }

                    VBox newPost =
                            createPost(
                                    stage,
                                    username,
                                    text,
                                    "Just now",
                                    item.isEmpty()
                                            ? "Wish"
                                            : item,
                                    price,
                                    imagePath
                            );

                    posts.getChildren().add(
                            0,
                            newPost
                    );

                    dialog.close();
                }
        );

        dialog.showAndWait();
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Create Post"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    // =========================================================
    // CREATE POST
    // =========================================================

    private VBox createPost(
            Stage stage,
            String username,
            String text,
            String time,
            String itemName,
            double price,
            String imageName) {

        VBox post =
                new VBox(10);

        post.getStyleClass()
                .add("post");

        HBox userRow =
                new HBox(10);

        userRow.setAlignment(
                Pos.CENTER_LEFT
        );

        String avatarText =
                username.isEmpty()
                        ? "?"
                        : username.substring(0, 1);

        Label avatar =
                new Label(
                        avatarText
                );

        avatar.getStyleClass()
                .add("avatar");

        VBox userInfo =
                new VBox(2);

        Label name =
                new Label(
                        username
                );

        name.getStyleClass()
                .add("post-name");

        Label timeLabel =
                new Label(
                        time
                );

        timeLabel.getStyleClass()
                .add("post-time");

        userInfo.getChildren().addAll(
                name,
                timeLabel
        );

        userRow.getChildren().addAll(
                avatar,
                userInfo
        );

        Label postText =
                new Label(
                        text
                );

        postText.getStyleClass()
                .add("post-text");

        postText.setWrapText(true);

        ImageView imageView =
                createImage(
                        imageName
                );

        HBox imageContainer =
                new HBox(
                        imageView
                );

        imageContainer.setAlignment(
                Pos.CENTER
        );

        imageContainer.setMaxWidth(
                Double.MAX_VALUE
        );

        if (imageName == null
                || imageName.trim().isEmpty()
                || imageView.getImage() == null) {

            imageContainer.setManaged(false);
            imageContainer.setVisible(false);
        }

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_LEFT
        );

        Button likeButton =
                new Button("♡  Like");

        Button commentButton =
                new Button("💬  Comment");

        Button contributeButton =
                new Button("🎁  Contribute");

        likeButton.getStyleClass()
                .add("post-button");

        commentButton.getStyleClass()
                .add("post-button");

        contributeButton.getStyleClass()
                .add("contribute-button");

        // =====================================================
        // LIKE
        // =====================================================

        likeButton.setOnAction(e -> {

            if (likeButton
                    .getText()
                    .startsWith("♡")) {

                likeButton.setText(
                        "♥  Liked"
                );

            } else {

                likeButton.setText(
                        "♡  Like"
                );
            }
        });

        // =====================================================
        // COMMENT
        // =====================================================

        commentButton.setOnAction(e -> {

            TextInputDialog dialog =
                    new TextInputDialog();

            dialog.setTitle(
                    "Comment"
            );

            dialog.setHeaderText(
                    "Add a comment"
            );

            dialog.setContentText(
                    "Comment:"
            );

            dialog.showAndWait()
                    .ifPresent(comment -> {

                        if (!comment
                                .trim()
                                .isEmpty()) {

                            Label commentLabel =
                                    new Label(
                                            "You: "
                                                    + comment
                                                    .trim()
                                    );

                            commentLabel
                                    .getStyleClass()
                                    .add(
                                            "comment-label"
                                    );

                            post.getChildren()
                                    .add(
                                            commentLabel
                                    );
                        }
                    });
        });

        // =====================================================
        // CONTRIBUTE
        // =====================================================

        contributeButton.setOnAction(e -> {

            showError(
                    "Please open the friend's Wish List " +
                            "to make a contribution."
            );
        });

        actions.getChildren().addAll(
                likeButton,
                commentButton,
                contributeButton
        );

        post.getChildren().addAll(
                userRow,
                postText,
                imageContainer,
                new Separator(),
                actions
        );

        return post;
    }

    // =========================================================
    // IMAGE
    // =========================================================

    private ImageView createImage(
            String imageName) {

        ImageView imageView =
                new ImageView();

        if (imageName == null
                || imageName.trim().isEmpty()) {

            return imageView;
        }

        try {

            if (imageName.startsWith("file:")) {

                Image image =
                        new Image(
                                imageName,
                                false
                        );

                imageView.setImage(
                        image
                );

            } else {

                try (
                        InputStream in =
                                getClass()
                                        .getResourceAsStream(
                                                "/" + imageName
                                        )
                ) {

                    if (in == null) {

                        System.out.println(
                                "IMAGE NOT FOUND: /"
                                        + imageName
                        );

                        return imageView;
                    }

                    imageView.setImage(
                            new Image(in)
                    );
                }
            }

            imageView.setFitWidth(
                    500
            );

            imageView.setFitHeight(
                    280
            );

            imageView.setPreserveRatio(
                    true
            );

            imageView.getStyleClass()
                    .add(
                            "post-image"
                    );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return imageView;
    }

    // =========================================================
    // RIGHT BAR
    // =========================================================

    private VBox createRightBar() {

        VBox rightBar =
                new VBox(18);

        rightBar.getStyleClass()
                .add("right-bar");

        rightBar.setPadding(
                new Insets(
                        25,
                        20,
                        25,
                        5
                )
        );

        rightBar.setPrefWidth(
                285
        );

        // =====================================================
        // SEARCH
        // =====================================================

        VBox searchBox =
                new VBox();

        searchBox.getStyleClass()
                .add("side-card");

        TextField search =
                new TextField();

        search.setPromptText(
                "Search I-Wish"
        );

        search.getStyleClass()
                .add("search-field");

        searchBox.getChildren()
                .add(search);

        // =====================================================
        // TRENDING
        // =====================================================

        VBox trending =
                new VBox(12);

        trending.getStyleClass()
                .add("side-card");

        Label trendingTitle =
                new Label(
                        "Trending Wishes"
                );

        trendingTitle.getStyleClass()
                .add("side-title");

        Label trend1 =
                new Label(
                        "🎁 #BirthdayWishes"
                );

        Label trend2 =
                new Label(
                        "💻 #NewLaptop"
                );

        Label trend3 =
                new Label(
                        "✈ #TravelDreams"
                );

        Label trend4 =
                new Label(
                        "💄 #BeautyLover"
                );

        trend1.getStyleClass()
                .add("trend");

        trend2.getStyleClass()
                .add("trend");

        trend3.getStyleClass()
                .add("trend");

        trend4.getStyleClass()
                .add("trend");

        trending.getChildren().addAll(
                trendingTitle,
                trend1,
                trend2,
                trend3,
                trend4
        );

        // =====================================================
        // SUGGESTIONS
        // =====================================================

        VBox suggestions =
                new VBox(12);

        suggestions.getStyleClass()
                .add("side-card");

        Label suggestionTitle =
                new Label(
                        "Who to follow"
                );

        suggestionTitle.getStyleClass()
                .add("side-title");

        Label person1 =
                new Label(
                        "سارة\nLifestyle Lover"
                );

        Label person2 =
                new Label(
                        "رنا\nStudent ♡"
                );

        Label person3 =
                new Label(
                        "سما\nDream Big ♡"
                );

        person1.getStyleClass()
                .add("suggestion");

        person2.getStyleClass()
                .add("suggestion");

        person3.getStyleClass()
                .add("suggestion");

        suggestions.getChildren().addAll(
                suggestionTitle,
                person1,
                person2,
                person3
        );

        rightBar.getChildren().addAll(
                searchBox,
                trending,
                suggestions
        );

        return rightBar;
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        launch(args);
    }
}