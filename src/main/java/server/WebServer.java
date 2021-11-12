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
import java.util.ArrayList;
import java.util.List;

public class WebServer {
    private static final char SEP = File.separatorChar;
    private static final String COLOR = "\033[33m";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    private static final String LOG_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "server.log";
    final static String HOST = FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.HOST_INDEX];
    private final static int PORT =
            Integer.parseInt(FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.PORT_INDEX]);
    private final static List<User> users = new ArrayList<>();

    public static void start() {
        System.out.println(COLOR + "Сервер запускается...");
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(HOST, PORT));
            try (final SocketChannel client = server.accept()) {
                final ByteBuffer buf = ByteBuffer.allocate(2 << 5);

                System.out.println(COLOR + "Сервер установил соединение");
                while (client.isConnected()) {
                    System.out.println(COLOR + "Сервер ожидает запроса");
                    int bytes = client.read(buf);
                    if (bytes == -1) break;
                    final String request = new String(buf.array(), 0, bytes, StandardCharsets.UTF_8);
                    buf.clear();
                    System.out.println(COLOR + "Сервер получил запрос:\t" + request);

                    String result = "";
//                    TODO: some server magic
                    result = "<!!!> " + request;
                    System.out.println(COLOR + "Сервер отправляет ответ:\t" + result);
                    client.write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(COLOR + "Сервер завершил работу" + "\033[0m");
    }

    private static boolean addNewUser(User user) { return users.add(user); }
}