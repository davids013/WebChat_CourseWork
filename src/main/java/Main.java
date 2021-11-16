import client.WebClient;
import entities.*;
import server.WebServer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world!");

        Thread server = new Thread(WebServer::start);
        Thread client1 = new Thread(WebClient::start);
        Thread client2 = new Thread(WebClient::start);
        Thread client3 = new Thread(WebClient::start);
        server.start();
        Thread.sleep(50);
        client1.start();
        client2.start();
        client3.start();

//        Commands com1 = Commands.SEND_MESSAGE;
//        String str = Serializer.serialize(com1);
//        System.out.println(Serializer.serialize(com1));
//        System.out.println(Serializer.deserialize(str, Commands.class));
//        Message msg = new Message("Maria", "Frank",
//                Message.TEMPLATE[(int) Math.round(Math.random() * Message.TEMPLATE.length)]);
//        System.out.println(Serializer.serialize(msg));
    }
}
