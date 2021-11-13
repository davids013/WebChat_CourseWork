package server;

import entities.User;
import file_worker.FileWorker;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class WebServer {
    private static final char SEP = File.separatorChar;
    private static final String COLOR = "\033[33m";
    private static final String EXIT_WORD_EN = "exit";
    private static final String EXIT_WORD_RU = "учше";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    private static final String LOG_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "server.log";
    final static String HOST = FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.HOST_INDEX];
    private static final int PORT =
            Integer.parseInt(FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.PORT_INDEX]);
    private static final Set<User> users = new HashSet<>();
    private static final AtomicInteger online = new AtomicInteger(0);

    public static void start() {
        System.out.println(COLOR + "Сервер запускается...");
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(HOST, PORT));
            while (true) {
//                try (SocketChannel client = server.accept()) {
                    new Thread(() -> {
                        try (SocketChannel client = server.accept()) {
                            final ByteBuffer buf = ByteBuffer.allocate(2 << 5);
                            System.out.println(COLOR + "client " + client);
                            System.out.println(COLOR + "Установлено соединение. Онлайн " + online.incrementAndGet());
                            System.out.println(COLOR + client.isConnected());
                            while (client.isConnected()) {
                                System.out.println(COLOR + "Сервер ожидает запроса");
                                int bytes = 0;
                                bytes = client.read(buf);
                                System.out.println(COLOR + "bytes " + bytes);
                                if (bytes == -1)  {
                                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + online.decrementAndGet());
                                    break;
                                }
                                final String request = new String(buf.array(), 0, bytes, StandardCharsets.UTF_8);
                                if (EXIT_WORD_EN.equalsIgnoreCase(request.trim()) || EXIT_WORD_RU.equalsIgnoreCase(request.trim())) {
                                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + online.decrementAndGet());
                                    break;
                                }
                                buf.clear();
                                System.out.println(COLOR + "Сервер получил запрос:\t" + request);

                                String result = "";
//                    TODO: some server magic
                                result = "<!!!> " + request;
                                System.out.println(COLOR + "Сервер отправляет ответ:\t" + result);
                                try {
                                    client.write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                    }).start();

                }
            } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(COLOR + "Сервер завершил работу" + "\033[0m");
    }

    private static boolean addNewUser(User user) { return users.add(user); }
}