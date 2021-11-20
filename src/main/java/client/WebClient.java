package client;

import entities.*;
import entities.ConfigWorker;
import server.WebServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class WebClient {
    private static final byte SESSION_MESSAGES = 5;
    private static final char SEP = File.separatorChar;
    private static final String COLOR = "\033[34m";
    private static final String EXIT_WORD_EN = "exit";
    private static final String EXIT_WORD_RU = "учше";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    private final static String HOST = ConfigWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[ConfigWorker.HOST_INDEX];
    private final static int PORT =
            Integer.parseInt(ConfigWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[ConfigWorker.PORT_INDEX]);

    public static void start() {
        System.out.println(COLOR + "Клиент " + Thread.currentThread().getName()
                + " подключается к серверу " + HOST + " (порт " + PORT + ")...");

        final Scanner scanner = new Scanner(System.in);
        final InetSocketAddress address = new InetSocketAddress(HOST, PORT);
        try (final SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(address);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 20);

            String input = null;
            String userName = null;
            Request request = null;
            int counterInt = 0;
            while (true) {
                if (request == null) {
                    Thread.sleep(750); // Костыль для правильной очередности печати в консоль
                    System.out.print(COLOR +
                            "Введите имя пользователя (`" + EXIT_WORD_EN + "` для отказа):\n>> ");
                    input = scanner.nextLine();
                    request = requestRegistration(input);
                    userName = request.getBody();
                    Thread.currentThread().setName(userName);
                } else if (counterInt < SESSION_MESSAGES) {
                    request = requestSendMessage(userName);
                    counterInt++;
                } else {
                    request = new Request(Commands.EXIT, "exit");
                    socketChannel.write(ByteBuffer.wrap(
                            Serializer.serialize(request).getBytes(StandardCharsets.UTF_8)));
                    break;
                }
                final String requestStr = Serializer.serialize(request);
                System.out.println(COLOR + "Клиент " + Thread.currentThread().getName()
                        + " отсылает запрос:\t" + requestStr);
                socketChannel.write(ByteBuffer.wrap(requestStr.getBytes(StandardCharsets.UTF_8)));
                if (EXIT_WORD_EN.equalsIgnoreCase(input.trim()) || EXIT_WORD_RU.equalsIgnoreCase(input.trim()))
                    break;
                int bytesCount = -1;
                try {
                    bytesCount = socketChannel.read(inputBuffer);
                } catch (IOException ignored) { }
                if (bytesCount >= 0) {
                    System.out.println(COLOR + "Ответ сервера получен:\t" +
                            new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                }
                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(COLOR + "Клиент " + Thread.currentThread().getName() + " завершил работу" + "\033[0m");
    }

    private static Request requestRegistration(String userName) {
        if (EXIT_WORD_EN.equalsIgnoreCase(userName.trim()) || EXIT_WORD_RU.equalsIgnoreCase((userName).trim()))
            return new Request(Commands.EXIT, "exit");
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
        if (WebServer.getUsers().size() <= 1) {
            addresseeName = authorName;
        } else {
            final Set<String> userNames = new HashSet<>(WebServer.getUsers().keySet());
            userNames.remove(authorName);
            addresseeName = (String) userNames.toArray()[rnd.nextInt(userNames.size())];
        }
        text = Message.TEMPLATE[rnd.nextInt(Message.TEMPLATE.length)];
        return new Message(authorName, addresseeName, text);
    }
}
