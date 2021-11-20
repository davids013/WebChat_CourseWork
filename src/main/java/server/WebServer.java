package server;

import entities.ConfigWorker;
import entities.Serializer;
import entities.User;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WebServer {
    public static final char SEP = File.separatorChar;
    protected static final String COLOR = "\033[33m";
//    protected static final String EXIT_WORD_EN = "exit";
//    protected static final String EXIT_WORD_RU = "учше";
//    public static final String SEND_MESSAGE_KEY = "send";
//    public static final String REGISTER_USER_KEY = "register";
    private static final String RESOURCES_PATH = "src" + SEP + "main" + SEP + "resources";
    public static final String SETTINGS_FILE_PATH = RESOURCES_PATH + SEP + "settings.txt";
    public static final String LOG_EXTENSION = ".log";
    public static final String ROOT_LOG_DIRECTORY = "logs";
    public static final String CHAT_LOG_DIRECTORY = ROOT_LOG_DIRECTORY + SEP + "chat_log";
    protected static final String SERVER_LOG_PATH = ROOT_LOG_DIRECTORY + SEP + "requests" + LOG_EXTENSION;
    private static final int PORT =
            Integer.parseInt(ConfigWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[ConfigWorker.PORT_INDEX]);
    public static final int MAX_ONLINE = 10;
    public static final String USERS_STORAGE_PATH = RESOURCES_PATH + SEP + "users.info";
    public static final Map<String, User> users = new ConcurrentSkipListMap<>();
    protected static final AtomicInteger online = new AtomicInteger(0);

    public static void start() {
        System.out.println(COLOR + "Сервер запускается...");
        final ExecutorService pool = Executors.newFixedThreadPool(MAX_ONLINE);
        try {
//            final ServerSocketChannel server = ServerSocketChannel.open();
            final ServerSocket server = new ServerSocket(PORT);
//            server.bind(new InetSocketAddress(HOST, PORT));
            if (Files.exists(Paths.get(USERS_STORAGE_PATH))) {
                final List<String> savedUsers =
                        Files.readAllLines(Paths.get(USERS_STORAGE_PATH), StandardCharsets.UTF_8);
                for (String userJson : savedUsers) {
                    if (!userJson.trim().isEmpty()) {
                        final User user = Serializer.deserialize(userJson, User.class);
                        users.put(user.getName(), user);
                    }
                }

            }

            while (!Thread.currentThread().isInterrupted()) {
//                new Thread(new ServerTask(server.accept())).start();
                pool.submit(new ServerTask(server.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
//        System.out.println(COLOR + "Сервер завершил работу" + "\033[0m");
    }

    public static int getOnline() { return online.get(); }

    public static Map<String, User> getUsers() { return users; }
}