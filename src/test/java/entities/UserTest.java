package entities;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDateTime;

public class UserTest {
    final String name = "TestUser";

    @BeforeAll
    static void start() { Methods.start(); }

    @AfterAll
    static void end() { Methods.end(); }

    @BeforeEach
    public void newTest() { Methods.newTest(); }

    @AfterEach
    public void endTest() { Methods.endTest(); }

    @Test
    void sendMessageTest() {
        final Message message = Mockito.mock(Message.class);
        Mockito.when(message.getAuthor()).thenReturn("testAuthor");
        Mockito.when(message.getAddressee()).thenReturn("testAddressee");
        Mockito.when(message.getAddressee()).thenReturn("testAddressee");
        Mockito.when(message.getText()).thenReturn("testText");
        Mockito.when(message.getSentTime()).thenReturn(LocalDateTime.now());

        final User user = new User(name);
        user.sendMessage(message);
        new File(user.getLogFile()).delete();

        Assertions.assertTrue(user.getOutgoingMessages().contains(message));
    }

    @Test
    void receiveMessageTest() {
        final Message message = Mockito.mock(Message.class);
        Mockito.when(message.getAuthor()).thenReturn("testAuthor");
        Mockito.when(message.getAddressee()).thenReturn("testAddressee");
        Mockito.when(message.getAddressee()).thenReturn("testAddressee");
        Mockito.when(message.getText()).thenReturn("testText");
        Mockito.when(message.getSentTime()).thenReturn(LocalDateTime.now());

        final User user = new User(name);
        user.receiveMessage(message);
        new File(user.getLogFile()).delete();

        Assertions.assertTrue(user.getIncomingMessages().contains(message));
    }
}
