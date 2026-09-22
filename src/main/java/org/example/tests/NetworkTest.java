package org.example.tests;

import org.example.client.NetworkClient;
import org.example.network.Response;

import java.io.IOException;

public class NetworkTest {

    public static void main(String[] args) {

        NetworkClient client = new NetworkClient();

        try {
            client.connect();

            Response response =
                    client.login("ahmed", "123456");

            System.out.println("Success: "
                    + response.isSuccess());

            System.out.println("Message: "
                    + response.getMessage());

            if (response.getData() != null) {
                System.out.println("Client: "
                        + response.getData());
            }

            client.disconnect();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}