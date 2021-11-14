package server;

import entities.Message;
import entities.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerTask implements Runnable {
    private final String EXIT_WORD_EN = WebServer.EXIT_WORD_EN;
    private final String EXIT_WORD_RU = WebServer.EXIT_WORD_RU;
    private final SocketChannel client;
    private final String COLOR = WebServer.COLOR;

    public ServerTask(SocketChannel client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.out.println(COLOR + Thread.currentThread().getName() +
                    " Установлено соединение. Онлайн " + WebServer.online.incrementAndGet());
            System.out.println(COLOR + "isConnected " + client.isConnected());
            while (client.isConnected()) {
                final ByteBuffer buf = ByteBuffer.allocate(2 << 5);
//                Thread.sleep(1);
                System.out.println(COLOR + "Сервер ожидает запроса");
                int bytes = 0;
                bytes = client.read(buf);
                System.out.println(COLOR + "bytes " + bytes);
                if (bytes == -1) {
                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + WebServer.online.decrementAndGet());
                    break;
                }
                final String request = new String(buf.array(), 0, bytes, StandardCharsets.UTF_8);
                if (EXIT_WORD_EN.equalsIgnoreCase(request.trim()) || EXIT_WORD_RU.equalsIgnoreCase(request.trim())) {
                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + WebServer.online.decrementAndGet());
                    break;
                }
                buf.clear();
                System.out.println(COLOR + "Сервер получил запрос:\t" + request);

                String result = "";
//                    TODO: some server magic
                result = "<!!!> " + request;
                System.out.println(COLOR + "Сервер отправляет ответ:\t" + result);
                client.write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
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
            return "Успешно. Пользователь с ником \"" + name + "" + "\" зарегистрирован";
        } return "Отказано. Пользователь с ником \"" + name + "" + "\" уже существует";
    }

    private String sendMessage(Message message) {
        final String authorName = message.getAuthor();
        final String addresseeName = message.getAddressee();
        final User author = new User(authorName);
        final User addressee = new User(addresseeName);
        final String text = message.getText();
        if (!WebServer.users.containsKey(authorName) || !WebServer.users.containsKey(addressee)) {
            return "Отказано. Сообщение не отправлено по причине отсутствия пользователя в базе";
        } else {
//            TODO: убедиться, что письма добавляются пользователям, а не создаются юзеры с единственным письмом
            WebServer.users.get(authorName).sendMessage(message);
            WebServer.users.get(addresseeName).receiveMessage(message);
            return "Успешно. Сообщение отправлено пользователю \"" + addresseeName + "\"";
        }
    }
}
