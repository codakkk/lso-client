package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.User;

public class Message implements ILSOSerializable {
    private User mUser;
    private String mMessage;

    public Message() {}

    public Message(String message) {
        mMessage = message;
    }

    @Override
    public void Serialize(LSOWriter writer) {
        writer.write(mMessage);
    }

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
