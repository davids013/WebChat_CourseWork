package server;

import entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerTask implements Runnable {
    private final String EXIT_WORD_EN = WebServer.EXIT_WORD_EN;
    private final String EXIT_WORD_RU = WebServer.EXIT_WORD_RU;
    private final Socket client;
    private final String COLOR = WebServer.COLOR;
    protected static final String SEND_MESSAGE_KEY = WebServer.SEND_MESSAGE_KEY;
    protected static final String REGISTER_USER_KEY = WebServer.REGISTER_USER_KEY;

    public ServerTask(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (final PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             final BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream()))) {
            System.out.println(COLOR + Thread.currentThread().getName() +
                    " Установлено соединение. Онлайн " + WebServer.online.incrementAndGet());
//            while (client.isConnected()) {
            String requestStr;
            while ((requestStr = in.readLine()) != null) {
//                final ByteBuffer buf = ByteBuffer.allocate(2 << 5);
//                Thread.sleep(1);
                System.out.println(COLOR + "Сервер ожидает запроса");
                int bytes = 0;
//                bytes = client.read(buf);
//                if (bytes == -1) {
//                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + WebServer.online.decrementAndGet());
//                    break;
//                }
//                final String requestStr = new String(buf.array(), 0, bytes, StandardCharsets.UTF_8);
                if (EXIT_WORD_EN.equalsIgnoreCase(requestStr.trim())
                        || EXIT_WORD_RU.equalsIgnoreCase(requestStr.trim())) {
                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + WebServer.online.decrementAndGet());
                    break;
                }
//                buf.clear();
                System.out.println(COLOR + "Сервер получил запрос:\t" + requestStr);

                final String result;
//                if (requestStr.contains(REGISTER_USER_KEY) || requestStr.contains(SEND_MESSAGE_KEY)) {
                if (requestStr.contains(Serializer.serialize(Commands.REGISTER_USER))
                        || requestStr.contains(Serializer.serialize(Commands.SEND_MESSAGE))) {
                        final Request request = Serializer.deserialize(requestStr, Request.class);
//                        final String command = request.getCommand();
                    final Commands command = request.getCommand();
                        final String body = request.getBody();
                        switch (command) {
                            case REGISTER_USER:
                                result = registerUser(body);
                                break;
                            case SEND_MESSAGE:
                                result = sendMessage(Serializer.deserialize(body, Message.class));
                                break;
                            default:
                                result = "Ошибка. Неизвестный запрос";
                        }
                    } else result = "<!!!> " + requestStr;
//                }

//                result = "<!!!> " + requestStr;
                System.out.println(COLOR + "Сервер отправляет ответ:\t" + result);
//                client.write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
                out.println(result);
                System.out.println("users: " + WebServer.users);
            }
    } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String registerUser(String name) {
        final User user = new User(name);
        if (!WebServer.users.containsValue(user)) {
            WebServer.users.put(name, user);
            System.out.println("users: " + WebServer.users);
            return "Успешно. Пользователь с именем \"" + name + "" + "\" зарегистрирован";
        } return "Отказано. Пользователь с именем \"" + name + "" + "\" уже существует";
    }

    private String sendMessage(Message message) {
        final String authorName = message.getAuthor();
        final String addresseeName = message.getAddressee();
        if (!WebServer.users.containsKey(authorName)) {
            return "Отказано. Сообщение не отправлено, отправитель " + authorName + " не найден в базе";
        } if (!WebServer.users.containsKey(addresseeName)) {
            return "Отказано. Сообщение не отправлено, получатель " + addresseeName + " не найден в базе";
        } else {
//            TODO: убедиться, что письма добавляются пользователям, а не создаются юзеры с единственным письмом
            WebServer.users.get(authorName).sendMessage(message);
            WebServer.users.get(addresseeName).receiveMessage(message);
            return "Успешно. Сообщение отправлено пользователю \"" + addresseeName + "\"";
        }
    }
}
