package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private final LocalDateTime SENT_TIME;
    private final String AUTHOR;
    private final String ADDRESSEE;
    private final String text;

    public Message(String author, String username, String text) {
        SENT_TIME = LocalDateTime.now();
        AUTHOR = author;
        ADDRESSEE = username;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Message message = (Message) o;
        return AUTHOR.equals(message.AUTHOR)
                && ADDRESSEE.equals(message.ADDRESSEE)
                && Objects.equals(text, message.text)
                && SENT_TIME.equals(message.SENT_TIME);
    }

    @Override
    public int hashCode() { return Objects.hash(AUTHOR, ADDRESSEE, text, SENT_TIME); }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb      .append("SENT_TIME=")   .append(SENT_TIME)
                .append(", AUTHOR='")   .append(AUTHOR)     .append('\'')
                .append(", ADDRESSEE='").append(ADDRESSEE)  .append('\'')
                .append(", text='")     .append(text)       .append('\'')
                .append('}');
        return sb.toString();
    }
}
