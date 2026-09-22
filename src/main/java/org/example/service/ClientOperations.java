package org.example.service;

import org.example.database.FriendRequestDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.example.model.Client;
import org.example.model.Contribution;
import org.example.model.FriendRequest;
import org.example.model.FriendRequestStatus;
import org.example.model.GiftItem;
import org.example.model.Notification;
import org.example.model.NotificationType;
import org.example.model.WishList;
import org.example.database.NotificationDAO;
import org.example.database.WishListDAO;
import org.example.database.ContributionDAO;
import org.example.database.DatabaseConnection;



public class ClientOperations {
    private final Client currentClient;
    private final FriendRequestDAO friendRequestDAO = new FriendRequestDAO();
    private final WishListDAO wishListDAO = new WishListDAO();
    private final ContributionDAO contributionDAO = new ContributionDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public ClientOperations(Client currentClient) {
        if (currentClient == null || currentClient.getId() <= 0)
            throw new IllegalArgumentException("The current client must be a registered client");
        this.currentClient = currentClient;
    }

    public void addFriend(Client friend) {
        requireRegistered(friend);
        if (friend.equals(currentClient)) throw new IWishException("You cannot add yourself as a friend");
        try {
            FriendRequest existing = friendRequestDAO.findBetween(currentClient.getId(), friend.getId());
            if (existing != null) {
                switch (existing.getStatus()) {
                    case PENDING:
                        throw new IWishException("A friend request between you two is already pending");
                    case ACCEPTED:
                        throw new IWishException("You are already friends");
                    default:
                        friendRequestDAO.delete(existing.getId());
                }
            }
            if (!friendRequestDAO.create(new FriendRequest(currentClient, friend)))
                throw new IWishException("The friend request could not be created");
        } catch (SQLException e) {
            throw new IWishException("Could not send the friend request", e);
        }
    }

    public void removeFriend(Client friend) {
        requireRegistered(friend);
        try {
            FriendRequest fr = friendRequestDAO.findBetween(currentClient.getId(), friend.getId());
            if (fr == null || fr.getStatus() != FriendRequestStatus.ACCEPTED)
                throw new IWishException("You are not friends with " + friend.getName());
            friendRequestDAO.delete(fr.getId());
        } catch (SQLException e) {
            throw new IWishException("Could not remove the friend", e);
        }
    }

    public List<Client> viewFriends() {
        try {
            return friendRequestDAO.findFriends(currentClient.getId());
        } catch (SQLException e) {
            throw new IWishException("Could not load the friends", e);
        }
    }

    public List<FriendRequest> viewPendingRequests() {
        try {
            return friendRequestDAO.findPendingForReceiver(currentClient.getId());
        } catch (SQLException e) {
            throw new IWishException("Could not load the friend requests", e);
        }
    }

    public void acceptFriend(FriendRequest request) {
        requireReceiver(request);
        request.acceptFriend();
        try {
            if (!friendRequestDAO.accept(request.getId())) throw new IWishException("The request is no longer pending");
        } catch (SQLException e) {
            throw new IWishException("Could not accept the request", e);
        }
    }

    public void declineFriend(FriendRequest request) {
        requireReceiver(request);
        request.declineFriend();
        try {
            if (!friendRequestDAO.decline(request.getId())) throw new IWishException("The request is no longer pending");
        } catch (SQLException e) {
            throw new IWishException("Could not decline the request", e);
        }
    }

    public WishList viewFriendWishList(Client friend) {
        requireFriend(friend);
        try {
            return wishListDAO.findByUser(friend.getId());
        } catch (SQLException e) {
            throw new IWishException("Could not load the wish list", e);
        }
    }

