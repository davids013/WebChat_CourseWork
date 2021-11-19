package server;

import entities.*;

import java.io.*;
import java.net.Socket;

public class ServerTask implements Runnable {
//    private final String EXIT_WORD_EN = WebServer.EXIT_WORD_EN;
//    private final String EXIT_WORD_RU = WebServer.EXIT_WORD_RU;
    private final Socket client;
    private final String COLOR = WebServer.COLOR;
    private final String LOG_SEPARATOR = FileLogger.LINE_SEPARATOR;
    //    protected static final String SEND_MESSAGE_KEY = WebServer.SEND_MESSAGE_KEY;
//    protected static final String REGISTER_USER_KEY = WebServer.REGISTER_USER_KEY;
    private final String usersLogFile = WebServer.USERS_STORAGE_PATH;
    private final Logger usersLogger;
    private final String requestLogFile = WebServer.SERVER_LOG_PATH;
    private final Logger requestLogger;

    public ServerTask(Socket client) {
        this.client = client;
        usersLogger = new FileLogger(usersLogFile, false);
        requestLogger = new FileLogger(requestLogFile, true);
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

//                if (EXIT_WORD_EN.equalsIgnoreCase(requestStr.trim())
//                        || EXIT_WORD_RU.equalsIgnoreCase(requestStr.trim())) {
//                    System.out.println(COLOR + "Разорвано соединение. Онлайн " + WebServer.online.decrementAndGet());
//                    break;
//                }

//                buf.clear();
                System.out.println(COLOR + "Сервер получил запрос:\t" + requestStr);

                requestLogger.log(requestStr);

                final String result;
//                if (requestStr.contains(REGISTER_USER_KEY) || requestStr.contains(SEND_MESSAGE_KEY)) {
                if (requestStr.contains(Serializer.serialize(Commands.EXIT))
                        || requestStr.contains(Serializer.serialize(Commands.REGISTER_USER))
                        || requestStr.contains(Serializer.serialize(Commands.SEND_MESSAGE))) {
                    final Request request = Serializer.deserialize(requestStr, Request.class);
//                        final String command = request.getCommand();
                    final Commands command = request.getCommand();
                    if (Commands.EXIT.equals(command)) {
                        System.out.println(COLOR + "Разорвано соединение. Онлайн " + WebServer.online.decrementAndGet());
                        break;
                    }
                    final String body = request.getBody();
                    switch (command) {
                        case REGISTER_USER:
                            result = registerUser(body);
                            final StringBuilder sbr = new StringBuilder();
                            WebServer.users.values().forEach(user ->
                                            sbr.append(Serializer.serialize(user)).append(LOG_SEPARATOR));
                            sbr.delete(sbr.lastIndexOf(LOG_SEPARATOR), sbr.length());
                            usersLogger.log(sbr.toString());
//                            usersLogger.log(Serializer.serialize(WebServer.users));
                            break;
                        case SEND_MESSAGE:
                            result = sendMessage(Serializer.deserialize(body, Message.class));
                            final StringBuilder sbs = new StringBuilder();
                            WebServer.users.values().forEach(user ->
                                    sbs.append(Serializer.serialize(user)).append(LOG_SEPARATOR));
                            sbs.delete(sbs.lastIndexOf(LOG_SEPARATOR), sbs.length());
                            usersLogger.log(sbs.toString());
//                            usersLogger.log(Serializer.serialize(WebServer.users));
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
//                System.out.println("users: " + WebServer.users);
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
        if (!WebServer.users.containsKey(user.getName())) {
            WebServer.users.put(name, user);
            return "Успешно. Пользователь с именем \"" + name + "" + "\" зарегистрирован";
        }
        return "Успешно. Пользователь с именем \"" + name + "" + "\" авторизован";
    }

    private String sendMessage(Message message) {
        final String authorName = message.getAuthor();
        final String addresseeName = message.getAddressee();
        if (!WebServer.users.containsKey(authorName)) {
            return "Отказано. Сообщение не отправлено, отправитель " + authorName + " не найден в базе";
        }
        if (!WebServer.users.containsKey(addresseeName)) {
            return "Отказано. Сообщение не отправлено, получатель " + addresseeName + " не найден в базе";
        } else {
            WebServer.users.get(authorName).sendMessage(message);
            WebServer.users.get(addresseeName).receiveMessage(message);
            return "Успешно. Сообщение отправлено пользователю \"" + addresseeName + "\"";
        }
    }
}
