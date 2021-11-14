package entities;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private final LocalDateTime sentTime;
    private final String author;
    private final String addressee;
    private final String text;

    public Message(String author, String addressee, String text) {
        sentTime = LocalDateTime.now();
        this.author = author;
        this.addressee = addressee;
        this.text = text;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getAddressee() {
        return addressee;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Message message = (Message) o;
        return author.equals(message.author)
                && addressee.equals(message.addressee)
                && Objects.equals(text, message.text)
                && sentTime.equals(message.sentTime);
    }

    @Override
    public int hashCode() { return Objects.hash(author, addressee, text, sentTime); }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb      .append("SENT_TIME=")   .append(sentTime)
                .append(", AUTHOR='")   .append(author)     .append('\'')
                .append(", ADDRESSEE='").append(addressee)  .append('\'')
                .append(", text='")     .append(text)       .append('\'')
                .append('}');
        return sb.toString();
    }

//    public String serialize() {
//        return new Gson().toJson(this);
//    }
//
//    public static Message deserialize(String messageJson) {
//        return new Gson().fromJson(messageJson, Message.class);
//    }
}
