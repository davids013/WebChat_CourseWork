package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final String NAME;
    private final String EMAIL;
    private final String PASSWORD;
    private final List<Message> incomingMessages = new ArrayList<>();
    private final List<Message> outgoingMessages = new ArrayList<>();

    public User(String name, String email, String password) {
        NAME = name;
        EMAIL = email;
        PASSWORD = password;
    }

    public boolean receiveMessage(Message message) { return outgoingMessages.add(message); }

    public boolean sendMessage(Message message) { return incomingMessages.add(message); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return EMAIL.equals(user.EMAIL);
    }

    @Override
    public int hashCode() { return Objects.hash(EMAIL); }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb      .append("NAME='")   .append(NAME)   .append('\'')
                .append(", EMAIL='").append(EMAIL)  .append('\'')
                .append('}');
        return sb.toString();
    }
}
