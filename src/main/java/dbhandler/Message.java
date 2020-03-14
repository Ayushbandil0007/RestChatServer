package dbhandler;

/**
 * Created by Ayush Bandil on 12/2/2020.
 */
public class Message {
    private String topic;
    private String message;
    private int time;
    private String username;

    public Message(String topic, String message, int time, String username) {
        this.topic = topic;
        this.message = message;
        this.time = time;
        this.username = username;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
