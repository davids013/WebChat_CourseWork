import client.WebClient;
import entities.Message;
import server.WebServer;

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
        String req = mes1.serialize();
        System.out.println(req);
        Message mes2 = Message.deserialize(req);
        System.out.println(mes2);
    }
}
