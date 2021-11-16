package client;

import entities.Commands;
import entities.Message;
import entities.Request;
import entities.Serializer;
import file_worker.FileWorker;
import server.WebServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class WebClient {
    private static final byte TOTAL_MESSAGES = 3;
    private static final char SEP = File.separatorChar;
    private static final String COLOR = "\033[34m";
    private static final String EXIT_WORD_EN = "exit";
    private static final String EXIT_WORD_RU = "учше";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    private static final String LOG_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "client.log";
    private final static String HOST = FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.HOST_INDEX];
    private final static int PORT =
            Integer.parseInt(FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.PORT_INDEX]);

    public static void start() {
        System.out.println(COLOR + "Клиент " + Thread.currentThread().getName() + " запускается...");

        final Scanner scanner = new Scanner(System.in);
        final InetSocketAddress address = new InetSocketAddress(HOST, PORT);
        try (Socket socket = new Socket(HOST, PORT);
             final BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             final PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream()), true)) {
//        try (final SocketChannel socketChannel = SocketChannel.open()) {

//            socketChannel.connect(address);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 20);

            String input = null;
            String userName = null;
            Request request = null;
            final ThreadLocal<Integer> counter = new ThreadLocal<>();
            while (true) {
                Thread.sleep(100); // Костыль для правильной очередности печати в консоль
//                input = "test input";
//                Thread.sleep(1000);
//                final Request request = new Request(WebServer.REGISTER_USER_KEY, input);
                if (request == null) {
                    counter.set(0);
                    System.out.print(COLOR +
                            "Введите запрос серверу (`" + EXIT_WORD_EN + "` для выхода):\n>> ");
                    input = scanner.nextLine();
                    request = requestRegistration(input);
                    userName = request.getBody();
                    Thread.currentThread().setName(userName);
//                    Thread.sleep(500);
                } else if (counter.get() < TOTAL_MESSAGES) {
                    request = requestSendMessage(userName);
                    counter.set(counter.get() + 1);
//                    Thread.sleep(500);
                } else {
//                    socketChannel.write(ByteBuffer.wrap(EXIT_WORD_EN.getBytes(StandardCharsets.UTF_8)));
                    out.println(EXIT_WORD_EN);
                    break;
                }
//                final Request request = new Request(Commands.REGISTER_USER, input);
                final String requestStr = Serializer.serialize(request);
                System.out.println(COLOR + "Клиент " + Thread.currentThread().getName() + " отсылает запрос:\t" + requestStr);
//                socketChannel.write(ByteBuffer.wrap(requestStr.getBytes(StandardCharsets.UTF_8)));
                out.println(requestStr);
                if (EXIT_WORD_EN.equalsIgnoreCase(input.trim()) || EXIT_WORD_RU.equalsIgnoreCase(input.trim()))
                    break;
//                int bytesCount = socketChannel.read(inputBuffer);
//                if (bytesCount >= 0) {
                if (true) {
                    System.out.println(COLOR + "Ответ сервера получен:\t" +
//                            new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                        in.readLine());
                }
//                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(COLOR + "Клиент " + Thread.currentThread().getName() + " завершил работу" + "\033[0m");
    }

    private static Request requestRegistration(String userName) {
//        final Scanner scanner = new Scanner(System.in);
        return new Request(Commands.REGISTER_USER, userName);
    }

    private static Request requestSendMessage(String authorName) {
        final Message message = getRandomMessage(authorName);
        final String body = Serializer.serialize(message);
        return new Request(Commands.SEND_MESSAGE, body);
    }

    private static Message getRandomMessage(String authorName) {
        final Random rnd = new Random();
        final String addresseeName;
        final String text;
        final Message message;
        System.out.println("usersSize = " + WebServer.users.size());
        if (WebServer.users.size() <= 1) {
            addresseeName = authorName;
        } else {
            final Set<String> userNames = new HashSet<>(WebServer.users.keySet());
            userNames.remove(authorName);
            addresseeName = (String) userNames.toArray()[rnd.nextInt(userNames.size())];
        }
        text = Message.TEMPLATE[rnd.nextInt(Message.TEMPLATE.length)];
        return new Message(authorName, addresseeName, text);
    }

    private static boolean log(String message, String logFilename) {
//        TODO: code it
        return false;
    }
}
