import client.WebClient;
import entities.*;
import server.WebServer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Курсовой проект \"Сетевой чат\"\n");

//        TO+DO: add client/server logs
//        TO+DO: save users list to file
//        TODO: load users list from users.db
//        TODO: examine in user list before register
//        TODO: FileWorker -> Logger, adapt to read
//        TODO: add tests

        Thread server = new Thread(WebServer::start);
        Thread client1 = new Thread(WebClient::start);
        Thread client2 = new Thread(WebClient::start);
        Thread client3 = new Thread(WebClient::start);
        server.start();
        Thread.sleep(50);
        client1.start();
        client2.start();
        client3.start();

        client1.join();
        client2.join();
        client3.join();

//        String test = "{\"Edward\":{\"name\":\"Edward\",\"incomingMessages\":[{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":408736400}},\"author\":\"Eva\",\"addressee\":\"Edward\",\"text\":\"Забери сына из школы!!!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":408736400}},\"author\":\"Mark\",\"addressee\":\"Edward\",\"text\":\"Как дела?\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":600620200}},\"author\":\"Eva\",\"addressee\":\"Edward\",\"text\":\"Кто она?! Я её знаю?!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":899434300}},\"author\":\"Mark\",\"addressee\":\"Edward\",\"text\":\"Привет!\"}],\"outgoingMessages\":[{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":409736200}},\"author\":\"Edward\",\"addressee\":\"Eva\",\"text\":\"Как дела?\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":593624200}},\"author\":\"Edward\",\"addressee\":\"Mark\",\"text\":\"Ходил сегодня на собеседование\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":778510200}},\"author\":\"Edward\",\"addressee\":\"Eva\",\"text\":\"Привет!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":958398600}},\"author\":\"Edward\",\"addressee\":\"Eva\",\"text\":\"Забери сына из школы!!!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":51,\"nano\":110304000}},\"author\":\"Edward\",\"addressee\":\"Mark\",\"text\":\"Забери сына из школы!!!\"}],\"logFile\":\"logs\\\\chat_log\\\\Edward.log\",\"logger\":{\"file\":{\"path\":\"logs\\\\chat_log\\\\Edward.log\"},\"isAppendable\":true}},\"Eva\":{\"name\":\"Eva\",\"incomingMessages\":[{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":409736200}},\"author\":\"Edward\",\"addressee\":\"Eva\",\"text\":\"Как дела?\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":581633400}},\"author\":\"Mark\",\"addressee\":\"Eva\",\"text\":\"Ходил сегодня на собеседование\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":740531700}},\"author\":\"Mark\",\"addressee\":\"Eva\",\"text\":\"Забери сына из школы!!!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":778510200}},\"author\":\"Edward\",\"addressee\":\"Eva\",\"text\":\"Привет!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":958398600}},\"author\":\"Edward\",\"addressee\":\"Eva\",\"text\":\"Забери сына из школы!!!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":51,\"nano\":28355400}},\"author\":\"Mark\",\"addressee\":\"Eva\",\"text\":\"Кто она?! Я её знаю?!\"}],\"outgoingMessages\":[{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":408736400}},\"author\":\"Eva\",\"addressee\":\"Edward\",\"text\":\"Забери сына из школы!!!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":600620200}},\"author\":\"Eva\",\"addressee\":\"Edward\",\"text\":\"Кто она?! Я её знаю?!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":781508600}},\"author\":\"Eva\",\"addressee\":\"Mark\",\"text\":\"Кто она?! Я её знаю?!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":932414200}},\"author\":\"Eva\",\"addressee\":\"Mark\",\"text\":\"Жена опять пилит\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":51,\"nano\":75326500}},\"author\":\"Eva\",\"addressee\":\"Mark\",\"text\":\"Жена опять пилит\"}],\"logFile\":\"logs\\\\chat_log\\\\Eva.log\",\"logger\":{\"file\":{\"path\":\"logs\\\\chat_log\\\\Eva.log\"},\"isAppendable\":true}},\"Mark\":{\"name\":\"Mark\",\"incomingMessages\":[{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":593624200}},\"author\":\"Edward\",\"addressee\":\"Mark\",\"text\":\"Ходил сегодня на собеседование\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":781508600}},\"author\":\"Eva\",\"addressee\":\"Mark\",\"text\":\"Кто она?! Я её знаю?!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":932414200}},\"author\":\"Eva\",\"addressee\":\"Mark\",\"text\":\"Жена опять пилит\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":51,\"nano\":75326500}},\"author\":\"Eva\",\"addressee\":\"Mark\",\"text\":\"Жена опять пилит\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":51,\"nano\":110304000}},\"author\":\"Edward\",\"addressee\":\"Mark\",\"text\":\"Забери сына из школы!!!\"}],\"outgoingMessages\":[{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":408736400}},\"author\":\"Mark\",\"addressee\":\"Edward\",\"text\":\"Как дела?\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":581633400}},\"author\":\"Mark\",\"addressee\":\"Eva\",\"text\":\"Ходил сегодня на собеседование\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":740531700}},\"author\":\"Mark\",\"addressee\":\"Eva\",\"text\":\"Забери сына из школы!!!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":50,\"nano\":899434300}},\"author\":\"Mark\",\"addressee\":\"Edward\",\"text\":\"Привет!\"},{\"sentTime\":{\"date\":{\"year\":2021,\"month\":11,\"day\":19},\"time\":{\"hour\":1,\"minute\":21,\"second\":51,\"nano\":28355400}},\"author\":\"Mark\",\"addressee\":\"Eva\",\"text\":\"Кто она?! Я её знаю?!\"}],\"logFile\":\"logs\\\\chat_log\\\\Mark.log\",\"logger\":{\"file\":{\"path\":\"logs\\\\chat_log\\\\Mark.log\"},\"isAppendable\":true}}}";
//        Map<String, User> map = Serializer.deserialize(test, Map.class);
//        for (String key : map.keySet()) {
//            System.out.println(key);
//        }

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
