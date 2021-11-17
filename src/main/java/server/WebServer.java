package server;

import entities.User;
import file_worker.FileWorker;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WebServer {
    public static final char SEP = File.separatorChar;
    protected static final String COLOR = "\033[33m";
    protected static final String EXIT_WORD_EN = "exit";
    protected static final String EXIT_WORD_RU = "учше";
    public static final String SEND_MESSAGE_KEY = "send";
    public static final String REGISTER_USER_KEY = "register";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    public static final String CHAT_LOG_DIRECTORY = "src" + SEP + "logs";
    private static final String LOG_FILE_PATH = CHAT_LOG_DIRECTORY + SEP + "server.log";
    public static final String LOG_EXTENSION = ".log";
    private final static String HOST = FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.HOST_INDEX];
    private static final int PORT =
            Integer.parseInt(FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.PORT_INDEX]);
    public static final Map<String, User> users = new ConcurrentSkipListMap<>();
    protected static final AtomicInteger online = new AtomicInteger(0);

    public static void start() {
        System.out.println(COLOR + "Сервер запускается...");
        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
//            final ServerSocketChannel server = ServerSocketChannel.open();
            final ServerSocket server = new ServerSocket(PORT);
//            server.bind(new InetSocketAddress(HOST, PORT));
            while (true) {
                new Thread(new ServerTask(server.accept())).start();
//                pool.submit(new ServerTask(server.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
//        } finally {
//            pool.shutdownNow();
        }
//        System.out.println(COLOR + "Сервер завершил работу" + "\033[0m");
    }

    private static void addNewUser(User user) {
        users.put(user.getName(), user);
    }
}