package org.example.service;

import org.example.database.FriendRequestDAO;
import org.example.database.UserDAO;
import org.example.model.Client;
import org.example.model.FriendRequest;

import java.sql.SQLException;

public class FriendRequestService {

    private final UserDAO userDAO;
    private final FriendRequestDAO friendRequestDAO;

    public FriendRequestService() {
        this.userDAO = new UserDAO();
        this.friendRequestDAO = new FriendRequestDAO();
    }

    public String addFriend(int senderId, int receiverId)
            throws SQLException {

        Client sender = userDAO.findById(senderId);
        Client receiver = userDAO.findById(receiverId);

        if (sender == null) {
            return "Sender not found";
        }

        if (receiver == null) {
            return "Receiver not found";
        }

        FriendRequest request = new FriendRequest(sender, receiver);

        boolean created =
                friendRequestDAO.create(request);

        if (!created) {
            return "Friend request already exists";
        }

        return "Friend request sent";
    }
}