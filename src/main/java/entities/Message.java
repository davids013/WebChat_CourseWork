package entities;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    public static final String ATTR_SEP = "<>";
    private final LocalDateTime SENT_TIME;
    private final String AUTHOR;
    private final String ADDRESSEE;
    private final String TEXT;

    public Message(String author, String username, String text) {
        SENT_TIME = LocalDateTime.now();
        AUTHOR = author;
        ADDRESSEE = username;
        this.TEXT = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Message message = (Message) o;
        return AUTHOR.equals(message.AUTHOR)
                && ADDRESSEE.equals(message.ADDRESSEE)
                && Objects.equals(TEXT, message.TEXT)
                && SENT_TIME.equals(message.SENT_TIME);
    }

    @Override
    public int hashCode() { return Objects.hash(AUTHOR, ADDRESSEE, TEXT, SENT_TIME); }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb      .append("SENT_TIME=")   .append(SENT_TIME)
                .append(", AUTHOR='")   .append(AUTHOR)     .append('\'')
                .append(", ADDRESSEE='").append(ADDRESSEE)  .append('\'')
                .append(", text='")     .append(TEXT)       .append('\'')
                .append('}');
        return sb.toString();
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

    public static Message deserialize(String messageJson) {
        return new Gson().fromJson(messageJson, Message.class);
    }
}
