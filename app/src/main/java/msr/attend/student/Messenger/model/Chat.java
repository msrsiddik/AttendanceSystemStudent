package msr.attend.student.Messenger.model;

import java.util.Objects;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private Long dateLong;
    private boolean isseen;
    private String msgId;

    public Chat(String sender, String receiver, String message, Long dateLong, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.dateLong = dateLong;
        this.isseen = isseen;
    }

    public Chat(String sender, String receiver, String message, Long dateLong, boolean isseen, String msgId) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.dateLong = dateLong;
        this.isseen = isseen;
        this.msgId = msgId;
    }

    public Chat() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return isseen == chat.isseen &&
                Objects.equals(sender, chat.sender) &&
                Objects.equals(receiver, chat.receiver) &&
                Objects.equals(message, chat.message) &&
                Objects.equals(dateLong, chat.dateLong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, message, dateLong, isseen);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDateLong() {
        return dateLong;
    }

    public void setDateLong(Long dateLong) {
        this.dateLong = dateLong;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
