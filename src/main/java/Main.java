import client.WebClient;
import server.WebServer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Курсовой проект \"Сетевой чат\"\n");

//        TO+DO: add client/server logs
//        TO+DO: save users list to file
//        TO+DO: load users list from users.info
//        TO+DO: examine in user list before register
//        TO-DO: ConfigWorker -> Logger, adapt to read          - not necessary
//        TO+DO: add entities tests
//        TO+DO: add server tests
//        TO-DO: add client tests                               - dont'know how

        Thread server = new Thread(WebServer::start);
        Thread client1 = new Thread(WebClient::start);
        Thread client2 = new Thread(WebClient::start);
        Thread client3 = new Thread(WebClient::start);
        server.start();
        Thread.sleep(10);
        client1.start();
        client2.start();
        client3.start();

        client1.join();
        client2.join();
        client3.join();

        System.out.println("\033[0m" + "Принудительная остановка программы");
        System.exit(130);
    }
}
