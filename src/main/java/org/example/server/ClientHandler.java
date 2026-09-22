package org.example.server;

import org.example.database.*;
import org.example.model.*;
import org.example.network.*;
import org.example.service.AuthenticationService;
import org.example.service.FriendRequestService;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    private final AuthenticationService authenticationService;
    private final FriendRequestDAO friendRequestDAO;
    private final UserDAO userDAO;
    private final WishListDAO wishListDAO = new WishListDAO();
    private final GiftItemDAO giftItemDAO = new GiftItemDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final Socket socket;
    private final FriendRequestService friendRequestService;

    public ClientHandler(
            AuthenticationService authenticationService,
            FriendRequestDAO friendRequestDAO,
            UserDAO userDAO,
            Socket socket) {

        this.friendRequestDAO = friendRequestDAO;
        this.userDAO = userDAO;
        this.socket = socket;
        this.friendRequestService = new FriendRequestService();
        this.authenticationService = authenticationService;
    }

    @Override
    public void run() {

        try (
                ObjectOutputStream output =
                        new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream input =
                        new ObjectInputStream(socket.getInputStream())
        ) {

            output.flush();

            System.out.println(
                    "ClientHandler started for: "
                            + socket.getInetAddress()
            );

            while (true) {

                Request request =
                        (Request) input.readObject();

                System.out.println(
                        "Request received: "
                                + request.getType()
                );

                Response response =
                        handleRequest(request);

                output.writeObject(response);
                output.flush();
            }

        } catch (EOFException e) {

            System.out.println("Client disconnected.");

        } catch (IOException | ClassNotFoundException | SQLException e) {

            e.printStackTrace();

        } finally {

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response handleRequest(Request request)
            throws SQLException {

        switch (request.getType()) {

            case REGISTER: {

                RegisterData data =
                        (RegisterData) request.getData();

                Client registerClient =
                        new Client(
                                data.getUsername(),
                                data.getPassword(),
                                data.getName(),
                                null
                        );

                boolean registered =
                        authenticationService.register(
                                registerClient
                        );

                if (!registered) {
                    return new Response(
                            false,
                            "Username already exists",
                            null
                    );
                }

                return new Response(
                        true,
                        "Registration successful",
                        null
                );
            }

            case LOGIN: {

                LoginData loginData =
                        (LoginData) request.getData();

                boolean success =
                        authenticationService.login(
                                loginData.getUsername(),
                                loginData.getPassword()
                        );

                if (!success) {
                    return new Response(
                            false,
                            "Invalid username or password",
                            null
                    );
                }

                Client client =
                        authenticationService.findClient(
                                loginData.getUsername()
                        );

                ClientData clientData =
                        new ClientData(
                                client.getId(),
                                client.getUsername(),
                                client.getName(),
                                client.getDateOfBirth()
                        );

                return new Response(
                        true,
                        "Login successful",
                        clientData
                );
            }

            case ADD_FRIEND: {

                AddFriendData friendData =
                        (AddFriendData) request.getData();

                try {

                    String message =
                            friendRequestService.addFriend(
                                    friendData.getSenderId(),
                                    friendData.getReceiverId()
                            );

                    return new Response(
                            true,
                            message,
                            null
                    );

                } catch (SQLException e) {

                    e.printStackTrace();

                    return new Response(
                            false,
                            "Database error",
                            null
                    );
                }
            }

            case GET_FRIENDS: {

                int clientId =
                        (Integer) request.getData();

                try {

                    List<Client> friends =
                            friendRequestDAO.findFriends(
                                    clientId
                            );

                    List<FriendData> friendDataList =
                            new ArrayList<>();

                    for (Client friend : friends) {

                        friendDataList.add(
                                new FriendData(
                                        friend.getId(),
                                        friend.getUsername(),
                                        friend.getName(),
                                        friend.getDateOfBirth()
                                )
                        );
                    }

                    return new Response(
                            true,
                            "Friends loaded successfully",
                            friendDataList
                    );

                } catch (SQLException e) {

                    e.printStackTrace();

                    return new Response(
                            false,
                            "Could not load friends",
                            null
                    );
                }
            }

            case GET_FRIEND_REQUESTS: {

                int receiverId =
                        (Integer) request.getData();

                try {

                    List<FriendRequest> requests =
                            friendRequestDAO
                                    .findPendingForReceiver(
                                            receiverId
                                    );

                    List<FriendRequestData> requestDataList =
                            new ArrayList<>();

                    for (FriendRequest requestItem : requests) {

                        Client sender =
                                requestItem.getSender();

                        requestDataList.add(
                                new FriendRequestData(
                                        requestItem.getId(),
                                        sender.getId(),
                                        sender.getUsername(),
                                        sender.getName(),
                                        requestItem.getDateCreated()
                                )
                        );
                    }

                    return new Response(
                            true,
                            "Friend requests loaded successfully",
                            requestDataList
                    );

                } catch (SQLException e) {

                    e.printStackTrace();

                    return new Response(
                            false,
                            "Could not load friend requests",
                            null
                    );
                }
            }

            case ACCEPT_FRIEND_REQUEST: {

                int acceptId =
                        (Integer) request.getData();

                try {

                    boolean accepted =
                            friendRequestDAO.accept(
                                    acceptId
                            );

                    if (accepted) {

                        return new Response(
                                true,
                                "Friend request accepted successfully.",
                                null
                        );
                    }

                    return new Response(
                            false,
                            "Friend request could not be accepted.",
                            null
                    );

                } catch (SQLException e) {

                    e.printStackTrace();

                    return new Response(
                            false,
                            "Database error.",
                            null
                    );
                }
            }

            case DECLINE_FRIEND_REQUEST: {

                int declineId =
                        (Integer) request.getData();

                try {

                    boolean declined =
                            friendRequestDAO.decline(
                                    declineId
                            );

                    if (declined) {

                        return new Response(
                                true,
                                "Friend request declined successfully.",
                                null
                        );
                    }

                    return new Response(
                            false,
                            "Friend request could not be declined.",
                            null
                    );

                } catch (SQLException e) {

                    e.printStackTrace();

                    return new Response(
                            false,
                            "Database error.",
                            null
                    );
                }
            }

            case SEARCH_USERS: {

                SearchData searchData =
                        (SearchData) request.getData();

                try {

                    List<Client> users =
                            userDAO.searchUsers(
                                    searchData.getQuery(),
                                    searchData.getCurrentUserId()
                            );

                    List<FriendData> result =
                            new ArrayList<>();

                    for (Client user : users) {

                        result.add(
                                new FriendData(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getName(),
                                        user.getDateOfBirth()
                                )
                        );
                    }

                    return new Response(
                            true,
                            "Users found",
                            result
                    );

                } catch (SQLException e) {

                    e.printStackTrace();

                    return new Response(
                            false,
                            "Database error",
                            null
                    );
                }
            }

            case GET_MY_WISH_LIST: {

                int userId =
                        (Integer) request.getData();

                WishList wishList =
                        wishListDAO.findByUser(userId);

                if (wishList == null) {

                    return new Response(
                            true,
                            "No wish list found",
                            null
                    );
                }

                List<GiftItemData> items =
                        wishList.getItems()
                                .stream()
                                .map(item ->
                                        new GiftItemData(
                                                item.getId(),
                                                item.getName(),
                                                item.getPrice()
                                        )
                                )
                                .toList();

                WishListData wishListData =
                        new WishListData(
                                wishList.getId(),
                                wishList.getName(),
                                items
                        );

                return new Response(
                        true,
                        "Wish list loaded",
                        wishListData
                );
            }

            case CREATE_WISH_LIST: {

                CreateWishListData data =
                        (CreateWishListData) request.getData();

                Client owner =
                        userDAO.findById(
                                data.getUserId()
                        );

                if (owner == null) {

                    return new Response(
                            false,
                            "User not found",
                            null
                    );
                }

                WishList wishList =
                        new WishList(
                                data.getName(),
                                owner
                        );

                boolean created =
                        wishListDAO.create(
                                wishList
                        );

                return new Response(
                        created,
                        created
                                ? "Wish list created"
                                : "Wish list already exists",
                        null
                );
            }

            case ADD_GIFT_ITEM: {

                AddGiftItemData data =
                        (AddGiftItemData) request.getData();

                GiftItem item =
                        new GiftItem(
                                data.getName(),
                                data.getPrice()
                        );

                boolean added =
                        giftItemDAO.add(item);

                GiftItemData result =
                        added
                                ? new GiftItemData(
                                item.getId(),
                                item.getName(),
                                item.getPrice()
                        )
                                : null;

                return new Response(
                        added,
                        added
                                ? "Gift item added"
                                : "Could not add gift item",
                        result
                );
            }

            case ADD_WISH_LIST_ITEM: {

                WishListItemData data =
                        (WishListItemData) request.getData();

                boolean added =
                        wishListDAO.addItem(
                                data.getWishListId(),
                                data.getGiftItemId()
                        );

                return new Response(
                        added,
                        added
                                ? "Item added to wish list"
                                : "Item already exists",
                        null
                );
            }

            case REMOVE_WISH_LIST_ITEM: {

                WishListItemData data =
                        (WishListItemData) request.getData();

                boolean removed =
                        wishListDAO.removeItem(
                                data.getWishListId(),
                                data.getGiftItemId()
                        );

                return new Response(
                        removed,
                        removed
                                ? "Item removed"
                                : "Item not found",
                        null
                );
            }

            case UPDATE_GIFT_ITEM: {

                UpdateGiftItemData data =
                        (UpdateGiftItemData) request.getData();

                GiftItem item =
                        giftItemDAO.findById(
                                data.getId()
                        );

                if (item == null) {

                    return new Response(
                            false,
                            "Gift item not found",
                            null
                    );
                }

                item.setName(data.getName());
                item.setPrice(data.getPrice());

                boolean updated =
                        giftItemDAO.update(item);

                return new Response(
                        updated,
                        updated
                                ? "Gift item updated"
                                : "Could not update gift item",
                        null
                );
            }

            case DELETE_GIFT_ITEM: {

                int id =
                        (Integer) request.getData();

                boolean deleted =
                        giftItemDAO.delete(id);

                return new Response(
                        deleted,
                        deleted
                                ? "Gift item deleted"
                                : "Gift item not found",
                        null
                );
            }
            case ADD_NOTIFICATION: {

                AddNotificationData data =
                        (AddNotificationData) request.getData();

                Client recipient =
                        userDAO.findById(
                                data.getRecipientId()
                        );

                if (recipient == null) {

                    return new Response(
                            false,
                            "Recipient not found",
                            null
                    );
                }

                Notification notification =
                        new Notification(
                                recipient,
                                NotificationType.ITEM_BOUGHT,
                                data.getMessage()
                        );

                boolean created =
                        notificationDAO.create(
                                notification
                        );

                return new Response(
                        created,
                        created
                                ? "Notification created"
                                : "Could not create notification",
                        null
                );
            }

            case GET_NOTIFICATIONS: {

                int userId =
                        (Integer) request.getData();

                List<Notification> notifications =
                        notificationDAO.getUserNotifications(userId);

                List<NotificationData> notificationData =
                        notifications.stream()
                                .map(notification ->
                                        new NotificationData(
                                                notification.getId(),
                                                notification.getType().name(),
                                                notification.getMessage(),
                                                notification.getDate(),
                                                notification.isRead()
                                        )
                                )
                                .toList();

                return new Response(
                        true,
                        "Notifications loaded",
                        notificationData
                );
            }

            default:

                return new Response(
                        false,
                        "Unknown request",
                        null
                );
        }
    }
}