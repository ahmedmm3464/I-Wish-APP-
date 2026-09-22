package org.example.server;

import org.example.database.FriendRequestDAO;
import org.example.database.UserDAO;
import org.example.service.AuthenticationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5000;
    private static AuthenticationService authenticationService = new AuthenticationService();
    private static FriendRequestDAO friendRequestDAO = new FriendRequestDAO();
    private static  UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started...");

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "Client connected: "
                                + socket.getInetAddress()
                );

                ClientHandler handler = new ClientHandler(authenticationService , friendRequestDAO , userDAO, socket );

                Thread thread = new Thread(handler);

                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}