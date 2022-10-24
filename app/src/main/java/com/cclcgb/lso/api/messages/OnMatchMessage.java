package com.cclcgb.lso.api.messages;

import com.cclcgb.lso.api.LSOReader;
import com.cclcgb.lso.api.LSOWriter;
import com.cclcgb.lso.models.ILSOSerializable;
import com.cclcgb.lso.models.User;

public class OnMatchMessage implements ILSOSerializable {
    private User mUser;
    private int mMaxChatTimeInSeconds;

    @Override
    public void Serialize(LSOWriter writer) {}

    @Override
    public void Deserialize(LSOReader reader) {
        try {
            mUser = reader.readSerializable(new User());
            mMaxChatTimeInSeconds = reader.readInt();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public User getUser() {
        return mUser;
    }

    public int getMaxChatTimeInSeconds() {
        return mMaxChatTimeInSeconds;
    }
}
