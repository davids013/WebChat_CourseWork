package client;

import file_worker.FileWorker;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class WebClient {
    private static final char SEP = File.separatorChar;
    private static final String COLOR = "\033[34m";
    private static final String EXIT_WORD_EN = "exit";
    private static final String EXIT_WORD_RU = "учше";
    private static final String SETTINGS_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "settings.txt";
    private static final String LOG_FILE_PATH = "src" + SEP + "main" + SEP + "resources" + SEP + "client.log";
    final static String HOST = FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.HOST_INDEX];
    final static int PORT =
            Integer.parseInt(FileWorker.getHostAndPortFromConfig(SETTINGS_FILE_PATH)[FileWorker.PORT_INDEX]);

    public static void start() {
        System.out.println(COLOR + "Клиент запускается...");

        final InetSocketAddress address = new InetSocketAddress(HOST, PORT);
        try (final SocketChannel socketChannel = SocketChannel.open();
             final Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(address);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 20);

            String request;
            Thread.sleep(1); // Костыль для правильной очередности печати в консоль
            while (true) {
                System.out.print(COLOR +
                        "Введите запрос серверу (`" + EXIT_WORD_EN + "` для выхода):\n>> ");
                request = scanner.nextLine();
                if (EXIT_WORD_EN.equalsIgnoreCase(request.trim()) || EXIT_WORD_RU.equalsIgnoreCase(request.trim()))
                    break;
                System.out.println(COLOR + "Клиент отсылает запрос:\t" + request);
                socketChannel.write(ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8)));
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(COLOR + "Ответ сервера получен:\t" +
                        new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(COLOR + "Клиент завершил работу" + "\033[0m");
    }


    private static boolean log(String message, String logFilename) {
//        TODO: code it
        return false;
    }
}
