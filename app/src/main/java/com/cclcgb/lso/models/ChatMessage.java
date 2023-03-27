package com.cclcgb.lso.models;

public class ChatMessage {
    private ChatMessageType mType;
    private User mUser;
    private String mMessage;

    private int mColor;

    private ChatMessageJoinRequestState mRequestState;

    public ChatMessage(ChatMessageType type, User user, String message, int color) {
        this();
        mType = type;
        mUser = user;
        mMessage = message;
        mColor = color;
    }

    public ChatMessage() {
        mRequestState = ChatMessageJoinRequestState.Waiting;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public User getUser() {
        return mUser;
    }
    public int getColor() {
        return mColor;
    }

    public ChatMessageType getType() {
        return mType;
    }
    public ChatMessageJoinRequestState getRequestState() {
        return mRequestState;
    }

    public void setRequestState(ChatMessageJoinRequestState state) {
        mRequestState = state;
    }
}
