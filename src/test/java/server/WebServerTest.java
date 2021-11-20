package server;

import com.google.gson.Gson;
import entities.*;
import methods.Methods;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;

public class WebServerTest {
    @BeforeAll
    static void start() { Methods.start(); }

    @AfterAll
    static void end() { Methods.end(); }

    @BeforeEach
    public void newTest() { Methods.newTest(); }

    @AfterEach
    public void endTest() { Methods.endTest(); }

    @Test
    void startTest() throws IOException, InterruptedException {
        boolean[] result = {false, false, false};
        String userName = "testUser";
        final Gson gson = new Gson();
        final String[] hostAndPort = ConfigWorker.getHostAndPortFromConfig(WebServer.SETTINGS_FILE_PATH);
        final String host = hostAndPort[ConfigWorker.HOST_INDEX];
        final int port = Integer.parseInt(hostAndPort[ConfigWorker.PORT_INDEX]);
        final Thread serverThread = new Thread(WebServer::start);
        serverThread.start();
        try (final Socket socket = new Socket(host, port);
             final BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             final PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream()), true)) {

            while (WebServer.getUsers().containsKey(userName)) { userName += "+"; }
            Request request = new Request(Commands.REGISTER_USER, userName);
            out.println(gson.toJson(request));
            if (in.readLine().startsWith("Успешно") && WebServer.getUsers().containsKey(userName)) {
                result[0] = true;
            }

            final Message message = new Message(userName, userName, "testMessage");
            request = new Request(Commands.SEND_MESSAGE, gson.toJson(message));
            out.println(gson.toJson(request));
            if (in.readLine().startsWith("Успешно")
                    && WebServer.getUsers().get(userName).getIncomingMessages().contains(message)
                    && WebServer.getUsers().get(userName).getOutgoingMessages().contains(message)) {
                result[1] = true;
            }

            request = new Request(Commands.EXIT, "exit");
            final int online = WebServer.getOnline();
            out.println(gson.toJson(request));
            Thread.sleep(100);                      // Пауза на удаление
            if (WebServer.getOnline() == online - 1) {
                result[2] = true;
            }

            final String logFile = WebServer.getUsers().get(userName).getLogFile();
            new File(logFile).delete();

            Assertions.assertTrue(result[0] && result[1] && result[2]);
        }
    }

}
