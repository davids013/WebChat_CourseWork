package entities;

public class Request {
    private final String command;
    private final String body;

    public Request(String command, String body) {
        this.command = command;
        this.body = body;
    }

    public String getCommand() {
        return command;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() { return "Request{command=" + command + ", body=" + body + "}"; }
}
