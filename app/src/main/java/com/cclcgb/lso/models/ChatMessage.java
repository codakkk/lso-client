package com.cclcgb.lso.models;

public class ChatMessage {
    private String messageId;
    private String message;
    private int senderId;

    public ChatMessage(String message, int senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public ChatMessage() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
}
