package client;

import file_worker.FileWorker;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WebClient {
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
        try (final SocketChannel socketChannel = SocketChannel.open()) {

            socketChannel.connect(address);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 20);

            String request;
            while (true) {
                Thread.sleep(100); // Костыль для правильной очередности печати в консоль
                System.out.print(COLOR +
                        "Введите запрос серверу (`" + EXIT_WORD_EN + "` для выхода):\n>> ");
//                request = "test request";
//                Thread.sleep(1000);
                request = scanner.nextLine();
                System.out.println(COLOR + "Клиент " + Thread.currentThread().getName() + " отсылает запрос:\t" + request);
                socketChannel.write(ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8)));
//                int bytesCount = socketChannel.read(inputBuffer);
//                System.out.println(COLOR + "Ответ сервера получен:\t" +
//                        new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                if (EXIT_WORD_EN.equalsIgnoreCase(request.trim()) || EXIT_WORD_RU.equalsIgnoreCase(request.trim()))
                    break;
                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("!!!!!!!!!!!!!!!!! " + e.getMessage());
        }
        System.out.println(COLOR + "Клиент " + Thread.currentThread().getName() + " завершил работу" + "\033[0m");
    }


    private static boolean log(String message, String logFilename) {
//        TODO: code it
        return false;
    }
}
