package org.example.client;

import org.example.network.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {

    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public NetworkClient() {
        this("localhost", 5000);
    }

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);

        output =
                new ObjectOutputStream(
                        socket.getOutputStream()
                );

        output.flush();

        input =
                new ObjectInputStream(
                        socket.getInputStream()
                );
    }

    public boolean isConnected() {

        return socket != null
                && socket.isConnected()
                && !socket.isClosed()
                && output != null
                && input != null;
    }

    private void ensureConnected()
            throws IOException {

        if (!isConnected()) {
            connect();
        }
    }

    public Response login(
            String username,
            String password)
            throws IOException, ClassNotFoundException {

        LoginData data =
                new LoginData(
                        username,
                        password
                );

        Request request =
                new Request(
                        RequestType.LOGIN,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response register(
            RegisterData data)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.REGISTER,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response addFriend(
            AddFriendData data)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.ADD_FRIEND,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response getFriends(
            int clientId)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.GET_FRIENDS,
                        clientId
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response getFriendRequests(
            int clientId)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.GET_FRIEND_REQUESTS,
                        clientId
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response acceptFriendRequest(
            int requestId)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.ACCEPT_FRIEND_REQUEST,
                        requestId
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response declineFriendRequest(
            int requestId)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.DECLINE_FRIEND_REQUEST,
                        requestId
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response searchUsers(
            String query,
            int currentUserId)
            throws IOException, ClassNotFoundException {

        SearchData data =
                new SearchData(
                        query,
                        currentUserId
                );

        Request request =
                new Request(
                        RequestType.SEARCH_USERS,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response getMyWishList(
            int userId)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.GET_MY_WISH_LIST,
                        userId
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response createWishList(
            int userId,
            String name)
            throws IOException, ClassNotFoundException {

        CreateWishListData data =
                new CreateWishListData(
                        userId,
                        name
                );

        Request request =
                new Request(
                        RequestType.CREATE_WISH_LIST,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response addGiftItem(
            String name,
            double price)
            throws IOException, ClassNotFoundException {

        ensureConnected();

        AddGiftItemData data =
                new AddGiftItemData(
                        name,
                        price
                );

        Request request =
                new Request(
                        RequestType.ADD_GIFT_ITEM,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response addWishListItem(
            int wishListId,
            int giftItemId)
            throws IOException, ClassNotFoundException {

        WishListItemData data =
                new WishListItemData(
                        wishListId,
                        giftItemId
                );

        Request request =
                new Request(
                        RequestType.ADD_WISH_LIST_ITEM,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response removeWishListItem(
            int wishListId,
            int giftItemId)
            throws IOException, ClassNotFoundException {

        WishListItemData data =
                new WishListItemData(
                        wishListId,
                        giftItemId
                );

        Request request =
                new Request(
                        RequestType.REMOVE_WISH_LIST_ITEM,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response updateGiftItem(
            int id,
            String name,
            double price)
            throws IOException, ClassNotFoundException {

        UpdateGiftItemData data =
                new UpdateGiftItemData(
                        id,
                        name,
                        price
                );

        Request request =
                new Request(
                        RequestType.UPDATE_GIFT_ITEM,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response deleteGiftItem(
            int id)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.DELETE_GIFT_ITEM,
                        id
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response addNotification(
            int recipientId,
            String message)
            throws IOException, ClassNotFoundException {

        AddNotificationData data =
                new AddNotificationData(
                        recipientId,
                        message
                );

        Request request =
                new Request(
                        RequestType.ADD_NOTIFICATION,
                        data
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }

    public Response getNotifications(
            int userId)
            throws IOException, ClassNotFoundException {

        Request request =
                new Request(
                        RequestType.GET_NOTIFICATIONS,
                        userId
                );

        output.writeObject(request);
        output.flush();

        return (Response) input.readObject();
    }


    public void disconnect() {

        try {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}