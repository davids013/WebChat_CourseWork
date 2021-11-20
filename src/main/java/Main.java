import client.WebClient;
import entities.*;
import server.WebServer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Курсовой проект \"Сетевой чат\"\n");

//        TO+DO: add client/server logs
//        TO+DO: save users list to file
//        TO+DO: load users list from users.db
//        TO+DO: examine in user list before register
//        TO-DO: ConfigWorker -> Logger, adapt to read        - not necessary
//        TO+DO: add entities tests
//        TODO: add server/client tests

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


//        Map<String, User> users = new ConcurrentSkipListMap<>();
//        User user1 = new User("user1");
//        User user2 = new User("user2");
//        User user3 = new User("user2");
//        Message mes1 = new Message(user1.getName(), user2.getName(), "message1");
//        users.put(user1.getName(), user1);
//        users.put(user2.getName(), user2);
//        users.put(user2.getName(), user2);
//        users.put(user2.getName(), user2);
//
//        System.out.println(users.containsKey(user2.getName()));
//        for (Map.Entry<String, User> entry : users.entrySet()) {
//            System.out.println(entry.getKey() + " -> " + entry.getValue());
//        }
//        User user = new User("Jerry");
//        Message mes = new Message(user.getName(), "Tina", "Hello");
//        user.sendMessage(mes);
//        user.receiveMessage(mes);
//        String test = Serializer.serialize(user);
//        List<String> jsons = new ArrayList<>();
//        jsons.add(test);
//        System.out.println(test);
//        System.out.println(Serializer.deserialize(test, User.class));
//        User user2 = new User("Tina");
//        user2.sendMessage(mes);
//        test = Serializer.serialize(user2) + "\r\n";
//        jsons.add(test);
//        List<User> userList = new ArrayList<>();
//        userList.add(user);
//        userList.add(user2);
//        test = Serializer.serialize(userList);
//        System.out.println(test);
////        ArrayList<User> desUserList = Serializer.deserialize(test, ArrayList.class);
//        List<User> desUserList = new ArrayList<>();
//        for (String s : jsons) {
//            desUserList.add(Serializer.deserialize(s, user2.getClass()));
//        }
//        for (User u : desUserList) System.out.println(u);


        System.out.println("\033[0m" + "Принудительная остановка программы");
        System.exit(130);


//        Commands com1 = Commands.SEND_MESSAGE;
//        String str = Serializer.serialize(com1);
//        System.out.println(Serializer.serialize(com1));
//        System.out.println(Serializer.deserialize(str, Commands.class));
//        Message msg = new Message("Maria", "Frank",
//                Message.TEMPLATE[(int) Math.round(Math.random() * Message.TEMPLATE.length)]);
//        System.out.println(Serializer.serialize(msg));

//        final String SEP = File.separator;
//        final String filePath = "src\\logs\\ura.log";
//        Logger logger = new FileLogger(filePath);
//        logger.log("test1_" + System.currentTimeMillis());
//        logger.log("test2_" + System.currentTimeMillis());
//        logger.log("test3_" + System.currentTimeMillis());
    }
}
