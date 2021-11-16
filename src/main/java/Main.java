import client.WebClient;
import entities.Message;
import entities.Request;
import entities.Serializer;
import entities.User;
import server.WebServer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world!");

        Thread server = new Thread(WebServer::start);
        Thread client1 = new Thread(WebClient::start);
        Thread client2 = new Thread(WebClient::start);
        server.start();
        Thread.sleep(50);
        client1.start();
        client2.start();

    }
}
