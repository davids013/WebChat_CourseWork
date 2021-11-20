import client.WebClient;
import server.WebServer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Курсовой проект \"Сетевой чат\"\n");

        Thread server = new Thread(WebServer::start);
        Thread client1 = new Thread(WebClient::start);
        Thread client2 = new Thread(WebClient::start);
        Thread client3 = new Thread(WebClient::start);

        server.start();
        client1.start();
        client2.start();
        client3.start();

        client1.join();
        client2.join();
        client3.join();

        Thread.sleep(100);
        System.out.println("\033[0m" + "Принудительная остановка программы");
        System.exit(130);
    }
}
