package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.User;

public class MessageReceivedMessage implements ILSOSerializable {
    private User mUser;
    private String mMessage;

    public MessageReceivedMessage() {}

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mUser = reader.readSerializable(new User());
            mMessage = reader.readString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() { return mUser; }
    public String getMessage() {
        return mMessage;
    }
}
