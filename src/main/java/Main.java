import client.WebClient;
import server.WebServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello world!");

        Thread server = new Thread(WebServer::start);
        Thread client1 = new Thread(WebClient::start);
        Thread client2 = new Thread(WebClient::start);
        server.start();
//        Thread.sleep(10);
        client1.start();
    }
}
