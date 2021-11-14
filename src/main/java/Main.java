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
//        server.start();
        Thread.sleep(50);
//        client1.start();
//        client2.start();
        Message mes1 = new Message("Ida", "Ozreal", "Hello my friend!");
        System.out.println(mes1);
        String req = Serializer.serialize(mes1);
        System.out.println(req);
        Message mes2 = Serializer.deserialize(req, Message.class);
        System.out.println(mes2);
        Request request1 = new Request("com", Serializer.serialize(mes2));
        String req1str = Serializer.serialize(request1);
        System.out.println("command ->  " + Serializer.deserialize(req1str, Request.class).getCommand());
        System.out.println("body -> " +
                Serializer.deserialize(Serializer.deserialize(req1str, Request.class).getBody(), Message.class));

        List<User> list = new ArrayList<>();
        String name = "igor";
        list.add(new User(name));
        System.out.println("contains -> " + list.contains(new User(name)));
    }
}
