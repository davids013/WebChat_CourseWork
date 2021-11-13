package server;

import entities.User;
import file_worker.FileWorker;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class WebServer {
    private static final char SEP = File.separatorChar;
    protected static final String COLOR = "\033[33m";
    protected static final String EXIT_WORD_EN = "exit";
    protected static final String EXIT_WORD_RU = "учше";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    private static final String LOG_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "server.log";
    private final static String HOST = FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.HOST_INDEX];
    private static final int PORT =
            Integer.parseInt(FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.PORT_INDEX]);
    protected static final Set<User> users = new HashSet<>();
    protected static final AtomicInteger online = new AtomicInteger(0);

    public static void start() {
        System.out.println(COLOR + "Сервер запускается...");
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(HOST, PORT));
            while (true) {
                new Thread(new ServerTask(server.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(COLOR + "Сервер завершил работу" + "\033[0m");
    }

    private static boolean addNewUser(User user) {
        return users.add(user);
    }
}