    public Contribution contribute(Client friend, GiftItem item, double amount) {
        requireFriend(friend);
        long cents = toCents(amount);
        if (cents <= 0) throw new IWishException("The amount must be greater than zero");
        Connection con = null;
        boolean previousAutoCommit = true;
        try {
            con = DatabaseConnection.getInstance().getConnection();
            previousAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            WishList wl = wishListDAO.findByUser(friend.getId());
            if (wl == null || !wl.contains(item))
                throw new IWishException("This item is not in " + friend.getName() + "'s wish list");

            long remaining = toCents(item.getPrice()) - toCents(contributionDAO.getTotal(wl.getId(), item.getId()));
            if (remaining <= 0) throw new IWishException("The price of this item is already complete");
            if (cents > remaining)
                throw new IWishException("Too much: only " + (remaining / 100.0) + " is still needed for this item");

            Contribution c = new Contribution(currentClient, item, wl, cents / 100.0);
            c.contribute();
            if (!contributionDAO.create(c)) throw new IWishException("The contribution could not be saved");

            if (cents == remaining) notifyCompletion(wl, item);
            con.commit();
            return c;
        } catch (SQLException | RuntimeException e) {
            rollback(con);
            if (e instanceof IWishException) throw (IWishException) e;
            if (e instanceof SQLException) throw new IWishException("Could not save the contribution", e);
            throw (RuntimeException) e;
        } finally {
            restoreAutoCommit(con, previousAutoCommit);
        }
    }

    private void notifyCompletion(WishList wl, GiftItem item) throws SQLException {
        Map<Integer, Client> contributors = new LinkedHashMap<>();
        for (Contribution c : contributionDAO.getContributions(wl.getId(), item.getId()))
            contributors.putIfAbsent(c.getClient().getId(), c.getClient());

        StringBuilder names = new StringBuilder();
        for (Client c : contributors.values()) {
            if (names.length() > 0) names.append(", ");
            names.append(c.getName());
            Notification buyer = new Notification(c, NotificationType.GIFT_COMPLETED,
                    "The price of \"" + item.getName() + "\" for " + wl.getOwner().getName() + " is now complete. Thank you!");
            buyer.send();
            notificationDAO.create(buyer);
        }
        Notification receiver = new Notification(wl.getOwner(), NotificationType.ITEM_BOUGHT,
                "\"" + item.getName() + "\" from your wish list has been bought by " + names + ".");
        receiver.send();
        notificationDAO.create(receiver);
    }

    public List<Notification> viewNotifications() {
        try {
            return notificationDAO.getUserNotifications(currentClient.getId());
        } catch (SQLException e) {
            throw new IWishException("Could not load the notifications", e);
        }
    }

    public void markNotificationAsRead(Notification n) {
        if (!currentClient.equals(n.getRecipient())) throw new IWishException("This is not your notification");
        try {
            if (!notificationDAO.markAsRead(n.getId())) throw new IWishException("Notification not found");
            n.markAsRead();
        } catch (SQLException e) {
            throw new IWishException("Could not update the notification", e);
        }
    }

    private void requireRegistered(Client c) {
        if (c == null || c.getId() <= 0) throw new IWishException("That client does not exist");
    }

    private void requireReceiver(FriendRequest r) {
        if (r == null || !currentClient.equals(r.getReceiver()))
            throw new IWishException("Only the receiver can answer a friend request");
    }

    private void requireFriend(Client friend) {
        requireRegistered(friend);
        try {
            FriendRequest fr = friendRequestDAO.findBetween(currentClient.getId(), friend.getId());
            if (fr == null || fr.getStatus() != FriendRequestStatus.ACCEPTED)
                throw new IWishException("You are not friends with " + friend.getName());
        } catch (SQLException e) {
            throw new IWishException("Could not check the friendship", e);
        }
    }

    private static long toCents(double money) { return Math.round(money * 100); }

    private static void rollback(Connection con) {
        if (con == null) return;
        try {
            con.rollback();
        } catch (SQLException ex) {
            System.err.println("Rollback failed: " + ex.getMessage());
        }
    }

    private static void restoreAutoCommit(Connection con, boolean value) {
        if (con == null) return;
        try {
            con.setAutoCommit(value);
        } catch (SQLException ex) {
            System.err.println("Could not restore auto-commit: " + ex.getMessage());
        }
    }
}
